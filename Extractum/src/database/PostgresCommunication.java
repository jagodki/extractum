/*
 * Copyright 2017 Christoph.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoph
 */
public class PostgresCommunication {
    
    private Database db;
    ResultSet rs = null;
    Statement st =  null;

    public PostgresCommunication() {
    }

    public PostgresCommunication(Database db) {
        this.db = db;
    }

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }
    
    public boolean insertData(String table, String[] columns, String[] colTypes, String[] datasets, String template) {
        //create statement
        String columnsString = "";
        for(int i = 0; i < columns.length; i++) {
            columnsString += columns[i];
            if(i != columns.length - 1) {
                columnsString += ",\n";
            }
        }
        
        String values = "";
        for(int i = 0; i < datasets.length; i++) {
            values += "'" + datasets[i] + "'" + "::" + colTypes[i];
            if(i != datasets.length - 1) {
                values += ",\n";
            }
        }
        
        String sqlCommand = template.replace("&table&", table).replace("&columns&", columnsString).replace("&values&", values);
        
        //execute the statement
        try {
            this.st = this.db.getConnection().createStatement();
            st.executeUpdate(sqlCommand);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean createTable(String tableName, String[] columnNames, String[] colTypes, String template) {
        //create the statement
        String columns = "";
        for(int i = 0; i < columnNames.length; i++) {
            columns += columnNames[i] + " " + colTypes[i];
            if(i != columnNames.length - 1) {
                columns += ",\n";
            }
        }
        
        String sqlCommand = template.replace("&table&", tableName).replace("&columns&", columns);
        
        //execute the statement
        try {
            this.st = this.db.getConnection().createStatement();
            st.executeUpdate(sqlCommand);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean SetPrimaryKey(String tableName, String[] columnNames, String template) {
        
    }
    
    public boolean SetForeignKey(String tableName, String[] columnNames, String referencedTable, String[] referencedColumns, String template) {
        
    }
    
}

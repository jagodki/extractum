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
    
    public boolean insertData(String table, String[] columns, String[] colTypes, String[] datasets) {
        
    }
    
    public boolean updateData(String table, String[] columns, String[] colTypes, String[] datasets) {
        
    }
    
    public boolean createTable(String tableName, String[] columnNames, String[] colTypes) {
        //create the statement
        String sqlCommand = "create table " + tableName + "(\n";
        for(int i = 0; i < columnNames.length; i++) {
            sqlCommand += columnNames[i] + " " + colTypes[i];
            if(i != columnNames.length - 1) {
                sqlCommand += ",\n";
            }
        }
        sqlCommand += ");";
        
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
    
}

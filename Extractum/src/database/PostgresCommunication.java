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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides funcionalities for the communication with a PostgreSQL-Server, 
 * i.e. execute SQL-commands and parsing the results of queries, to provide them 
 * to other objects.
 * @author Christoph
 */
public class PostgresCommunication {
    
    private Database db;
    private ResultSet rs = null;
    private Statement st =  null;

    /**
     * The empty constructor of the class.
     */
    public PostgresCommunication() {
    }

    /**
     * The constructor of this class
     * @param db a Database object
     */
    public PostgresCommunication(Database db) {
        this.db = db;
    }

    /**
     * This function returns the Database object of the class.
     * @return the Database object
     */
    public Database getDb() {
        return db;
    }

    /**
     * This function sets a new Database object.
     * @param db the Database object
     */
    public void setDb(Database db) {
        this.db = db;
    }
    
    /**
     * This function inserts new datasets into a database table.
     * @param table the name of the table, in which the datasets has to be insert
     * @param columns an array of column names for inserting the datasets
     * @param colTypes an array of column types for parsing the datasets in the correct type
     * @param datasets an array of comma-separeted datasets
     * @param template the template of the insert-statement
     * @return true whether insert was correct, otherwise false
     */
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
            this.st = null;
            this.st = this.db.getConnection().createStatement();
            st.executeUpdate(sqlCommand);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * This function creates a new table in the database
     * @param tableName the name of the new table
     * @param columnNames an array of column names for the new table
     * @param colTypes an array of column types for the new table
     * @param template a template of the update-SQL-command
     * @return true whether insert was correct, otherwise false
     */
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
            this.st = null;
            this.st = this.db.getConnection().createStatement();
            st.executeUpdate(sqlCommand);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PostgresCommunication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * This functions executes a select-statement, i.e. it queries data from the database.
     * This function returns [null], if an excpetion was thrown or if the sql statement
     * does not start with the key word <i>select</i>.
     * @param sqlCommand the sql statement
     * @return the result set as a List of Strings, each entry contains a comma-separeted dataset
     */
    public List<String> selectData(String sqlCommand) {
        if(sqlCommand.startsWith("select")) {
            //execute the statement
            try {
                this.st = this.db.getConnection().createStatement();
                this.rs = st.executeQuery(sqlCommand);

                //create the resulting list
                List<String> result = new ArrayList<>();
                int i = 0;
                while(this.rs.next()) {
                    String resultEntry = "";
                    try {
                        resultEntry += this.rs.getString(i) + ";";
                    } catch(Exception ex) {
                        //exception will be thrown, if index is out of bounds
                        if(resultEntry.length() > 0) {
                            //remove the last character (;) from our result string
                            resultEntry = resultEntry.substring(0, resultEntry.length() - 2);
                            result.add(resultEntry);
                        } else {
                            //problem detected

                        }
                    }
                    i++;
                }
                return result;

            } catch (SQLException ex) {
                Logger.getLogger(PostgresCommunication.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    public List<String> selectColumnNamesTypesOfTable(String table, String template) {
        String sqlStatement = template.replace("&table&", table);
        List<String> result = this.selectData(sqlStatement);
        return result;
    }
    
    public List<String> selectColumnsOfConstraint(String table, String constraint, String template) {
        String sqlStatement = template.replace("&table&", table).replace("&constraint&", constraint);
        List<String> result = this.selectData(sqlStatement);
        return result;
    }
    
    public List<String> selectTablesOfSchema(String schema, String template) {
        String sqlStatement = template.replace("&schema&", schema);
        List<String> result = this.selectData(sqlStatement);
        return result;
    }
    
}

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

import Utilities.LogArea;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides funcionalities for the communication with a PostgreSQL-Server, 
 * i.e. execute SQL-commands and parsing the results of queries, to provide them 
 * to other objects.
 * @author Christoph
 */
public class PostgresCommunication {
    
    private Database db;

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
     * @param log an object for logging and displaying information to the user
     * @return true whether insert was correct, otherwise false
     */
    public boolean insertData(String table, String[] columns, String[] colTypes, String[] datasets, String template, LogArea log) {
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
            PreparedStatement st = this.db.getConnection().prepareStatement(sqlCommand);
            st.executeUpdate();
            return true;
        } catch (SQLException ex) {
            log.log(LogArea.ERROR, "sql command failed", ex);
            return false;
        }
    }
    
    /**
     * This function creates a new table in the database
     * @param tableName the name of the new table
     * @param columnNames an array of column names for the new table
     * @param colTypes an array of column types for the new table
     * @param template a template of the update-SQL-command
     * @param log an object for logging and displaying information to the user
     * @return true whether insert was correct, otherwise false
     */
    public boolean createTable(String tableName, String[] columnNames, String[] colTypes, String template, LogArea log) {
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
            PreparedStatement st = this.db.getConnection().prepareStatement(sqlCommand);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException ex) {
            log.log(LogArea.ERROR, "sql command CREATE TABLE failed", ex);
            return false;
        }
    }
    
    /**
     * This functions executes a select-statement, i.e. it queries data from the database.
     * This function returns [null], if an excpetion was thrown or if the sql statement
     * does not start with the key word <i>select</i>.
     * @param sqlCommand the sql statement
     * @param log an object for logging and displaying information to the user
     * @return the result set as a List of Strings, each entry contains a comma-separeted dataset
     */
    public List<String> selectData(String sqlCommand, LogArea log) {
        if(sqlCommand.toUpperCase().startsWith("SELECT")) {
            //execute the statement
            try {
                PreparedStatement st = this.db.getConnection().prepareStatement(sqlCommand);
                ResultSet rs = st.executeQuery();

                //create the resulting list
                List<String> result = new ArrayList<>();
                int columnCount = rs.getMetaData().getColumnCount();
                while(rs.next()) {
                    String resultEntry = "";
                    for(int i = 1; i <= columnCount; i++) {
                        resultEntry += rs.getString(i) + ";";
                    }
                    //remove the last character (;) from our result string
                    if(resultEntry.length() > 1) {
                        resultEntry = resultEntry.substring(0, resultEntry.length() - 1);
                    }
                    result.add(resultEntry);
                }
                st.close();
                return result;

            } catch (SQLException ex) {
                log.log(LogArea.ERROR, "sql command failed", ex);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * This function returns the names and types of all columns of on table.
     * <p>The column names have index 0, the data types index 1.
     * <br>The indices are separeted by semicolons.
     * @param table the name of the table as String
     * @param template the correct template for the sql command
     * @param log an object for logging information
     * @return the result of the commando as a list of comma separeted Strings
     */
    public List<String> selectColumnNamesTypesOfTable(String table, String template, LogArea log) {
        String sqlStatement = template.replace("&table&", table);
        List<String> result = this.selectData(sqlStatement, log);
        return result;
    }
    
    /**
     * This function returns the columns, which are part of a contraint.
     * @param table the name of the table as String
     * @param constraint the name of the constraint as String
     * @param template the correct template for the sql command
     * @param log an object for logging information
     * @return the result of the commando as a list of comma separeted Strings
     */
    public List<String> selectColumnsOfConstraint(String table, String constraint, String template, LogArea log) {
        String sqlStatement = template.replace("&table&", table).replace("&constraint&", constraint);
        List<String> result = this.selectData(sqlStatement, log);
        return result;
    }
    
    /**
     * This function returns all table names of a schema.
     * @param schema the name of the schema as String
     * @param template the correct template for the sql command
     * @param log an object for logging information
     * @return the result of the commando as a list of comma separeted Strings
     */
    public List<String> selectTablesOfSchema(String schema, String template, LogArea log) {
        String sqlStatement = template.replace("&schema&", schema);
        List<String> result = this.selectData(sqlStatement, log);
        return result;
    }
    
    /**
     * This function creates a new view.
     * @param viewName name of the new view
     * @param template the correct template for the sql command
     * @param log an object for logging information
     * @return true whether view was created, otherwise false
     */
    public boolean createView(String viewName, String template, LogArea log) {
        String sqlCommand = template.replace("&name&", viewName);
        
        //execute the statement
        try {
            PreparedStatement st = this.db.getConnection().prepareStatement(sqlCommand);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException ex) {
            log.log(LogArea.ERROR, "sql command CREATE VIEW failed", ex);
            return false;
        }
    }
    
    /**
     * This function creates a new schema.
     * @param schemaName name of the new schema
     * @param template the correct template for the sql command
     * @param log an object for logging information
     * @return true whether schema was created, otherwise false
     */
    public boolean createSchema(String schemaName, String template, LogArea log) {
        String sqlCommand = template.replace("&name&", schemaName);
        
        //execute the statement
        try {
            PreparedStatement st = this.db.getConnection().prepareStatement(sqlCommand);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException ex) {
            log.log(LogArea.ERROR, "sql command CREATE SCHEMA failed", ex);
            return false;
        }
    }
    
}

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
package importing;

import Utilities.LogArea;
import database.PostgresCommunication;
import exporting.ExportTableContent;
import exporting.ExportTableModel;
import extractumXml.ColType;
import extractumXml.DatabaseType;
import extractumXml.ForeignKeyType;
import extractumXml.PrimaryKeyType;
import extractumXml.TableType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

/**
 * This class provides functions for the data import.
 * @author Christoph
 */
public class ImportHandler {
    
    private DatabaseType dbt;

    /**
     * The constructor of this class.
     */
    public ImportHandler() {
        this.dbt = null;
    }
    
    /**
     * This function loads a configuration file (XML) into the JAXB-class-structure.
     * @param path the absolute path to the config file
     * @param log a LogArea object
     * @return false if something went wrong
     */
    public DatabaseType loadConfigFile(String path, LogArea log) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DatabaseType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (DatabaseType) JAXBIntrospector.getValue(jaxbUnmarshaller.unmarshal(new File(path)));
        } catch (JAXBException ex) {
            log.log(LogArea.ERROR, "import of config file was not successfull", ex);
            return null;
        }
    }
    
    /**
     * This function inits the import table from the information of a config file.
     * @param path the absolute path to the config file
     * @param log a LogArea object
     * @param importTable the table model of the import table
     * @param pb a progressbar for showing the progress of import
     * @param sqlStatements a HashMap with tables as keys and their SQL-statements as values, will
     * be filled up with information from the config file
     */
    public void initImportTableFromConfigFile(String path,
                                              LogArea log,
                                              ImportTableModel importTable,
                                              JProgressBar pb,
                                              HashMap<String, String> sqlStatements) {
        //import the config file
        this.dbt = null;
        this.dbt = this.loadConfigFile(path, log);
        
        //preparation of the iteration
        List<TableType> tables = this.dbt.getTable();
        pb.setValue(0);
        pb.setMaximum(tables.size());
        List<ImportTableContent> li = new ArrayList<>();
        
        //iterate through the jaxb object structur and fill the table
        for(TableType table : tables) {
            ImportTableContent tableContent = new ImportTableContent();
            tableContent.setTableName(table.getName());
            tableContent.setPath(table.getPath());
            tableContent.setImportTable(true);
            
            //PKs
            String primaryKeys = "";
            List<PrimaryKeyType> pkt = table.getPrimaryKeys().getPrimaryKey();
            for(PrimaryKeyType pk : pkt) {
                primaryKeys += pk.getColumn();
            }
            tableContent.setPrimaryKey(primaryKeys);
            
            //FKs
            String foreignKeys = "";
            List<ForeignKeyType> fkt = table.getForeignKeys().getContent();
            for(Object fk : fkt) {
                if(fk.getClass() == ForeignKeyType.class) {
                    foreignKeys += ((ForeignKeyType) fk).getColumn();
                }
            }
            tableContent.setForeignKey(foreignKeys);
            
            //SQL-statements
            sqlStatements.put(table.getName(), table.getSql().trim());
            
            //add the new line  the list of the table content
            li.add(tableContent);
            
            pb.setValue(pb.getValue() + 1);
        }
        importTable.setLi(li);
    }
    
    /**
     * This function inits the export table from the information of a config file.
     * @param path the absolute path to the config file
     * @param log a LogArea object
     * @param exportTable the table model of the export table
     * @param pb a progressbar for showing the progress of import
     * @param sqlStatements a HashMap with tables as keys and their SQL-statements as values, will
     * be filled up with information from the config file
     */
    public void initExportTableFromConfigFile (String path,
                                               LogArea log,
                                               ExportTableModel exportTable,
                                               JProgressBar pb,
                                               HashMap<String, String> sqlStatements) {
        //import the config file
        this.dbt = null;
        this.dbt = this.loadConfigFile(path, log);
        
        //preparation of the iteration
        List<TableType> tables = this.dbt.getTable();
        pb.setValue(0);
        pb.setMaximum(tables.size());
        List<ExportTableContent> li = new ArrayList<>();
        
        //iterate through the jaxb object structur and fill the table
        for(TableType table : tables) {
            ExportTableContent tableContent = new ExportTableContent();
            tableContent.setTableName(table.getName());
            tableContent.setExportTable(true);
            
            //PKs
            String primaryKeys = "";
            List<PrimaryKeyType> pkt = table.getPrimaryKeys().getPrimaryKey();
            for(PrimaryKeyType pk : pkt) {
                primaryKeys += pk.getColumn();
            }
            tableContent.setPrimaryKey(primaryKeys);
            
            //SQL-statements
            sqlStatements.put(table.getName(), table.getSql().trim());
            
            //add the new line  the list of the table content
            li.add(tableContent);
            
            pb.setValue(pb.getValue() + 1);
        }
        exportTable.setLi(li);
    }
    
    /**
     * This function creates a new view called "extractum" in the database.
     * @param viewName the name of the view
     * @param template the SQL-template
     * @param pgc an object for communicate with the database
     * @param log a LogArea object
     */
    private void createView(String viewName, String template, PostgresCommunication pgc, LogArea log) {
        boolean result = pgc.createView(viewName, template, log);
        if(!result) {
            JOptionPane.showMessageDialog(null,
                        "Cannot create a new view called EXTRACTUM. Maybe it already exists. Please check the log and database.",
                        "Import Data",
                        JOptionPane.ERROR_MESSAGE);
        } else {
            log.log(LogArea.INFO, "new view created", null);
        }
    }
    
    /**
     * This function creates a new schema called "extractum" in the database.
     * @param schemaName the name of the view
     * @param template the SQL-template
     * @param pgc an object for communicate with the database
     * @param log a LogArea object
     */
    private void createSchema(String schemaName, String template, PostgresCommunication pgc, LogArea log) {
        boolean result = pgc.createSchema(schemaName, template, log);
        if(!result) {
            JOptionPane.showMessageDialog(null,
                        "Cannot create a new schemaName called EXTRACTUM.\nMaybe it already exists.\nPlease check the log and database.",
                        "Import Data",
                        JOptionPane.ERROR_MESSAGE);
        } else {
            log.log(LogArea.INFO, "new SCHEMA created", null);
        }
    }
    
    /**
     * This function creates a new table in the database.
     * @param template the SQL-template for the CREATE TABLE-query
     * @param tableName the name of the new table
     * @param columnNames the names of the columns as an array of Strings
     * @param colTypes the types of the columns as an array of Strings
     * @param pgc an object for communicate with the database
     * @param log a LogArea object
     */
    private void createTable(String template,
                             String tableName,
                             String[] columnNames,
                             String[] colTypes,
                             PostgresCommunication pgc,
                             LogArea log) {
        boolean result = pgc.createTable(tableName, columnNames, colTypes, template, log);
        if(!result) {
            JOptionPane.showMessageDialog(null,
                        "Cannot create a new table called " + tableName + ". Maybe it already exists. Please check the log.",
                        "Import Data",
                        JOptionPane.ERROR_MESSAGE);
        } else {
            log.log(LogArea.INFO, "new table " + tableName + " created", null);
        }
    }
    
    /**
     * This function imports a given dataset into a table of the database.
     * @param template the SQL-template for inserting values
     * @param tableName the name of the table
     * @param columnNames the names of the columns as an array of Strings
     * @param colTypes the types of the columns as an array of Strings
     * @param data the data as a semicolon-separated String
     * @param log a LogArea object
     * @param pgc an object for communicate with the database
     */
    private void importDataset(String template,
                               String tableName,
                               String[] columnNames,
                               String[] colTypes,
                               String data,
                               LogArea log,
                               PostgresCommunication pgc) {
        int countOfData = data.split(";").length;
        
        if(columnNames.length != countOfData) {
            log.log(LogArea.WARNING, "the count of column names and dataset " + data + " is unequal", null);
        } else {
            //convert the dataset from array of Strings to String
            String[] dataArray = data.split(";");
            
            //insert the dataset
            boolean result = pgc.insertData(tableName, columnNames, colTypes, dataArray, template, log);
            
            if(!result) {
            JOptionPane.showMessageDialog(null,
                        "Cannot insert the following dataset, please check the log for further information:\n" + data,
                        "Import Data",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * This function prepares the database for the data import (e.g. create schema and tables)
     * and at the end, starts the import of the data.
     * @param schemaTemplate the SQL-template for creating a new schema
     * @param tableTemplate the SQL-template for creating a new table
     * @param importTemplate the SQL-template for insert values
     * @param log a LogArea object
     * @param mainPb the main progressbar
     * @param secondPb the minor progressbar
     * @param pgc an object for communicate with the database
     * @param itm the table model of the import table
     * @param path the directory of the config file
     */
    public void importData(String schemaTemplate,
                           String tableTemplate,
                           String importTemplate,
                           LogArea log,
                           JProgressBar mainPb,
                           JProgressBar secondPb,
                           PostgresCommunication pgc,
                           ImportTableModel itm,
                           String path) {
        //create the new schema for importing the data
        String importSchema = "extractum";
        this.createSchema(importSchema, schemaTemplate, pgc, log);
        
        //iterate through the table of the import tab
        int iMax = itm.getRowCount();
        for(int i = 0; i < iMax; i++) {
            ImportTableContent itcRow = itm.getRow(i);
            if(itcRow.isImportTable()) {
                
                //search the current table in the XML data structur
                for(TableType tt : this.dbt.getTable()) {
                    if(tt.getName().equals(itcRow.getTableName())) {
                        
                        //store the names and types of the columns in arrays of Strings
                        List<ColType> columnsList = tt.getColumns().getCol();
                        String[] columnNames = new String[columnsList.size()];
                        String[] columnTypes = new String[columnsList.size()];
                        int j = 0;
                        for(ColType column : columnsList) {
                            columnNames[j] = column.getName();
                            columnTypes[j] = column.getType();
                            
                            //avoid the usage of type serial, because data cannot be casted into serial
                            if(columnTypes[j].equals("serial")) {
                                columnTypes[j] = "integer";
                            }
                            j++;
                        }
                        
                        //create the table
                        this.createTable(tableTemplate, importSchema + "." + tt.getName(), columnNames, columnTypes, pgc, log);
                        
                        try(BufferedReader br = new BufferedReader(new FileReader(path + File.separator + tt.getPath()))) {
                            String currentLine;
                            boolean notFirstLine = false;
                            
                            //iterate through the content of the file and import the datasets
                            while((currentLine = br.readLine()) != null) {
                                if(notFirstLine) {
                                    this.importDataset(importTemplate, importSchema + "." + tt.getName(), columnNames, columnTypes, currentLine, log, pgc);
                                } else {
                                    notFirstLine = true;
                                }
                            }
                        } catch (IOException ex) {
                            log.log(LogArea.ERROR, "cannot read the CSV file " + path + File.pathSeparator + tt.getPath(), ex);
                            JOptionPane.showMessageDialog(null,
                                                          "Error while reading a CSV file. Check the log for further information.",
                                                          "Import Data",
                                                          JOptionPane.ERROR_MESSAGE);
                            log.log(LogArea.ERROR, "not able to read the selected file", ex);
                        }
                        
                    }
                }
            }
        }
    }

    /**
     * This function retunrs the Database member of this class.
     * @return a Database object
     */
    public DatabaseType getDbt() {
        return this.dbt;
    }
    
    /**
     * This function sets a new Database member for this class.
     * @param dbt the new Database object
     */
    public void setDbt(DatabaseType dbt) {
        this.dbt = dbt;
    }
}

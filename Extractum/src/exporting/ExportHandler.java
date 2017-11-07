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
package exporting;

import Utilities.LogArea;
import database.PostgresCommunication;
import extractumXml.DatabaseType;
import extractumXml.TableType;
import extractumXml.ColType;
import extractumXml.ColumnsType;
import extractumXml.ForeignKeyType;
import extractumXml.ForeignKeysType;
import extractumXml.PrimaryKeyType;
import extractumXml.PrimaryKeysType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * This class provides functions for the data export.
 * @author Christoph
 */
public class ExportHandler {

    /**
     * The constructor of this class.
     */
    public ExportHandler() {
    }
    
    /**
     * This function writes a given List of Strings to a file.
     * <p>If a file with the same absolute path already exists, the function
     * will try to delete it. If it is not possible, the export will be canceled.</p>
     * @param path the absolute path of the new file
     * @param datasets a List of Strings with the content, that should be exported
     * @param log a LogArea object for logging information
     * @param columnNames a String containing the names of the columns (should be comma separeted)
     * @param pb a progress bar object to indicate the progress of the export
     * @return true whether export was successfull, otherwise false
     */
    private boolean writeDatasetsToCsv(String path,
                                       List<String> datasets,
                                       LogArea log,
                                       String columnNames,
                                       JProgressBar pb) {
        //init the progressbar
        pb.setMaximum(datasets.size());
        pb.setValue(0);
        
        //check, whether a file with the specified path exists already and delete it if necessary
        boolean deleteFile = this.deleteExistingFile(path, log);
        if(!deleteFile) {
            return false;
        }
        
        //add each list entry to the specified file
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(columnNames + "\n");
            for(String dataset : datasets) {
                bw.write(dataset + "\n");
                pb.setValue(pb.getValue() + 1);
            }
        } catch(IOException ex) {
            log.log(LogArea.ERROR, "cannot write data to file", ex);
            JOptionPane.showMessageDialog(null,
                        "Cannot write data to the export file - see log window for further information.",
                        "Export Data",
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * This function exports the data of a table into a CSV file
     * <p>the CSV file will be named by the name of the table.</p>
     * <br>The function exports all tables in the given JAXB construction.
     * <br>The first line of each file contains the column names.
     * @param dbt the root element of the JAXB object
     * @param pgc an object for the communication with the PostgreSQL database
     * @param pbMain a first progressbar
     * @param pbSec a second progressbar
     * @param log an object for logging information
     * @param directory the directory, where all files should be saved in
     */
    public void exportToCSV(DatabaseType dbt,
                            PostgresCommunication pgc,
                            JProgressBar pbMain,
                            JProgressBar pbSec,
                            LogArea log,
                            String directory) {
        //init routines
        List<TableType> tableList = dbt.getTable();
        pbMain.setValue(0);
        pbMain.setMaximum(tableList.size());
        
        //create directy for storing the data
        String exportDirectory = directory + File.separator + "data";
        new File(exportDirectory).mkdir();
        
        //iterate over the tables
        for(TableType table : tableList) {
            pbSec.setValue(0);
            
            //log information
            log.log(LogArea.INFO, "start export of table " + table.getName(), null);
            
            //get the column names for the heading line
            String headingLine = "";
            List<ColType> allColumns = table.getColumns().getCol();
            for(ColType oneColumn : allColumns) {
                headingLine += oneColumn.getName() + ";";
            }
            headingLine = headingLine.substring(0, headingLine.lastIndexOf(";"));
            
            //get all datasets of the table
            List<String> datasets = pgc.selectData(table.getSql(), log);
            
            //write all the data to a file
            this.writeDatasetsToCsv(exportDirectory + File.separator + table.getName() + ".csv", datasets, log, headingLine, pbSec);
            
            pbMain.setValue(pbMain.getValue() + 1);
        }
    }
    
    /**
     * This function deletes an existing file.
     * <p>If the file can be deleted successfully, the function returns <code>true</code>.
     * <p>Whether it is not possible to delete the file, an exception will be send to
     * a log object for further information.
     * <p>Whether a file with the specified name does not exist, the function will
     * return <code>true</code> and an information will be send to the log object.
     * @param path the absolute path as String of the file
     * @param log an object for logging information
     * @return true whether the file was deleted successfully, otherwise false
     */
    private boolean deleteExistingFile(String path, LogArea log) {
        //check, whether a file with the specified path exists already and delete it if necessary
        File file = new File(path);
        if (file.exists()) {
            boolean deleteOfFile = file.delete();
            if(!deleteOfFile) {
                log.log(LogArea.WARNING, "file already exists and not possible to remove it", null);
                JOptionPane.showMessageDialog(null,
                        "Already existing file cannot removed - see log window for further information.",
                        "Export Data",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
    
    /**
     * This function exports a given JAXB-root element to an XML file.
     * @param configPath the absolute path as String as destination of the export
     * @param rootObject the JAXB-root element
     * @param log an object for logging information
     * @param tableContent the table of the export tab of the gui
     * @param pgc the database communicator
     * @param sqlConstraint the sql-template for querying constraints
     * @param sqlTypes the sql-template for querying types of columns
     * @param sqlStatements a HashMap with table names and corresponding sql statements as key-value-pair
     * @param dbName a String representing the name of the database
     * @return true whether export was successfull, otherwise false
     */
    public boolean exportToXml(String configPath,
                               DatabaseType rootObject,
                               LogArea log,
                               ExportTableModel tableContent,
                               PostgresCommunication pgc,
                               String sqlConstraint,
                               String sqlTypes,
                               HashMap<String, String> sqlStatements,
                               String dbName) {
        //check, whether a file with the specified path exists already and delete it if necessary
        boolean deleteFile = this.deleteExistingFile(configPath, log);
        if(!deleteFile) {
            return false;
        }
        
        File outputFile = new File(configPath);
        try {
            //init components
            JAXBContext jaxbContext = JAXBContext.newInstance(DatabaseType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            
            //pretty print
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //export
            jaxbMarshaller.marshal(rootObject, outputFile);
        } catch (JAXBException ex) {
            log.log(LogArea.ERROR, "JAXB-configuration failed - unable to marshall java objects", ex);
            JOptionPane.showMessageDialog(null,
                    "Export of XML-file failed - see log window for further information.",
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * This function extracts all needed information from the database to fill
     * the JAXB-objects for storing extractum configuration.
     * @param tableContent the table of the export tab of the gui
     * @param pgc the database communicator
     * @param log an object for logging information
     * @param sqlConstraint the sql-template for querying constraints
     * @param sqlTypes the sql-template for querying types of columns
     * @param sqlStatements the sql-select-statements as a HashMap of strings and strings
     * @param destinationDirectory the directory where all file have to be saved in
     * @param dbName  String representing the name of the database
     * @return 
     */
    public DatabaseType extractConfigurationFromDatabase(ExportTableModel tableContent,
                                                         PostgresCommunication pgc,
                                                         LogArea log,
                                                         String sqlConstraint,
                                                         String sqlTypes,
                                                         HashMap<String, String> sqlStatements,
                                                         String destinationDirectory,
                                                         String dbName) {
        DatabaseType dbt = new DatabaseType();
        dbt.setName(dbName);
        
        int rowCount = tableContent.getRowCount();
        
        for(int i = 0; i < rowCount; i++) {
            if((Boolean) tableContent.getValueAt(i, 2)) {
                TableType tt = new TableType();

                //add primary keys
                List<String> pk = pgc.selectColumnsOfConstraint((String) tableContent.getValueAt(i, 0),
                        "PRIMARY KEY", sqlConstraint, log);
                PrimaryKeysType pkt = new PrimaryKeysType();
                for(String entry : pk) {
                    PrimaryKeyType primaryKey = new PrimaryKeyType();
                    primaryKey.setColumn(entry);
                    pkt.getPrimaryKey().add(primaryKey);
                }
                tt.setPrimaryKeys(pkt);
                
                //add foreign keys
                List<String> fk = pgc.selectColumnsOfConstraint((String) tableContent.getValueAt(i, 0),
                        "FOREIGN KEY", sqlConstraint, log);
                ForeignKeysType fkt = new ForeignKeysType();
                for(String entry : fk) {
                    ForeignKeyType foreignKey = new ForeignKeyType();
                    foreignKey.setColumn(entry);
                    fkt.getContent().add(foreignKey);
                }
                tt.setForeignKeys(fkt);
                
                //add the table name
                String tableName = (String) tableContent.getValueAt(i, 0);
                tt.setName(tableName);
                
                //add the sql statement
                if(sqlStatements.containsKey(tableName)) {
                    tt.setSql(sqlStatements.get(tableName));
                } else {
                    log.log(LogArea.WARNING, "sql-statement for table " + tableName + " cannot saved", null);
                    log.log(LogArea.ERROR, "Export cancelled", null);
                }
                
                //add the export path
                tt.setPath(destinationDirectory + File.separator + tableName + ".csv");
                
                //add all columns with their name and data type
                List<String> columnsNamesTypes = pgc.selectColumnNamesTypesOfTable(
                        sqlStatements.get(tableName), log);
                ColumnsType ct = new ColumnsType();
                for(String entry : columnsNamesTypes) {
                    ColType column = new ColType();
                    column.setName(entry.split(";")[0]);
                    column.setType(entry.split(";")[1]);
                    ct.getCol().add(column);
                }
                tt.setColumns(ct);
                
                //add the current table to the root element
                dbt.getTable().add(tt);
            }
        }
        return dbt;
    }
    
}

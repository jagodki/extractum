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
 *
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
    
    public void initExportTableFromConfigFile (String path,
                                               LogArea log,
                                               ExportTableModel exportTable,
                                               JProgressBar pb) {
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
            
            //add the new line  the list of the table content
            li.add(tableContent);
            
            pb.setValue(pb.getValue() + 1);
        }
        exportTable.setLi(li);
    }
    
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
    
    public void importData(String schemaTemplate,
                           String tableTemplate,
                           String importTemplate,
                           LogArea log,
                           JProgressBar mainPb,
                           JProgressBar secondPb,
                           PostgresCommunication pgc,
                           ImportTableModel itm,
                           String path) {
        //create the new view for importing the data
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

    public DatabaseType getDbt() {
        return this.dbt;
    }
    
    public void setDbt(DatabaseType dbt) {
        this.dbt = dbt;
    }
}

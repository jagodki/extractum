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
package extractum;

import Utilities.LogArea;
import database.Database;
import database.PostgresCommunication;
import exporting.ExportHandler;
import exporting.ExportTableContent;
import exporting.ExportTableModel;
import extractumXml.DatabaseType;
import importing.ImportHandler;
import importing.ImportTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christoph
 */
public class ExtractumController {

    private final Database db;
    private final PostgresCommunication pgc;
    private final LogArea log;
    private final HashMap<String, String> importSql;
    private final HashMap<String, String> exportSql;
    private final String pathSchemaTemplate = "src/sqlTemplates/schema.sql";
    private final String pathTableTemplate = "src/sqlTemplates/create.sql";
    private final String pathImportTemplate = "src/sqlTemplates/insert.sql";
    private final String pathSchemataTemplate = "src/sqlTemplates/schemata.sql";
    private final String pathTablesTemplate = "src/sqlTemplates/tables.sql";
    private final String pathConstraintTemplate = "src/sqlTemplates/constraint.sql";
    private final String patDataTypesTemplate = "src/sqlTemplates/datatypes.sql";
    
    public ExtractumController() {
        this.exportSql = new HashMap<>();
        this.importSql = new HashMap<>();
        this.db = new Database();
        this.pgc = new PostgresCommunication();
        this.log = new LogArea(null, System.getProperty("user.dir"));
    }
    
    public ExtractumController(JTextArea ta) {
        this.exportSql = new HashMap<>();
        this.importSql = new HashMap<>();
        this.db = new Database();
        this.pgc = new PostgresCommunication();
        this.log = new LogArea(ta, System.getProperty("user.dir"));
    }
    
    public LogArea getLog() {
        return log;
    }
    
    public Database getDb() {
        return this.db;
    }
    
    /**
     * This function parses the content of the given text file from classpath,
     * i.e. from inside of the current JAR.
     * @param name the path of the ressource file as String
     * @return the content of the file or an empty String if somethin went wrong
     */
    public String getSqlTemplate(String name) {
        String result = ""; 
        try {           
            List<String> allLines = Files.readAllLines(Paths.get(name));
            for(String singleLine : allLines) {
                result += singleLine + " ";
            }
        } catch (IOException ex) {
            this.log.log(LogArea.ERROR, "cannot read SQL-template file " + name, ex);
        }
        return result.trim();
    }
    
    public boolean importData(JProgressBar pbMajor,
                              JProgressBar pbMinor,
                              String nameOfConfigFile,
                              String directoryOfConfigFile,
                              ImportTableModel itm) {
        this.log.log(LogArea.INFO, "start of import data", null);
        
        //create a new ImportHandler object and load the config file
        ImportHandler ih = new ImportHandler();
        ih.setDbt(ih.loadConfigFile(directoryOfConfigFile + File.separator + nameOfConfigFile, this.log));
        this.log.log(LogArea.INFO, "load config file finished", null);
        
        //now import the data using ImportHandler
        ih.importData(this.getSqlTemplate(this.pathSchemaTemplate),
                      this.getSqlTemplate(this.pathTableTemplate),
                      this.getSqlTemplate(this.pathImportTemplate),
                      this.log, pbMajor, pbMinor, pgc, itm, directoryOfConfigFile);
        
        this.log.log(LogArea.INFO, "finished import data", null);
        return true;
    }
    
    public boolean exportTables(JProgressBar pbMajor,
                             JProgressBar pbMinor,
                             String configPath,
                             String dataDirectory,
                             ExportTableModel etm,
                             String dbName) {
        this.log.log(LogArea.INFO, "start export of data", null);
        pbMajor.setValue(0);
        
        //create a new ExportHandler object and read in sql templates
        ExportHandler eh = new ExportHandler();
        String constraintTemplate = this.getSqlTemplate(this.pathConstraintTemplate);
        String typesTemplate = this.getSqlTemplate(this.patDataTypesTemplate);
        
        //create a new JAXB root element
        DatabaseType xmlRootElement = eh.extractConfigurationFromDatabase(etm,
                                            this.pgc,
                                            this.log,
                                            constraintTemplate,
                                            typesTemplate,
                                            this.exportSql,
                                            "data",
                                            dbName);
        
        //export config file
        eh.exportToXml(configPath,
                       xmlRootElement,
                       this.log, etm,
                       this.pgc,
                       constraintTemplate,
                       typesTemplate,
                       this.exportSql,
                       this.db.getDatabase());
        
        //export data from database
        eh.exportToCSV(xmlRootElement, this.pgc, pbMajor, pbMinor, this.log, dataDirectory);
        
        this.log.log(LogArea.INFO, "export finished ^o^", null);
        return true;
    }
    
    public String initTable(ImportTableModel itm,
                          ExportTableModel etm,
                          String path,
                          JProgressBar pb) {
        //create a new ImportHandler object to get access to import-functions
        ImportHandler ih = new ImportHandler();
        
        //now init the table(s)
        if(itm != null) {
            ih.initImportTableFromConfigFile(path, this.log, itm, pb, this.importSql);
            itm.fireTableDataChanged();
            return ih.getDbt().getName();
        } else if(etm != null) {
            ih.initExportTableFromConfigFile(path, this.log, etm, pb);
            etm.fireTableDataChanged();
            return ih.getDbt().getName();
        }
        return "---";
    }
    
    /**
     * This function returns the SQL-command to a given import-table.
     * @param table the name of the table
     * @return the corresponding sql-command
     */
    public String getImportSql(String table) {
        return this.importSql.get(table);
    }
    
    /**
     * This function returns the SQL-command to a given export-table.
     * @param table the name of the table
     * @return the corresponding sql-command
     */
    public String getExportSql(String table) {
        return this.exportSql.get(table);
    }
    
    public void setExportSql(String key, String value) {
        this.exportSql.put(key, value);
    }
    
    public void insertSchemataIntoExportTable(DefaultTableModel tm) {
        //delete all old entries
        int rows = tm.getRowCount();
        for(int i = 0; i < rows; i++) {
            tm.removeRow(0);
        }
        
        //query the names of all schemata
        String query = this.getSqlTemplate(this.pathSchemataTemplate);
        List<String> schemata = this.pgc.selectData(query, this.log);
        
        //iterate over the result and fill the table
        int schemataSize = schemata.size();
        for(int i = 0; i < schemataSize; i++) {
            tm.addRow(new String[] {schemata.get(i)});
            tm.fireTableDataChanged();
        }
    }
    
    public void insertTablesIntoExportTable(String schemaName,
                                            ExportTableModel etm) {
        //get all tables by their name
        String query = this.getSqlTemplate(this.pathTablesTemplate);
        List<String> tables = this.pgc.selectTablesOfSchema(schemaName, query, this.log);
        
        //prepare the iteration
        List<String> primaryKeys = new ArrayList<>();
        query = this.getSqlTemplate(this.pathConstraintTemplate);
        
        for(String table : tables) {
            //get the primary keys
            List<String> pkList = this.pgc.selectColumnsOfConstraint(table, "PRIMARY KEY", query, this.log);
            String pkConcated = "";
            for(int i = 0; i < pkList.size(); i++) {
                pkConcated += pkList.get(i);
            }
            primaryKeys.add(pkConcated);
            
            //fill up the export-HashMap for storing the sql-commands
            this.exportSql.put(table, "select * from " + schemaName + "." + table + ";");
        }
        
        //insert all data into the ExportTableModel
        List<ExportTableContent> li = new ArrayList<>();
        for(int i = 0; i < tables.size(); i++) {
            ExportTableContent etc = new ExportTableContent();
            etc.setTableName(tables.get(i));
            etc.setPrimaryKey(primaryKeys.get(i));
            etc.setExportTable(false);
            li.add(etc);
        }
        etm.setLi(li);
        etm.fireTableDataChanged();
    }
    
    public void selectAll(int tabIndex, ImportTableModel itm, ExportTableModel etm) {
        if(tabIndex == 0) {
            int maxRow = itm.getRowCount();
            for(int i = 0; i < maxRow; i++) {
                itm.getRow(i).setImportTable(true);
            }
            itm.fireTableDataChanged();
        } else {
            int maxRow = etm.getRowCount();
            for(int i = 0; i < maxRow; i++) {
                etm.getRow(i).setExportTable(true);
            }
            etm.fireTableDataChanged();
        }
    }
    
    public void unselectAll(int tabIndex, ImportTableModel itm, ExportTableModel etm) {
        if(tabIndex == 0) {
            int maxRow = itm.getRowCount();
            for(int i = 0; i < maxRow; i++) {
                itm.getRow(i).setImportTable(false);
            }
            itm.fireTableDataChanged();
        } else {
            int maxRow = etm.getRowCount();
            for(int i = 0; i < maxRow; i++) {
                etm.getRow(i).setExportTable(false);
            }
            etm.fireTableDataChanged();
        }
    }
    
    public boolean connectToDatabase() {
        if(this.db.hasDatabaseConnection()) {
            this.db.close(this.log);
        }
        boolean result = this.db.connectToPostgresDatabase(this.log);
        this.pgc.setDb(this.db);
        return result;
    }
    
}

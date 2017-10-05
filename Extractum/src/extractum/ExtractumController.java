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
import com.sun.javafx.scene.control.skin.VirtualFlow;
import database.Database;
import database.PostgresCommunication;
import exporting.ExportTableContent;
import exporting.ExportTableModel;
import importing.ImportHandler;
import importing.ImportTableModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christoph
 */
public class ExtractumController {

    private final HashMap<String, String> importSql;
    private final HashMap<String, String> exportSql;
    private final String pathSchemaTemplate = "src/sqlTemplates/schema.sql";
    private final String pathTableTemplate = "src/sqlTemplates/create.sql";
    private final String pathImportTemplate = "src/sqlTemplates/insert.sql";
    private final String pathSchemataTemplate = "src/sqlTemplates/schemata.sql";
    private final String pathTablesTemplate = "src/sqlTemplates/tables.sql";
    private final String pathConstraintTemplate = "src/sqlTemplates/constraint.sql";
    
    public ExtractumController() {
        this.exportSql = new HashMap<>();
        this.importSql = new HashMap<>();
    }
    
    /**
     * This function parses the content of the given text file from classpath,
     * i.e. from inside of the current JAR.
     * @param name the path of the ressource file as String
     * @param log a log-object for displaying information to the user
     * @return the content of the file or an empty String if somethin went wrong
     */
    public String getSqlTemplate(String name, LogArea log) {
        String result = ""; 
        try {           
            List<String> allLines = Files.readAllLines(Paths.get(name));
            for(String singleLine : allLines) {
                result += singleLine + " ";
            }
        } catch (IOException ex) {
            log.log(LogArea.ERROR, "cannot read SQL-template file " + name, ex);
        }
        return result.trim();
    }
    
    public boolean importData(LogArea log,
                           JProgressBar pbMajor,
                           JProgressBar pbMinor,
                           String pathOfConfigurationFile,
                           String directoryOfData,
                           String host,
                           String port,
                           String user,
                           String pw,
                           ImportTableModel itm) {
        log.log(LogArea.INFO, "start of import data", null);
        
        //create a new ImportHandler object and load the config file
        ImportHandler ih = new ImportHandler();
        ih.loadConfigFile(pathOfConfigurationFile, log);
        log.log(LogArea.INFO, "load config file finished", null);
        
        //init the connection to the database server
        Database db = new Database(host, port, ih.getDbt().getName(), user, pw);
        boolean connectionToDb = db.connectToPostgresDatabase(log);
        if(!connectionToDb) {
            log.log(LogArea.WARNING, "import of data cancelled", null);
            return false;
        }
        PostgresCommunication pgc = new PostgresCommunication(db);
        
        //now import the data using ImportHandler
        ih.importData(this.getSqlTemplate(this.pathSchemaTemplate, log),
                      this.getSqlTemplate(this.pathTableTemplate, log),
                      this.getSqlTemplate(this.pathImportTemplate, log),
                      log, pbMajor, pbMinor, pgc, itm, directoryOfData);
        
        log.log(LogArea.INFO, "finished import data", null);
        db.close(log);
        return true;
    }
    
    public String initTable(ImportTableModel itm,
                          ExportTableModel etm,
                          String path,
                          LogArea log,
                          JProgressBar pb) {
        //create a new ImportHandler object to get access to import-functions
        ImportHandler ih = new ImportHandler();
        
        //now init the table(s)
        if(itm != null) {
            ih.initImportTableFromConfigFile(path, log, itm, pb, this.importSql);
            itm.fireTableDataChanged();
            return ih.getDbt().getName();
        } else if(etm != null) {
            ih.initExportTableFromConfigFile(path, log, etm, pb);
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
    
    public void insertSchemataIntoExportTable(String host,
                                             String port,
                                             String db,
                                             String user,
                                             String pw,
                                             DefaultTableModel tm,
                                             LogArea log) {
        //get connection to database
        Database database = new Database(host, port, db, user, pw);
        database.connectToPostgresDatabase(log);
        PostgresCommunication pgc = new PostgresCommunication(database);
        
        //query the names of all schemata
        String query = this.getSqlTemplate(this.pathSchemataTemplate, log);
        List<String> schemata = pgc.selectData(query, log);
        
        //iterate over the result and fill the table
        int schemataSize = schemata.size();
        for(int i = 0; i < schemataSize; i++) {
            tm.addRow(new String[] {schemata.get(i)});
            tm.fireTableDataChanged();
        }
        database.close(log);
    }
    
    public void insertTablesIntoExportTable(String schemaName,
                                            String host,
                                            String port,
                                            String db,
                                            String user,
                                            String pw,
                                            ExportTableModel etm,
                                            LogArea log) {
        //get connection to database
        Database database = new Database(host, port, db, user, pw);
        database.connectToPostgresDatabase(log);
        PostgresCommunication pgc = new PostgresCommunication(database);
        
        //get all tables by their name
        String query = this.getSqlTemplate(this.pathTablesTemplate, log);
        List<String> tables = pgc.selectTablesOfSchema(schemaName, query, log);
        
        //prepare the iteration
        List<String> primaryKeys = new ArrayList<>();
        query = this.getSqlTemplate(this.pathConstraintTemplate, log);
        
        for(String table : tables) {
            //get the primary keys
            List<String> pkList = pgc.selectColumnsOfConstraint(table, "PRIMARY KEY", query, log);
            String pkConcated = "";
            for(int i = 0; i < pkList.size(); i++) {
                pkConcated += pkList.get(i);
            }
            primaryKeys.add(pkConcated);
            
            //fill up the export-HashMap for storing the sql-commands
            this.exportSql.put(table, "select * from " + table + ";");
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
        database.close(log);
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
    
}

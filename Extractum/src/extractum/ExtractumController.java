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
import exporting.ExportTableModel;
import importing.ImportHandler;
import importing.ImportTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.swing.JProgressBar;

/**
 *
 * @author Christoph
 */
public class ExtractumController {

    private HashMap<String, String> importSql;
    private HashMap<String, String> exportSql;
    private final String pathSchemaTemplate = "/Extractum/sqlTemplates/schema.sql";
    private final String pathTableTemplate = "/Extractum/sqlTemplates/create.sql";
    private final String pathImportTemplate = "/Extractum/sqlTemplates/insert.sql";
    
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
        
        try (InputStream is = getClass().getResourceAsStream(name);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            
        } catch (IOException ex) {
            log.log(LogArea.ERROR, "cannot read the SQL-template " + name, ex);
        }
        
        return result;
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
    
}

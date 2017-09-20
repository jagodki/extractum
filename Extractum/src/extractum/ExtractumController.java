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
import extractumXml.DatabaseType;
import importing.ImportHandler;
import importing.ImportTableModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JProgressBar;

/**
 *
 * @author Christoph
 */
public class ExtractumController {

    public ExtractumController() {
    }
    
    public String getSqlTemplate(String name, LogArea log) {
        String result = "";
        
        try {
            List<String> content = Files.readAllLines(Paths.get(name));
            result = content.stream().map((line) -> line).reduce(result, String::concat);
        } catch (IOException ex) {
            log.log(LogArea.ERROR, "cannot import SQL-template", ex);
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
        log.log(LogArea.INFO, "start of importing data", null);
        
        //create a new ImportHandler object and load the config file
        ImportHandler ih = new ImportHandler();
        ih.loadConfigFile(pathOfConfigurationFile, log);
        log.log(LogArea.INFO, "load config file finished", null);
        
        //init the connection to the database server
        Database db = new Database(host, port, ih.getDbt().getName(), user, pw);
        boolean connectionToDb = db.connectToPostgresDatabase(log);
        if(!connectionToDb) {
            log.log(LogArea.WARNING, "importing data cancelled", null);
            return false;
        }
        PostgresCommunication pgc = new PostgresCommunication(db);
        
        //now import the data using ImportHandler
        ih.importData(port, port, port, log, pbMajor, pbMinor, pgc, itm, directoryOfData);
        
        log.log(LogArea.INFO, "finished importing data", null);
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
            ih.initImportTableFromConfigFile(path, log, itm, pb);
            itm.fireTableDataChanged();
            return ih.getDbt().getName();
        } else if(etm != null) {
            ih.initExportTableFromConfigFile(path, log, etm, pb);
            etm.fireTableDataChanged();
            return ih.getDbt().getName();
        }
        return "---";
    }
    
}

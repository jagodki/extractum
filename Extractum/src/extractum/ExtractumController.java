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
import exporting.ExportTableModel;
import extractumXml.DatabaseType;
import importing.ImportHandler;
import importing.ImportTableModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.table.TableModel;

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
    
    public void importData(LogArea log,
                           JProgressBar pbMajor,
                           JProgressBar pbMinor,
                           String pathOfConfiguration) {
        //create a new ImportHandler object to get access to import-functions
        ImportHandler ih = new ImportHandler();
        
        //parse the configuration file into the JAXB-classes
        
        
        
    }
    
    public void initTable(ImportTableModel itm,
                          ExportTableModel etm,
                          //DatabaseType dbt,
                          String path,
                          LogArea log,
                          JProgressBar pb) {
        //create a new ImportHandler object to get access to import-functions
        ImportHandler ih = new ImportHandler();
        
        //now init the table(s)
        if(itm == null) {
            ih.initImportTableFromConfigFile(null, path, log, itm, pb);
        } else if(etm == null) {
            ih.initExportTableFromConfigFile(null, path, log, etm, pb);
        }
    }
    
}

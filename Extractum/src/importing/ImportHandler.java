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
import extractumXml.DatabaseType;
import extractumXml.ForeignKeyType;
import extractumXml.PrimaryKeyType;
import extractumXml.TableType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JProgressBar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Christoph
 */
public class ImportHandler {

    /**
     * The constructor of this class.
     */
    public ImportHandler() {
    }
    
    private DatabaseType loadConfigFile(String path, LogArea log) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DatabaseType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (DatabaseType) jaxbUnmarshaller.unmarshal(new File(path));
        } catch (JAXBException ex) {
            log.log(LogArea.ERROR, "import of config file was not successfull", ex);
            return null;
        }
    }
    
    public void initImportTableFromConfigFile(DatabaseType dbt, String path, LogArea log, ImportTableModel importTable, JProgressBar pb) {
        //import the config file
        if(dbt == null) {
            dbt = this.loadConfigFile(path, log);
        }
        
        //preparation of the iteration
        List<TableType> tables = dbt.getTable();
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
            List<ForeignKeyType> fkt = table.getForeignKeys().getForeignKey();
            for(ForeignKeyType fk : fkt) {
                foreignKeys += fk.getColumn();
            }
            tableContent.setForeignKey(foreignKeys);
            
            //add the new line  the list of the table content
            li.add(tableContent);
            
            pb.setValue(pb.getValue() + 1);
        }
        importTable.setLi(li);
    }
    
    public void initExportTableFromConfigFile () {
        
    }
    
}

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
import extractum.Extractum;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
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
    public boolean exportToCsv(String path, List<String> datasets, LogArea log, String columnNames, JProgressBar pb) {
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
    
    public boolean exportToXml(String path, DatabaseType rootObject, LogArea log) {
        String absolutePath = path + File.pathSeparator + "config.xml";
        
        //check, whether a file with the specified path exists already and delete it if necessary
        boolean deleteFile = this.deleteExistingFile(absolutePath, log);
        if(!deleteFile) {
            return false;
        }
        
        File outputFile = new File(new File(absolutePath).getAbsolutePath());
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
    
    public DatabaseType extractConfigurationFromDatabase(ExportTableModel tableContent, PostgresCommunication pgc, LogArea log, Extractum ex) {
        DatabaseType dbt = new DatabaseType();
        
        int rowCount = tableContent.getRowCount();
        int columnCount = tableContent.getColumnCount();
        
        for(int i = 0; i < rowCount; i++) {
            TableType tt = new TableType();
            
            List<String> pk = pgc.selectColumnsOfConstraint(tableContent.getValueAt(i, 0).toString(),
                    "PRIMARY KEY", ex.getSqlTemplate("constraint.sql", log), log);
            
            tt.setName(tableContent.getValueAt(i, 0).toString());
        }
        
    }
    
}

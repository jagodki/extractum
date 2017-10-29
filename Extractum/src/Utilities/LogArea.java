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
package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * 
 * @author Christoph
 */
public class LogArea {
    
    private final JTextArea log;
    public final static String INFO = "INFO";
    public final static String WARNING = "WARNING";
    public final static String ERROR = "ERROR";
    private final String logPath;

    public LogArea(JTextArea log, String path) {
        this.log = log;
        
        this.logPath = path + "/log/" + "extractum_" + new Date().toString() + ".log";
        File logDirectory = new File(path + "/log");
        File logFile = new File(this.logPath);
        
        if(!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        
        try {
            logFile.createNewFile();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Cannot create log file.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void writeLog(String logContent) {
        log.append(logContent + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
    
    public void log(String messageType, String message, Exception ex) {
        switch(messageType) {
            case "INFO":
                String infoMessage = "[INFO]: " + new Date().toString() + " - " + message;
                this.writeLog(infoMessage);
                this.writeIntoLogFile(infoMessage);
                break;
            case "WARNING":
                String warningMessage = "[WARNING]: " + new Date().toString() + " - " + message;
                this.writeLog(warningMessage);
                this.writeIntoLogFile(warningMessage);
                break;
            case "ERROR":
                String errorMessage = "[ERROR]: " + new Date().toString() + " - " + message;
                
                this.writeLog(errorMessage);
                this.writeLog(ex.toString());
                StackTraceElement[] exStackTrace = ex.getStackTrace();
                for(StackTraceElement singleStackTrace : exStackTrace) {
                    this.writeLog(singleStackTrace.toString());
                }
                
                this.writeIntoLogFile(errorMessage);
                this.writeIntoLogFile(ex.toString());
                for(StackTraceElement singleStackTrace : exStackTrace) {
                    this.writeIntoLogFile(singleStackTrace.toString());
                }
                break;
        }
    }
    
    private void writeIntoLogFile(String message) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(this.logPath, true))) {
            bw.write(message + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Cannot write into log file.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

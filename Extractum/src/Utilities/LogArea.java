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

import java.util.Date;
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

    public LogArea(JTextArea log) {
        this.log = log;
    }
    
    private void writeLog(String logContent) {
        log.append(logContent + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
    
    public void log(String messageType, String message, Exception ex) {
        switch(messageType) {
            case "INFO":
                this.writeLog("[INFO]: " + new Date().toString() + " - " + message);
                break;
            case "WARNING":
                this.writeLog("[WARNING]: " + new Date().toString() + " - " + message);
                break;
            case "ERROR":
                this.writeLog("[ERROR]: " + new Date().toString() + " - " + message);
                this.writeLog(ex.toString());
                StackTraceElement[] exStackTrace = ex.getStackTrace();
                for(StackTraceElement singleStackTrace : exStackTrace) {
                    this.writeLog(singleStackTrace.toString());
                }
                break;
        }
    }
    
}

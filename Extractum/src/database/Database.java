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
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Christoph
 */
public class Database {
    
    private String host;
    private String port;
    private String database;
    private String user;
    private String pw;
    private Connection connection;

    public Database() {
    }

    public Database(String host, String port, String database, String user, String pw, Connection connection) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pw = pw;
        this.connection = connection;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Connection getConnection() {
        return connection;
    }
    
    public boolean connectToDatabase() {
        //check, whether the postgres driver is availlable
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                            + "Include in your library path!");
            e.printStackTrace();
            return false;
        }
        
        //connect to the database
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + this.host + ":" + this.port, this.user, this.pw);
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return false;
        }
        
        return true;
    }
    
}

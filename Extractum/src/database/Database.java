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

import Utilities.LogArea;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides all information for the connection to a database.
 * This class is just a connector and is used by other classes, to realise
 * a communication with the databse.
 * @author Christoph
 */
public class Database {
    
    private String host;
    private String port;
    private String database;
    private String user;
    private String pw;
    private Connection connection = null;

    /**
     * The empty construtor of this class.
     */
    public Database() {
    }

    /**
     * The constructor of this class.
     * @param host the url of the host as String
     * @param port the port of the database server as String
     * @param database the name of the database as String
     * @param user the user name of the database user as String
     * @param pw the password of the user as String
     */
    public Database(String host, String port, String database, String user, String pw) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pw = pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
    
    /**
     * This function returns the url of the host of the database server.
     * @return the information about the host as String object
     */
    public String getHost() {
        return host;
    }

    /**
     * This function sets a new host.
     * @param host the information about the host as String object
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * This function returns the port of the database server.
     * @return the port of the server as String object
     */
    public String getPort() {
        return port;
    }

    /**
     * This function sets the port of the database server.
     * @param port the port of the server as String object
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * This function returns the name of the database.
     * @return the name of the database as String object
     */
    public String getDatabase() {
        return database;
    }

    /**
     * This function sets the name of the database.
     * @param database the name of the database as String object
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * This function returns the user name.
     * @return the name of the user as String object
     */
    public String getUser() {
        return user;
    }

    /**
     * This function sets the name of the user.
     * @param user the user name as String object
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * This function returns the connection object of the database object.
     * @return the connection object
     */
    protected Connection getConnection() {
        return connection;
    }
    
    /**
     * This function try the connection with a database represented by the
     * member variable of the current object.
     * @param log an object for logging and displaying information to the user
     * @return true whether the connection was successfull, otherwise false
     */
    public boolean connectToPostgresDatabase(LogArea log) {
        //check, whether the postgres driver is availlable
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.log(LogArea.ERROR, "cannot find PostgreSQL JDBC driver", e);
            return false;
        }
        
        //connect to the database
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.pw);
        } catch (SQLException e) {
            log.log(LogArea.ERROR, "connection to database failed", e);
            return false;
        }
        
        log.log(LogArea.INFO, "successfully connected to database", null);
        return true;
    }
    
    public void close(LogArea log) {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            log.log(LogArea.ERROR, "cannot close the database connection", ex);
        }
    }
    
}

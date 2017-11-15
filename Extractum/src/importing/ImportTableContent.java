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

/**
 * This class represents the content of a single row of an import table.
 * @author Christoph
 */
public class ImportTableContent {
    
    private String tableName;
    private String primaryKey;
    private String foreignKey;
    private String path;
    private boolean importTable;

    /**
     * The epmty constructor.
     */
    public ImportTableContent() {
    }

    /**
     * The constructor of this class.
     * @param tableName the value of the column <code>tableName</code>
     * @param primaryKey the value of the column <code>primaryKey</code>
     * @param foreignKey the value of the column <code>foreignKey</code>
     * @param path the value of the column <code>path</code>
     * @param importTable the value of the column <code>importTable</code>
     */
    public ImportTableContent(String tableName, String primaryKey, String foreignKey, String path, boolean importTable) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.path = path;
        this.importTable = importTable;
    }

    /**
     * This function returns the value of the column <code>tableName</code>.
     * @return a String representing the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This function sets a new value for the column <code>tableName</code>.
     * @param tableName a String representing the new table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * This function returns the value of the column <code>primaryKey</code>.
     * @return a String representing the name of the primary key
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * This function sets a new value for the column <code>primaryKey</code>.
     * @param primaryKey a String representing the name of the new primary key
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * This function returns the value of the column <code>foreignKey</code>.
     * @return a String representing the name of the foreign key(s)
     */
    public String getForeignKey() {
        return foreignKey;
    }

    /**
     * This function sets a new value for the column <code>foreignKey</code>.
     * @param foreignKey a String representing the name of the new foreign key(s)
     */
    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    /**
     * This function returns the value of the column <code>path</code>.
     * @return a String representing the path
     */
    public String getPath() {
        return path;
    }

    /**
     * This function sets a new value for the column <code>path</code>.
     * @param path a String representing the name of the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * This function returns the value of the column <code>importTable</code>.
     * @return a boolean whether the table can be imported or not
     */
    public boolean isImportTable() {
        return importTable;
    }

    /**
     * This function sets a new value for the column <code>importTable</code>.
     * @param importTable a boolean whether the table can be imported or not
     */
    public void setImportTable(boolean importTable) {
        this.importTable = importTable;
    }
    
}

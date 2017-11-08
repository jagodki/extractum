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

/**
 * This class represents the content of a single row of an export table.
 * @author Christoph
 */
public class ExportTableContent {
    
    private String tableName;
    private String primaryKey;
    private boolean exportTable;

    /**
     * The epmty constructor.
     */
    public ExportTableContent() {
    }

    /**
     * The constructor of this class.
     * @param tableName the value of the column <code>tableName</code>
     * @param primaryKey the value of the column <code>primaryKey</code>
     * @param exportTable the value of the column <code>exportTable</code>
     */
    public ExportTableContent(String tableName, String primaryKey, boolean exportTable) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.exportTable = exportTable;
    }

    /**
     * Returns the value of the column <code>tableName</code>.
     * @return a String presenting the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This function sets a new table name.
     * @param tableName a String representing the new table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the value of the column <code>primaryKey</code>.
     * @return a String representing the name of the primary key
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * This function sets a new name of the primary key.
     * @param primaryKey a String representing the name of the new primary key
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the value of the column <code>exporTTable</code>.
     * @return a boolean representing whether the table is exportable or not
     */
    public boolean isExportTable() {
        return exportTable;
    }

    /**
     * This function declares the current tupel as exportable or not.
     * @param exportTable a boolean representing whether the table is exportable or not
     */
    public void setExportTable(boolean exportTable) {
        this.exportTable = exportTable;
    }
    
}

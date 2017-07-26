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
 *
 * @author Christoph
 */
public class ImportTableContent {
    
    private String tableName;
    private String primaryKey;
    private String foreignKey;
    private String path;
    private boolean importTable;

    public ImportTableContent() {
    }

    public ImportTableContent(String tableName, String primaryKey, String foreignKey, String path, boolean importTable) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.path = path;
        this.importTable = importTable;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isImportTable() {
        return importTable;
    }

    public void setImportTable(boolean importTable) {
        this.importTable = importTable;
    }
    
}

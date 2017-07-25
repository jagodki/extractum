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
 *
 * @author Christoph
 */
public class ExportTableContent {
    
    private String tableName;
    private String primaryKey;
    private boolean exportTable;

    public ExportTableContent() {
    }

    public ExportTableContent(String tableName, String primaryKey, boolean exportTable) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.exportTable = exportTable;
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

    public boolean isExportTable() {
        return exportTable;
    }

    public void setExportTable(boolean exportTable) {
        this.exportTable = exportTable;
    }
    
}

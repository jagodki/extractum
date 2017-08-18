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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Christoph
 */
public class ImportTableModel extends AbstractTableModel {
    
    private List<ImportTableContent> li = new ArrayList<>();
    private final String[] columnNames = {"Table", "Primary Key", "Foreign Key", "Path", "Import?"};
    
    public void setLi(List<ImportTableContent> li) {
        this.li = li;
    }
    
    @Override
    public int getRowCount() {
        return li.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ImportTableContent tc = li.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return tc.getTableName();
            case 1:
                return tc.getPrimaryKey();
            case 2:
                return tc.getForeignKey();
            case 3:
                return tc.getPath();
            case 4:
                return tc.isImportTable();
        }
        return null;
    }
    
    @Override
    public String getColumnName(int columnIndex){
         return columnNames[columnIndex];
    }
    
}
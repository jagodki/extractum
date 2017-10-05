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
    private final Class[] columnClass = {String.class, String.class, String.class, String.class, Boolean.class};
    
    public void setLi(List<ImportTableContent> li) {
        this.li = li;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
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
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return (col == 4); 
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        ImportTableContent tc = this.li.get(row);
        switch(col) {
            case 0:
                tc.setTableName(((String) value));
                break;
            case 1:
                tc.setPrimaryKey((String) value);
                break;
            case 2:
                tc.setForeignKey((String) value);
                break;
            case 3:
                tc.setPath((String) value);
                break;
            case 4:
                tc.setImportTable((Boolean) value);
                break;
        }
    }
    
    public ImportTableContent getRow(int rowIndex) {
        return this.li.get(rowIndex);
    }
    
}

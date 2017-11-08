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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * This class represents the model of the export table.
 * @author Christoph
 */
public class ExportTableModel extends AbstractTableModel {
    
    private List<ExportTableContent> li = new ArrayList<>();
    private final String[] columnNames = {"Table", "Primary Key", "Export?"};
    private final Class[] columnClass = {String.class, String.class, Boolean.class};
    
    /**
     * The empty constructor.
     */
    public ExportTableModel() {
    }

    /**
     * This function replaces the current datasets with new ones.
     * @param li a List of ExportTableContent representing the whole data of the table
     */
    public void setLi(List<ExportTableContent> li) {
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
        ExportTableContent tc = li.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return tc.getTableName();
            case 1:
                return tc.getPrimaryKey();
            case 2:
                return tc.isExportTable();
        }
        return null;
    }
    
    @Override
    public String getColumnName(int columnIndex){
         return columnNames[columnIndex];
    }
    
    /**
     * This function returns true, whether a Cell is editable.
     * Only cells of column 2 are editable.
     * @param row zero based index of the row
     * @param col zero based index of the column
     * @return true whether the column index is 2
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return (col == 2); 
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        ExportTableContent tc = this.li.get(row);
        switch(col) {
            case 0:
                tc.setTableName((String) value);
                break;
            case 1:
                tc.setPrimaryKey((String) value);
                break;
            case 2:
                tc.setExportTable((Boolean) value);
                break;
        }
    }
    
    /**
     * This function returns a whole dataset depending of the index of the row.
     * @param rowIndex the zero based index of the needed row
     * @return a whole dataset/tupel as ExportTableContent
     */
    public ExportTableContent getRow(int rowIndex) {
        return this.li.get(rowIndex);
    }
    
}

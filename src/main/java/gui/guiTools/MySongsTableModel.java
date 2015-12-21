package gui.guiTools;

import javax.swing.table.AbstractTableModel;


public class MySongsTableModel extends AbstractTableModel {

    private String[] columnNames;
    private Object[][] data;

    public MySongsTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return columnNames.length;
    }

    @Override
    public int getColumnCount() {
        return data.length;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return Double.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            default:
                return String.class;
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
            switch (col) {
                case 0:
                    return false;
                case 1:
                    return false;
                case 2:
                    return false;
                case 3:
                    return false;
                case 4:
                    return true;
                case 5:
                    return true;
                default:
                    return false;
            }
    }
}

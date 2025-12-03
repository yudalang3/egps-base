package tsv.io;

import java.util.List;

/**
 * Data structure for storing integer-valued tables with row and column names.
 */
public class IntTable {


    private String colNameOfRowNames;
    private int[][] table;
    private String[] rowNames;
    private String[] colNames;


    public IntTable(int[][] table, String[] rowNames, String[] colNames) {
        this.table = table;
        this.rowNames = rowNames;
        this.colNames = colNames;
    }
    public IntTable(int[][] table, List<String> rowNames, List<String> colNames) {
        this.table = table;
        this.rowNames = rowNames.toArray(new String[rowNames.size()]);
        this.colNames = colNames.toArray(new String[colNames.size()]);
    }

    public int[][] getTable() {
        return table;
    }
    public String[] getRowNames() {
        return rowNames;
    }
    public String[] getColNames() {
        return colNames;
    }
    public int getValue(int row, int col) {
        return table[row][col];
    }

    public String getColNameOfRowNames() {
        return colNameOfRowNames;
    }

    public void setColNameOfRowNames(String colNameOfRowNames) {
        this.colNameOfRowNames = colNameOfRowNames;
    }
}
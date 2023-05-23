package com.database.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MonzyResultSetImpl implements MonzyResultSet {

    private Map<String, Object>[] data;
    private Object[][] values;
    private int indexData = -1;

    public MonzyResultSetImpl(ResultSet rs) throws SQLException {
        int nRow = 0;
        int nColumn = 0;
        ResultSetMetaData rsmd = null;
        try {
            rs.last();
            nRow = rs.getRow();
            rs.beforeFirst();
            rsmd = rs.getMetaData();
            nColumn = rsmd.getColumnCount();
        } catch (SQLException e) {
            throw e;
        }
        this.data = new HashMap[nRow];
        for (int i = 0; i < nRow; i++) {
            this.data[i] = new HashMap<>();
        }
        this.values = new Object[nRow][nColumn];
        int rowIndex = 0;
        while (rs.next()) {
            for (int i = 1; i <= nColumn; i++) {
                String tableName = rsmd.getTableName(i);
                String columnName = rsmd.getColumnName(i);
                Object columnValue = rs.getObject(i);
                this.data[rowIndex].put(columnName.toLowerCase(), columnValue);
                this.data[rowIndex].put(tableName.toLowerCase() + "." + columnName.toLowerCase(), columnValue);
                this.values[rowIndex][i - 1] = columnValue;
            }
            rowIndex++;
        }
        try {
            rs.getStatement().close();
            rs.close();
        } catch (SQLException e) {
            // ignore
        }
    }

    public void dispose() {
        for (Map<String, Object> map : this.data) {
            map.clear();
        }
        this.data = null;
        for (Object[] obj : this.values) {
            Arrays.fill(obj, null);
        }
        this.values = null;
    }

    public boolean next() throws Exception {
        checkDataAvailability();
        indexData++;
        return indexData < data.length;
    }

    public boolean first() throws Exception {
        checkDataAvailability();
        indexData++;
        return indexData == data.length - 1;
    }

    public boolean gotoResult(int index) throws Exception {
        checkDataAvailability();
        if (index >= 0 && index < data.length) {
            indexData = index;
            return true;
        } else {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
    }

    public boolean gotoFirst() throws Exception {
        checkDataAvailability();
        indexData = 0;
        return true;
    }

    public void gotoBeforeFirst() {
        indexData = -1;
    }

    public boolean gotoLast() throws Exception {
        checkDataAvailability();
        indexData = data.length - 1;
        return true;
    }

    public int getRows() throws Exception {
        checkDataAvailability();
        return data.length;
    }

    public byte getByte(int column) throws Exception {
        checkDataAvailability();
        return ((Integer) values[indexData][column - 1]).byteValue();
    }

    public byte getByte(String column) throws Exception {
        checkDataAvailability();
        return ((Integer) data[indexData].get(column.toLowerCase())).byteValue();
    }

    public int getInt(int column) throws Exception {
        checkDataAvailability();
        return ((Long) values[indexData][column - 1]).intValue();
    }

    public int getInt(String column) throws Exception {
        checkDataAvailability();
        return (Integer) data[indexData].get(column.toLowerCase());
    }

    public float getFloat(int column) throws Exception {
        checkDataAvailability();
        return (Float) values[indexData][column - 1];
    }

    public float getFloat(String column) throws Exception {
        checkDataAvailability();
        return (Float) data[indexData].get(column.toLowerCase());
    }

    public double getDouble(int column) throws Exception {
        checkDataAvailability();
        return (Double) values[indexData][column - 1];
    }

    public double getDouble(String column) throws Exception {
        checkDataAvailability();
        return (Double) data[indexData].get(column.toLowerCase());
    }

    public long getLong(int column) throws Exception {
        checkDataAvailability();
        return (Long) values[indexData][column - 1];
    }

    public long getLong(String column) throws Exception {
        checkDataAvailability();
        return (Long) data[indexData].get(column.toLowerCase());
    }

    public String getString(int column) throws Exception {
        checkDataAvailability();
        return String.valueOf(values[indexData][column - 1]);
    }

    public String getString(String column) throws Exception {
        checkDataAvailability();
        return String.valueOf(data[indexData].get(column.toLowerCase()));
    }

    public Object getObject(int column) throws Exception {
        checkDataAvailability();
        return values[indexData][column - 1];
    }

    public Object getObject(String column) throws Exception {
        checkDataAvailability();
        return data[indexData].get(column.toLowerCase());
    }

    public boolean getBoolean(int column) throws Exception {
        checkDataAvailability();
        try {
            return (Integer) values[indexData][column - 1] == 1;
        } catch (Exception e) {
            return (Boolean) values[indexData][column - 1];
        }
    }

    public boolean getBoolean(String column) throws Exception {
        checkDataAvailability();
        try {
            return (Integer) data[indexData].get(column.toLowerCase()) == 1;
        } catch (Exception e) {
            return (Boolean) data[indexData].get(column.toLowerCase());
        }
    }

    public Timestamp getTimestamp(int column) throws Exception {
        checkDataAvailability();
        return (Timestamp) values[indexData][column - 1];
    }

    public Timestamp getTimestamp(String column) throws Exception {
        checkDataAvailability();
        return (Timestamp) data[indexData].get(column.toLowerCase());
    }

    public short getShort(int column) throws Exception {
        checkDataAvailability();
        return ((Integer) values[indexData][column - 1]).shortValue();
    }

    public short getShort(String column) throws Exception {
        checkDataAvailability();
        return ((Integer) data[indexData].get(column.toLowerCase())).shortValue();
    }

    private void checkDataAvailability() throws Exception {
        if (data == null || data.length == 0) {
            throw new Exception("No data available");
        }
    }

}

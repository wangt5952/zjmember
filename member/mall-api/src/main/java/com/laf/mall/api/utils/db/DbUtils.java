package com.laf.mall.api.utils.db;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Component
public class DbUtils {
    public boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columns = resultSetMetaData.getColumnCount();

        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(resultSetMetaData.getColumnName(x))) {
                return true;
            }
        }

        return false;
    }

    public boolean hasColumn(SqlRowSet rowSet, String columnName) throws SQLException {
        SqlRowSetMetaData sqlRowSetMetaData = rowSet.getMetaData();
        int columns = sqlRowSetMetaData.getColumnCount();

        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(sqlRowSetMetaData.getColumnName(x))) {
                return true;
            }
        }

        return false;
    }
}

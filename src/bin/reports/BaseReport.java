package bin.reports;

import bin.sql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class BaseReport {
    protected static void GetQueryResult(String sql) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<ArrayList<Object>> results = resultSetToArrays(resultSet);

            for (ArrayList<Object> row : results) {
                for (Object item : row) {
                    System.out.println(item);
                }
            }
        }
    }

    private static ArrayList<ArrayList<Object>> resultSetToArrays(ResultSet resultSet) throws SQLException {

        ArrayList<ArrayList<Object>> list = new ArrayList<>();

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // column headers
        ArrayList<Object> headers = new ArrayList<>();
        for (int i = 1; i <= columnCount; ++i) {
            headers.add(rsmd.getColumnName(i));
        }
        list.add(headers);

        while(resultSet.next()) {
            ArrayList<Object> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; ++i) {
                row.add(resultSet.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
}

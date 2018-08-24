package bin.reports;

import bin.sql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseReport {
    protected static List<List<Object>> GetQueryResult(String sql) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<List<Object>> results = resultSetToArrays(resultSet);

            for (List<Object> row : results) {
                for (Object item : row) {
                    System.out.println(item);
                }
            }

            return results;
        }
    }

    private static List<List<Object>> resultSetToArrays(ResultSet resultSet) throws SQLException {

        List<List<Object>> list = new ArrayList<>();

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();

        // column headers
        List<Object> headers = new ArrayList<>();
        for (int i = 1; i <= columnCount; ++i) {
            headers.add(rsmd.getColumnName(i));
        }
        list.add(headers);

        while(resultSet.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; ++i) {
                row.add(resultSet.getObject(i));
            }
            list.add(row);
        }

        return list;
    }
}

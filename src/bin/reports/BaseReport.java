package bin.reports;

import bin.sql.DatabaseConnection;
import com.google.api.client.util.DateTime;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BaseReport {
    protected static List<List<Object>> GetQueryResult(String sql) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<List<Object>> results = resultSetToArrays(resultSet);
            System.out.println("fetched " + results.size() + " results");

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
                Object o = resultSet.getObject(i);
                if (o instanceof Timestamp)
                    row.add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(((Timestamp) o).toLocalDateTime()));
                else
                    row.add(o == null ? "" : o);
            }
            list.add(row);
        }

        return list;
    }
}

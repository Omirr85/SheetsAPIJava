package bin.sql;

import bin.xml.XMLNode;
import bin.xml.XMLParser;

import java.sql.*;

public class DatabaseConnection {
    public static final String environment = "Productie";
    public static final String connectionsPath = "/resources/connections.xml";

    public Connection getConnection()
    {
        XMLNode connectionInfo = XMLParser.Parse(connectionsPath);

        XMLNode dbInfo = connectionInfo.GetChildByAttributeValue("environment",environment);

        String url = dbInfo.GetChild("url").getContent();
        String user = dbInfo.GetChild("user").getContent();
        String password = dbInfo.GetChild("password").getContent();
        String name = dbInfo.GetChild("name").getContent();

        String connectionString = "jdbc:postgresql://" + url + "/" + name;


        try (Connection connection = DriverManager.getConnection(connectionString, user, password)) {
            System.out.println("Connected to PostgreSQL database!");
            return connection;

            /*
            Statement statement = connection.createStatement();
            System.out.println("Reading...");
            System.out.printf("%-30.30s  %-30.30s%n", "Model", "Price");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM onderdeel limit 10");

            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("naam_pad"), resultSet.getString("id"));
            }

            return connection;
            */

        } /*catch (ClassNotFoundException e) {
			System.out.println("PostgreSQL JDBC driver not found.");
			e.printStackTrace();
		}*/ catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
            return null;
        }
    }
}

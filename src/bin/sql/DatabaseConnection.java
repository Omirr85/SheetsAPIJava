package bin.sql;

import bin.xml.XMLNode;
import bin.xml.XMLParser;

import java.sql.*;

public class DatabaseConnection {
    public static final String environment = "Productie";
    public static final String connectionsPath = "/resources/connections.xml";

    public static Connection getConnection() throws SQLException {
        XMLNode connectionInfo = XMLParser.Parse(connectionsPath);

        XMLNode dbInfo = connectionInfo.GetChildByAttributeValue("environment", environment);

        String url = dbInfo.GetChild("url").getContent();
        String user = dbInfo.GetChild("user").getContent();
        String password = dbInfo.GetChild("password").getContent();
        String name = dbInfo.GetChild("name").getContent();

        String connectionString = "jdbc:postgresql://" + url + "/" + name;

        return DriverManager.getConnection(connectionString, user, password);
    }
}



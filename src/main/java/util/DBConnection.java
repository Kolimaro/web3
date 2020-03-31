package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection = null;

    private DBConnection() {
    }

    public static Connection getConnection() {

        if (connection == null) {
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("web_3_schemas?").       //db name
                    append("user=root&").           //login
                    append("password=root");        //password
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url.toString());
                System.out.println("URL: " + url + "\n");
                return connection;
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}

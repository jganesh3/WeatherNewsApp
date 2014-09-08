/*
* Weather & News App.
* CSCI 567 Project
* Author : Ganesh Joshi
*
*/

package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbutil {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        else {
            try {
                String driver = "com.mysql.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/weather";
                String user = "DBapp";
                String password = "abc";

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(url, user, password);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return connection;


        }


    }

}

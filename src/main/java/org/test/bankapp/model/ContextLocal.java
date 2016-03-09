package org.test.bankapp.model;

import java.sql.*;

/**
 * Created by stalker on 09.03.16.
 */
public class ContextLocal {
    public static Connection conn;

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn= openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection openConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:~/DEMO",
                "sa", // login
                "" // password
        );
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


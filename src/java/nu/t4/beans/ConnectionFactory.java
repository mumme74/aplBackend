/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlkonig
 */
public class ConnectionFactory {

    //Databas inloggning
    private static final String DATABASE_USER = "aplapp";
    private static final String DATABASE_PASS = "H0A%!i984t7e";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://apl.teknikum.it/aplapp";
        return (Connection) DriverManager
                .getConnection(url, DATABASE_USER, DATABASE_PASS);
    }

    public static Connection getConnection(String host) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url;
        if (host.equals("local")) {
            url = "jdbc:mysql://localhost/aplapp";
        } else {
            url = "jdbc:mysql://apl.teknikum.it/aplapp";
        }
        return (Connection) DriverManager
                .getConnection(url, DATABASE_USER, DATABASE_PASS);
    }
}

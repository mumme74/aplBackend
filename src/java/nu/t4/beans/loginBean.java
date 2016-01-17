package nu.t4.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mysql.jdbc.Connection;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Daniel Nilsson
 */
@ManagedBean
@ApplicationScoped
public class loginBean implements Serializable {

    private String username;

    private String password;
    
    private boolean loggedIn;
    
    public static Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://10.97.72.5/aplapp"; 
        String user = "aplapp";
        String password = "Teknikum123";
        Connection conn = (Connection) DriverManager.getConnection(url, user, password);
        return conn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM admin WHERE användarnamn = '%s'", username);
            ResultSet data = stmt.executeQuery(sql);
            
            data.next();//ta ut första raden
            String hashedpwd = data.getString("lösenord");
            if (BCrypt.checkpw(password, hashedpwd)) {
                conn.close();
                loggedIn = true;
                return "main"; //Om inloggningen stämmer skickas man till startsidan
            } else {
                conn.close();
                loggedIn = false; //Om inloggningen misslyckas stannar man kvar på index
                return "index";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            loggedIn = false;
            return "index";
        }
    }
    
    public String logout() {
        loggedIn = false;
        System.out.println("test 1337");
        return "index";
    }
}

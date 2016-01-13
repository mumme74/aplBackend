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
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/aplapp", "root", "");
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM adminuser WHERE username = '%s'", username);
            ResultSet data = stmt.executeQuery(sql);
            
            data.next();//ta ut första raden
            String hashedpwd = data.getString("password");
            if (BCrypt.checkpw(password, hashedpwd)) {
                conn.close();
                loggedIn = true;
                return "main";
            } else {
                conn.close();
                loggedIn = false;
                return "index";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            loggedIn = false;
            return "index";
        }
    }
}

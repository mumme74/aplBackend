package nu.t4.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mysql.jdbc.Connection;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Daniel Nilsson
 */
@ManagedBean
@SessionScoped
public class AdminManager  implements Serializable{
    private String klassnamn;

    public String getKlassnamn() {
        return klassnamn;
    }

    public void setKlassnamn(String klassnamn) {
        this.klassnamn = klassnamn;
    }

    public String addClass() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/aplapp", "root", "");
            Statement stmt = conn.createStatement();
            String sql = String.format("INSERT INTO klasser VALUES(NULL, '%s')", klassnamn);
            stmt.executeUpdate(sql);
            klassnamn = "";
            conn.close();
            System.out.println(sql);
            return "main";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "main";
        }
    }
}

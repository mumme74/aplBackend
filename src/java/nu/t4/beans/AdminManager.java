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
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Daniel Nilsson
 */
@ManagedBean
@ApplicationScoped
public class AdminManager implements Serializable {

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
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.97.72.5/aplapp", "aplapp", "Teknikum123");
            Statement stmt = conn.createStatement();
            if (!klassnamn.equals("")) {
                String sql = String.format("INSERT INTO klass VALUES(NULL, '%s')", klassnamn);
                stmt.executeUpdate(sql);
                System.out.println(sql);
            }
            klassnamn = "";
            conn.close();
            return "main";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "main";
        }
    }

    public List getClasses() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.97.72.5/aplapp", "aplapp", "Teknikum123");
            Statement stmt = conn.createStatement();
            String sql = "SELECT namn FROM klass";
            ResultSet data = stmt.executeQuery(sql);
            List classes = new ArrayList();
            while (data.next()) {
                classes.add(data.getString("namn"));
            }
            conn.close();
            return classes;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void removeClass(String namn) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://10.97.72.5/aplapp", "aplapp", "Teknikum123");
            Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM klasser WHERE namn ='%s'", namn);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

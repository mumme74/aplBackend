package nu.t4.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mysql.jdbc.Connection;
import java.io.Serializable;
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

    //Lägger till klassen i databasen
    public String addClass() {
        try {
            Connection conn = ConnectionFactory.getConnection();
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

    //Hämtar alla klasser
    public List getClasses() {
        try {
            Connection conn = ConnectionFactory.getConnection();
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

    //Tar bort klassen från listan med alla klasser
    public void removeClass(String namn) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM klass WHERE namn ='%s'", namn);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Hämtar alla användare som inte har lärarbehörighet
    public List getUsers(){
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT namn, email, behörighet FROM skolans_användare WHERE behörighet = 0";
            ResultSet data = stmt.executeQuery(sql);
            //List<Users> users = new ArrayList();
            //Users user = new Users();
            List users = new ArrayList();
            while (data.next()) {
                users.add(data.getObject("email"));
//                user.setNamn(data.getString("namn"));
//                user.setEmail(data.getString("email"));
//                user.setBehörighet("behörighet");
//                users.add(user);
            }
            conn.close();
            return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    //Sätter behörigheten som lärare mha deras email
    public void setBehörighet(String email){
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE skolans_användare SET behörighet = 1 WHERE email ='%s'", email);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Hämtar alla som har lärarbehörighet
    public List getLärare(){
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT namn, email FROM skolans_användare WHERE behörighet = 1";
            ResultSet data = stmt.executeQuery(sql);
            List lärare = new ArrayList();
            while (data.next()) {
                lärare.add(data.getObject("email"));
            }
            conn.close();
            return lärare;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    //Tar bort behörigheten som lärare mha deras email
    public void removeBehörighet(String email){
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE skolans_användare SET behörighet = 0 WHERE email ='%s'", email);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}

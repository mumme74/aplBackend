package nu.t4.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mysql.jdbc.Connection;
import java.io.Serializable;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private String programnamn;
    
    private String programIdNamn;
    
    private List filteredUsers;
    
    private List filteredLärare;

    public List getFilteredLärare() {
        return filteredLärare;
    }

    public void setFilteredLärare(List filteredLärare) {
        this.filteredLärare = filteredLärare;
    }

    public void setFilteredUsers(List filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public String getProgramIdNamn() {
        return programIdNamn;
    }

    public void setProgramIdNamn(String programIdNamn) {
        this.programIdNamn = programIdNamn;
    }

    public String getProgramnamn() {
        return programnamn;
    }

    public void setProgramnamn(String programnamn) {
        this.programnamn = programnamn;
    }

    public String getKlassnamn() {
        return klassnamn;
    }

    public void setKlassnamn(String klassnamn) {
        this.klassnamn = klassnamn;
    }
    
    public List getFilteredUsers() {
        return filteredUsers;
    }

    //Lägger till klassen i databasen
    public void addClass() {
        try {
            System.out.println(programIdNamn);
            int programId = 0;
            programId = getProgramId(programIdNamn);
            if (!klassnamn.equals("") && programId != 0) {
                Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                String sql = String.format("INSERT INTO klass VALUES(NULL, '%s', %d)", klassnamn, programId);
                stmt.executeUpdate(sql);
                conn.close();
            }
            klassnamn = "";
            programIdNamn = "Välj klass";
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Hämtar alla klasser
    public List getClasses() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM klass_program";
            ResultSet data = stmt.executeQuery(sql);
            List classes = new ArrayList();
            while (data.next()) {
                String temp = data.getString("programnamn") + ", " + data.getString("klassnamn");
                classes.add(temp);
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
            String tempArray[];
            tempArray = namn.split(", ");
            
            
            
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM klass WHERE namn ='%s'", tempArray[1]);
            System.out.println(sql);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Hämtar alla användare som inte har lärarbehörighet
    public List getUsers() {
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
    public void setBehörighet(String email) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE skolans_användare SET "
                    + "behörighet = 1, handledare_ID = NULL WHERE email ='%s'",
                    email);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Hämtar alla som har lärarbehörighet
    public List getLärare() {
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
    public void removeBehörighet(String email) {
        System.out.println("removing" + email);
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

    //Lägger till ett nytt program
    public void addProgram() {
        try {
            if (!programnamn.equals("")) {
                System.out.println(programnamn);
                programnamn = programnamn.trim();
                Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement();
                String sql = String.format("INSERT INTO program VALUES(NULL, '%s')", programnamn);
                stmt.executeUpdate(sql);
                conn.close();
            }
            programnamn = "";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    //Hämtar alla program
    public List getPrograms() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM program";
            ResultSet data = stmt.executeQuery(sql);
            List programs = new ArrayList();
            while (data.next()) {
                programs.add(data.getString("namn"));
            }
            conn.close();
            return programs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Tar bort programmet
    public void removeProgram(String namn) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM program WHERE namn ='%s'", namn);
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Hämtar id på inmatat program
    public int getProgramId(String namn) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT id FROM program WHERE namn = '%s'",namn);
            System.out.println(sql);
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            int programId = data.getInt("id");
            System.out.println(programId);
            conn.close();
            return programId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

}

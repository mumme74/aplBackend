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
import javax.xml.registry.infomodel.User;
import nu.t4.beans.Users;
import org.mindrot.jbcrypt.BCrypt;

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
    private List filteredHL;
    private List filteredAnv;
    private List filteredLärare;
    private Users selectedUser;
    private Users selectedHL;

    //Getters och setters start
    public List getFilteredHL() {
        return filteredHL;
    }

    public void setFilteredHL(List filteredHL) {
        this.filteredHL = filteredHL;
    }

    public List getFilteredAnv() {
        return filteredAnv;
    }

    public void setFilteredAnv(List filteredAnv) {
        this.filteredAnv = filteredAnv;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Users getSelectedHL() {
        return selectedHL;
    }

    public void setSelectedHL(Users selectedHL) {
        this.selectedHL = selectedHL;
    }

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

    //Getters och setters slut
    public String redigeraSkAnv(Users temp) {
        selectedUser = new Users();
        selectedUser = temp;
        return "redigeraSkAnv";
    }

    public String redigeraHL(Users temp) {
        selectedHL = new Users();
        selectedHL = temp;
        return "redigeraHL";
    }

    //Lägger till klassen i databasen
    public void addClass() {
        try {
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

    public List getHandledare() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT id, namn, företag FROM handledare";
            ResultSet data = stmt.executeQuery(sql);
            List<Users> handledare = new ArrayList();
            while (data.next()) {
                Users user = new Users();
                user.setId(data.getInt("id"));
                
                String namn_företag = data.getString("namn") + " - " + data.getString("företag");
                user.setNamn_företag(namn_företag);
                handledare.add(user);
            }
            conn.close();
            return handledare;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getProgram() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT id, namn FROM program";
            ResultSet data = stmt.executeQuery(sql);
            List<Users> programs = new ArrayList();
            while (data.next()) {
                Users user = new Users();
                user.setId(data.getInt("id"));
                user.setEmail(data.getString("namn"));
                programs.add(user);
            }
            conn.close();
            return programs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getKlasser() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT id, namn FROM klass";
            ResultSet data = stmt.executeQuery(sql);
            List<Users> handledare = new ArrayList();
            while (data.next()) {
                Users user = new Users();
                user.setId(data.getInt("id"));
                user.setEmail(data.getString("namn"));
                handledare.add(user);
            }
            conn.close();
            return handledare;
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
            String sql = String.format("SELECT id FROM program WHERE namn = '%s'", namn);
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            int programId = data.getInt("id");
            conn.close();
            return programId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public List getSkolansAnvändare() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM skolans_anvandare_handledare ORDER BY namn";
            ResultSet data = stmt.executeQuery(sql);
            List<Users> skolans_användare = new ArrayList();
            while (data.next()) {
                Users temp = new Users();
                temp.setId(data.getInt("ID"));
                temp.setNamn(data.getString("namn"));
                temp.setTfnr(data.getString("Telefonnummer"));
                temp.setEmail(data.getString("email"));
                temp.setKlass(data.getInt("klass"));
                temp.setHl_id(data.getInt("handledare_ID"));
                temp.setHl_namn(data.getString("hNamn"));
                temp.setBehörighet(data.getInt("behörighet"));
                skolans_användare.add(temp);
            }
            conn.close();
            return skolans_användare;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getHandledareAnv() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM handledare ORDER BY namn";
            ResultSet data = stmt.executeQuery(sql);
            List<Users> handledareList = new ArrayList();
            while (data.next()) {
                Users temp = new Users();
                temp.setId(data.getInt("ID"));
                temp.setNamn(data.getString("namn"));
                temp.setTfnr(data.getString("Telefonnummer"));
                temp.setEmail(data.getString("email"));
                temp.setProgram_id(data.getInt("program_id"));
                temp.setAnvnamn(data.getString("användarnamn"));
                temp.setFöretag(data.getString("företag"));

                handledareList.add(temp);
            }
            conn.close();
            return handledareList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String sparaSkAnv() {
        try {
            int id = selectedUser.getId();
            String namn = selectedUser.getNamn();
            String tfnr = selectedUser.getTfnr();
            String email = selectedUser.getEmail();
            int klass = selectedUser.getKlass();
            int hl_id = selectedUser.getHl_id();

            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE skolans_användare SET namn = '%s', "
                    + "Telefonnummer = '%s', "
                    + "email = '%s', "
                    + "handledare_ID = %d, "
                    + "klass = %d "
                    + "WHERE ID = %d", namn, tfnr, email, hl_id, klass, id);
            stmt.executeUpdate(sql);
            conn.close();
            return "redigeraSkStart";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redigeraSkAnv";
        }
    }

    public String sparaHL() {
        try {
            int id = selectedHL.getId();
            String namn = selectedHL.getNamn();
            String tfnr = selectedHL.getTfnr();
            String email = selectedHL.getEmail();
            int p_id = selectedHL.getProgram_id();
            String företag = selectedHL.getFöretag();
            String anvnamn = selectedHL.getAnvnamn();
            String lösenord = selectedHL.getLösenord().trim();

            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("UPDATE handledare SET namn = '%s', "
                    + "telefonnummer = '%s', "
                    + "email = '%s', "
                    + "företag = '%s', "
                    + "program_id = %d, "
                    + "användarnamn = '%s' ",
                    namn, tfnr, email, företag, p_id, anvnamn);
            if (!lösenord.equals("")) {
                String encrypted_lösenord = BCrypt.hashpw(lösenord, BCrypt.gensalt());
                sql += String.format(", lösenord = '%s' ", encrypted_lösenord);
            }
            sql += String.format("WHERE ID = %d", id);
            stmt.executeUpdate(sql);
            conn.close();
            return "redigeraHLStart";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redigeraHL";
        }
    }
}

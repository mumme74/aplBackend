/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel Nilsson
 */
public class AdminManagerTest {

    public AdminManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addClass method, of class AdminManager.
     */
    @Test
    public void testAddClass() {
        System.out.println("addClass");
        String expResult = "EttTest";
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "INSERT INTO klass VALUES(NULL, 'EttTest', 1)";
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = "";
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT namn FROM klass WHERE namn = 'EttTest'";
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            result = data.getString("namn");
        } catch (Exception e) {
        }
        
        assertEquals(expResult, result);
        
        System.out.println("deleteClass");
        expResult = "";
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "DELETE FROM klass WHERE namn ='EttTest'";
            stmt.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        result = "";
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT namn FROM klass WHERE namn = 'EttTest'";
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            result = data.getString("namn");
        } catch (Exception e) {
        }
        
        assertEquals(expResult, result);
    }
    
     /**
     * Test of addClass method, of class AdminManager.
     */
    @Test
    public void testSetBehörighet() {
        System.out.println("setBehörighet");
        int result = 0;
        
        int expResult = 9;
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "UPDATE skolans_användare SET behörighet = 9 WHERE ID = 1001";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
        }
        
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT behörighet FROM skolans_användare WHERE ID = 1001";
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            result = data.getInt("behörighet");
        } catch (Exception e) {
        }
        
        try {
            String url = "jdbc:mysql://10.97.72.5/aplapp";
            String user = "aplapp";
            String password = "Teknikum123";
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "UPDATE skolans_användare SET behörighet = 0 WHERE ID = 1001";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
        }
        
        assertEquals(expResult, result);
    }
    
    
    
    
    
    

}

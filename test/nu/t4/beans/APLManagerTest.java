/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import javax.ejb.embeddable.EJBContainer;
import javax.json.JsonArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carlkonig
 */
public class APLManagerTest {
    
    public APLManagerTest() {
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
     * Test of registerUser method, of class APLManager.
     */
    @Test
    public void testRegisterUser() throws Exception {
        System.out.println("registerUser");
        String google_id = "thisisatest";
        String namn = "Tester";
        int klass = 0;
        String tfnr = "0768104001";
        String email = "thisisa@test.te";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        APLManager instance = (APLManager)container.getContext().lookup("java:global/classes/APLManager");
        boolean expResult = true;
        boolean result = instance.registerGoogleUser(google_id, namn, klass, tfnr, email);
        assertEquals(expResult, result);
        instance.deleteUser(google_id);
        container.close();
    }
    
}

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
     * Test of getUser method, of class APLManager.
     */
    @Test
    public void testGetUser() throws Exception {
        System.out.println("getUser");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        APLManager instance = (APLManager)container.getContext().lookup("java:global/classes/APLManager");
        JsonArray expResult = null;
        JsonArray result = instance.getUser();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerUser method, of class APLManager.
     */
    @Test
    public void testRegisterUser() throws Exception {
        System.out.println("registerUser");
        String googleID = "thisisatest";
        String namn = "Tester";
        int klass = 0;
        int tfnr = 768104001;
        String email = "thisisa@test.te";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        APLManager instance = (APLManager)container.getContext().lookup("java:global/classes/APLManager");
        boolean expResult = true;
        boolean result = instance.registerUser(googleID, namn, klass, tfnr, email);
        assertEquals(expResult, result);
        container.close();
    }
    
}

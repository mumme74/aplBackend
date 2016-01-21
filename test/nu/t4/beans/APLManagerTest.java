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
        String användarnamn = "thisisatest";
        String lösenord = "testertesttest";
        String namn = "Tester";
        int klass = 1;
        String tfnr = "0768104001";
        String email = "thisisa@test.te";
        int program_id = 1;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        APLManager instance = (APLManager) container.getContext().lookup("java:global/classes/APLManager");
        boolean expResult = true;
        boolean result = instance.registerGoogleUser(google_id, namn, klass, tfnr, email);
        assertEquals(expResult, result);
        
        expResult = true;
        result = instance.deleteUser(google_id, true);
        assertEquals(expResult, result);
        
        //Testa handledare registrering
        expResult = true;
        result = instance.registerHandledare(användarnamn, namn, lösenord, tfnr, email, program_id);
        assertEquals(expResult, result);
        //Logga in som den nya handledaren
        expResult = true;
        result = instance.handledarAuth(användarnamn, lösenord);
        assertEquals(expResult, result);
        //Ta bort den nya handledaren
        expResult = true;
        result = instance.deleteUser(användarnamn, false);
        assertEquals(expResult, result);

        container.close();
    }

}

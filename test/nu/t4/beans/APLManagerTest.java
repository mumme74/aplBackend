/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import java.util.Base64;
import javax.ejb.embeddable.EJBContainer;
import javax.json.JsonArray;
import javax.xml.crypto.dsig.Transform;
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

    private EJBContainer container;
    
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
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
    }

    @After
    public void tearDown() {
        container.close();
    }

    /**
     * Test att registrera google användare
     */
    @Test
    public void testRegisteraGoogle() throws Exception {
        System.out.println("registerUser");
        String google_id = "googleidfortesting";
        String namn = "Tester";
        int klass = 1;
        String tfnr = "0768104001";
        String email = "thisisa@test.te";
        APLManager instance = (APLManager) container.getContext().lookup("java:global/classes/APLManager");
        //Skapa en ny elev
        boolean expResult = true;
        boolean result = instance.registerGoogleUser(google_id, namn, klass, tfnr, email);
        assertEquals(expResult, result);
        //Hämta den nya google användaren, kolla om behörigheten är elev
        int expResult2 = 0;
        int result2 = instance.getGoogleUser(google_id).getInt("behörighet");
        assertEquals(expResult2, result2);
        //Ta bort den nya eleven
        result = instance.deleteUser(google_id, true);
        assertEquals(expResult, result);
    }

    /**
     * Test att registrera handledare
     */
    @Test
    public void testRegisteraHandledare() throws Exception {
        System.out.println("registeraHandledare");
        String användarnamn = "thisisatest";
        String lösenord = "testertesttest";
        String namn = "Tester";
        String tfnr = "0768104001";
        String email = "thisisa@test.te";
        int program_id = 1;
        String foretag = "Företag Test";
        APLManager instance = (APLManager) container.getContext().lookup("java:global/classes/APLManager");
        boolean expResult = true;
        //Testa handledare registrering
        boolean result = instance.registerHandledare(användarnamn, namn, lösenord, tfnr, email, program_id, foretag);
        assertEquals(expResult, result);
        //Logga in som den nya handledaren
        byte[] byteArray = (användarnamn + ":" + lösenord).getBytes();
        String basic_auth = "Basic " + Base64.getEncoder().encodeToString(byteArray);
        result = instance.handledarAuth(basic_auth);
        assertEquals(expResult, result);
        //Ta bort den nya handledaren
        result = instance.deleteUser(användarnamn, false);
        assertEquals(expResult, result);
    }
    
    /**
     * Test att lägga till en loggbok
     */
    @Test
    public void testLoggbok() throws Exception {
        System.out.println("postLogg");
        //Indata
        int id = 1;
        int ljus = 0;
        String datum = "2016-01-01";
        String innehall = "En loggbok skapad av APLManagerTest.java; den borde inte finnas i databasen.";
        String bild = "bild.jpg";

        APLManager instance = (APLManager) container.getContext().lookup("java:global/classes/APLManager");

        //Testa att lägga in en ny loggbok
        boolean expResult = true;
        boolean result = instance.postLogg(id, innehall, datum, ljus, bild);
        assertEquals(expResult, result);

        //Testa att lägga in en identisk
        expResult = false;
        result = instance.postLogg(id, innehall, datum, ljus, bild);
        assertEquals(expResult, result);

        //Ta bort den nya loggboken.
        expResult = true;
        result = instance.deleteLogg(id, datum);
        assertEquals(expResult, result);
    }
}

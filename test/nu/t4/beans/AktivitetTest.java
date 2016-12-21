/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import nu.t4.beans.global.AktivitetManager;
import nu.t4.beans.global.APLManager;
import nu.t4.beans.larare.LarareHandledareManager;
import javax.ejb.embeddable.EJBContainer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
public class AktivitetTest {

    public AktivitetTest() {
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
     * Test att koppla elever och handledare
     */
    @Test
    public void testAktivitet() throws Exception {
        System.out.println("testAktivitet");

        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AktivitetManager aktivitetManager = (AktivitetManager) container.getContext().lookup("java:global/classes/AktivitetManager");
        APLManager aplManager = (APLManager) container.getContext().lookup("java:global/classes/APLManager");
        LarareHandledareManager elevHandledare = (LarareHandledareManager) container.getContext().lookup("java:global/classes/ElevHandledare");

        //Skapa en elev samt aktivitet att testa på
        boolean expResult = true;
        String google_id = "TestID";
        boolean result = aplManager.registerGoogleUser(google_id, "DELETE ME", 1, "01234", "testuser@t4.nu");
        assertEquals(expResult, result);

        int testUserID = aplManager.getGoogleUser(google_id).getInt("id");
        String datum = "2000-01-01";
        result = aplManager.postLogg(testUserID, "TEST, DELETE THIS", datum, 0, "test.jpg");
        assertEquals(expResult, result);

        //Sätt elevens handledare
        int handledare_id = -1;
        JsonArray input = Json.createArrayBuilder().add(
                Json.createObjectBuilder()
                .add("elev_id", testUserID)
                .add("handledare_id", handledare_id).build()).build();
        result = elevHandledare.setElevHandledare(input);
        assertEquals(expResult, result);
        
        //Hämta den nya aktiviteten
        JsonArray aktiviteter = aktivitetManager.getAktiviteter(handledare_id);
        JsonObject aktivitet = aktiviteter.getJsonObject(0);
        int typ = aktivitet.getInt("typ");
        int aktivitet_id = aktivitet.getInt("id");
        
        //Sätt den som godkänd
        result = aktivitetManager.uppdateraAktivitet(typ, 1, aktivitet_id, handledare_id);
        assertEquals(expResult, result);
        
        //Ta bort eleven och aktiviteten
        result = aplManager.deleteLogg(testUserID, datum);
        assertEquals(expResult, result);
        
        //Ta bort den nya eleven
        result = aplManager.deleteUser(google_id, true);
        assertEquals(expResult, result);
        
        container.close();
    }
}

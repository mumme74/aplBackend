/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import nu.t4.beans.global.APLManager;
import nu.t4.beans.larare.LarareHandledareManager;
import java.util.Base64;
import javax.ejb.embeddable.EJBContainer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import nu.t4.beans.larare.LarareHandledareManager;
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
public class ElevHandledareTest {

    public ElevHandledareTest() {
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
    public void testElevHandledareKoppling() throws Exception {
        System.out.println("setElevHandledare");

        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        LarareHandledareManager elevHandledare = (LarareHandledareManager) container.getContext().lookup("java:global/classes/ElevHandledare");
        APLManager aplManager = (APLManager) container.getContext().lookup("java:global/classes/APLManager");
        //Hämta en elev att testa på
        JsonArray elever = elevHandledare.getElev(); //Funktion flyttad
        JsonObject elev = (JsonObject) elever.get(0);
        int originellaHandledaren = elev.getInt("handledare_ID");
        int targetElev = elev.getInt("ID");

        //skapa en ny handledare att byta till
        String användarnamn = "testHandledare";
        String lösenord = "testPass";
        
        boolean expResult = true;
        boolean result = aplManager.registerHandledare(användarnamn, "DELETE ME", lösenord, "0987667", "test@test.se", 1, "Test Inc");
        assertEquals(expResult, result);

        byte[] byteArray = (användarnamn + ":" + lösenord).getBytes();
        String basic_auth = "Basic " + Base64.getEncoder().encodeToString(byteArray);
        int nyaHandledaren = aplManager.getHandledarId(basic_auth);
        
        //Testa att byta handledare
        JsonArrayBuilder abuilder = Json.createArrayBuilder();
        JsonObjectBuilder obuilder = Json.createObjectBuilder();
        abuilder.add(
                obuilder.add("elev_id", targetElev)
                .add("handledare_id", nyaHandledaren).build()
        );
        JsonArray data = abuilder.build();

        System.out.println(targetElev);
        System.out.println(nyaHandledaren);
        result = elevHandledare.setElevHandledare(data);
        assertEquals(expResult, result);

        //Byt tillbaka
        abuilder.add(
                obuilder.add("elev_id", targetElev)
                .add("handledare_id", originellaHandledaren).build()
        );
        data = abuilder.build();

        expResult = true;
        result = elevHandledare.setElevHandledare(data);
        assertEquals(expResult, result);
        
        //Ta bort den nya handledaren
        result = aplManager.deleteUser(användarnamn, false);
        assertEquals(expResult, result);

        container.close();
    }
}

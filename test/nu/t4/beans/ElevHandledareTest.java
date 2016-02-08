/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

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
        ElevHandledare instance = (ElevHandledare) container.getContext().lookup("java:global/classes/ElevHandledare");
        //Hämta en elev att testa på
        JsonArray elever = instance.getElev();
        JsonObject elev = (JsonObject)elever.get(0);
        int originellaHandledaren = elev.getInt("handledare_ID");
        int targetElev = elev.getInt("ID");
        //Hämta en handledare
        JsonArray handledare = instance.getHandledare();
        JsonObject handledaren;
        int nyaHandledaren = -1;
        while(handledare.iterator().hasNext())
        {
            handledaren = (JsonObject)handledare.iterator().next();
            if(handledaren.getInt("ID") != originellaHandledaren)
            {
                nyaHandledaren = handledaren.getInt("ID");
                break;
            }
        }
        assertNotEquals(nyaHandledaren, -1);
        
        //Testa att byta handledare
        JsonArrayBuilder abuilder = Json.createArrayBuilder();
        JsonObjectBuilder obuilder = Json.createObjectBuilder();
        abuilder.add(
                obuilder.add("elev_id", targetElev)
                .add("handledare_id", nyaHandledaren).build()
        );
        JsonArray data = abuilder.build();
        
        boolean expResult = true;
        boolean result = instance.setElevHandledare(data);
        assertEquals(expResult, result);

        //Byt tillbaka
        abuilder.add(
                obuilder.add("elev_id", targetElev)
                .add("handledare_id", originellaHandledaren).build()
        );
        data = abuilder.build();
        
        expResult = true;
        result = instance.setElevHandledare(data);
        assertEquals(expResult, result);

        container.close();
    }
}

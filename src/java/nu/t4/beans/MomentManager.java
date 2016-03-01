/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.math.BigDecimal;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @authorDaniel Lundberg
 */
@Stateless
public class MomentManager {

    /**
     * <h2>Skapa Moment</h2>
     * <p>
     * Funktion för att skapa moment, skall användas av lärare</p>
     *
     * @param user_id Lärarens id
     * @param Innehåll beskrivning av momentet
     * @return true om det skapats någonting annars false
     */
    public boolean skapaMoment(int user_id, String Innehåll) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("INSERT into aplapp.moment VALUES(null, %d,'%s');", user_id, Innehåll);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error from MomentManager:skapaMoment: " + e.getMessage());
            return false;
        }
    }

    /**
     * <h2>Se moment</h2>
     * <p>
     * Funktion för att se de moment som en elev har sig tilldelade</p>
     *
     * @param elev_id elevens id
     * @return jsonarray med [{moment:number, godkand:number},..]
     */
    public JsonArray seMoment(int elev_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("SELECT * FROM aplapp.tilldela_moment, moment WHERE tilldela_moment.användar_id = %d AND moment_id = moment.ID;;", elev_id);
            Statement stmt = conn.createStatement();
            System.out.println(sql);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("moment", data.getInt("moment_id"))
                        .add("innehall", data.getString("innehåll"))
                        .add("godkand", data.getInt("godkänd"))
                        .build()
                );
            }
            conn.close();
            return arrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error from MomentManager:seMoment: " + e.getMessage());
            return null;
        }

    }

    /**
     * <h2>Hämta lärarens moment</h2>
     * <p>
     * Funktion som hämtar alla momenten som den inloggade läraren har
     * skapat</p>
     *
     * @param lärar_id
     * @return jsonarray med [{ID:number, innehall:varchar},..]
     */
    public JsonArray seMomentLärare(int lärar_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT ID, innehåll from moment WHERE användar_ID =" + lärar_id;
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", data.getInt("ID"))
                        .add("innehall", data.getString("innehåll"))
                        .build()
                );
            }
            conn.close();
            return arrayBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * <h2>Radera lärarens moment</h2>
     * <p>
     * Funktion som raderar det valda momentet som läraren har skapat</p>
     *
     * @param moment_id
     * @param lärar_id
     * @return true om raderingen lyckas annars false
     */
    public boolean raderaMomentLärare(int moment_id, int lärar_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM moment WHERE ID = %d AND användar_ID = %d", moment_id, lärar_id);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * <h2>Tilldela Moment</h2>
     * <p>
     * funktion som kopplar moment med elever</p>
     *
     * @param moment json array av momenten
     * @param elever json array av eleverna
     * @return true om det skapats någonting annars false
     */
    public boolean tilldelaMoment(JsonArray moment, JsonArray elever) {
        try {
            Connection conn = ConnectionFactory.getConnection("test");
            Statement stmt = conn.createStatement();

            String sqlbase = "INSERT INTO tilldela_moment(användar_id, moment_id, godkänd) VALUES (%d,%d,0);";
            Iterator<JsonValue> mIterator = moment.iterator();
            while (mIterator.hasNext()) {
                JsonObject momentet = (JsonObject) mIterator.next();
                Iterator<JsonValue> eIterator = elever.iterator();
                while (eIterator.hasNext()) {
                    JsonObject eleven = (JsonObject) eIterator.next();
                    int moment_id = momentet.getInt("moment_id");
                    int elev_id = eleven.getInt("elev_id");
                    String sql = String.format(sqlbase, elev_id, moment_id);
                    stmt.addBatch(sql);
                }
            }
            stmt.executeBatch();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error from MomentManager:kopplaElev_Moment: " + e.getMessage());
            return false;
        }
    }
}

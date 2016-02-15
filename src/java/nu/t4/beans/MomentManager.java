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
     *  <h2>Skapa Moment</h2>
     * <p>Funktion för att skapa moment, skall användas av lärare</p>
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
     *<h2>Se moment</h2>
     * <p>Funktion för att se de moment som en elev har sig tilldelade</p>
     * @param elev_id elevens id
     * @return jsonarray med [{moment:number, godkand:number},..]
     */
    public JsonArray seMoment(int elev_id) {
        try {
  Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("SELECT * FROM aplapp.tilldela_moment WHERE användar_id = %d;", elev_id);
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while(data.next()){
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("moment",data.getInt("moment_id"))
                        .add("godkand",data.getInt("godkänd"))
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
     *<h2>Koppla elev med moment</h2>
     * <p>funktion som kopplar elev med ett moment</p>
     * @param elev_id elevens id
     * @param moment_id momentets id
     * @return true om det skapats någonting annars false
     */
    public boolean kopplaElev_Moment(int elev_id, int moment_id){
            try {
            Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("INSERT INTO tilldela_moment VALUES(%d,%d,0);", elev_id, moment_id);
                System.out.println(sql);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error from MomentManager:kopplaElev_Moment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     *  <h2>Koppla klass med moment</h2>
     * <p>funktion som kopplar de elever som tillhör en klass med ett moment</p>
     * @param klass_id klassens id
     * @param moment_id momentets id
     * @return true om det skapats någonting annars false
     */
    public boolean kopplaKlass_Moment(int klass_id, int moment_id){
            try {
            List<Integer> elev_ids = new ArrayList<>();
            Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("SELECT ID FROM aplapp.skolans_användare WHERE klass=%d AND behörighet = 0", klass_id);
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery(sql);
            while(data.next()){
                elev_ids.add(data.getInt("ID"));
            }
            conn.close();
            for(int elev_id: elev_ids){
                System.out.println(String.format("Elev_ID : %d, Moment_ID: %d",elev_id,moment_id));
                kopplaElev_Moment(elev_id, moment_id);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error from MomentManager:koplaKlass: " + e.getMessage());
            return false;
        }
    }

}

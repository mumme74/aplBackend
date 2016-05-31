/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class KommentarManager {

    public boolean postKommentar(int användar_id, int loggbok_id,
                                    String innehåll, String datum) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("INSERT INTO kommentar VALUES"
                    + "(NULL,%d,%d,'%s','%s')",
                    användar_id, loggbok_id, innehåll, datum);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public JsonArray getKommentar(int logg_id){
        
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM kommentarvy WHERE loggbok_id ="+logg_id;
            ResultSet data = stmt.executeQuery(sql);
            
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (data.next()) {
                
                JsonObjectBuilder obuilder = Json.createObjectBuilder();
                obuilder.add("innehall", data.getString("innehåll"))
                        .add("datum", data.getString("datum"))
                        .add("namn", data.getString("namn"));
                jsonArray.add(obuilder.build());
            }
            conn.close();
            return jsonArray.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public boolean raderaKommentar(int kommentar_id, int användar_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            String sql = String.format("DELETE FROM kommentar WHERE kommentar_id = %d AND användar_id = %d", kommentar_id, användar_id);
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

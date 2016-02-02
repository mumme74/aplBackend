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

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class GetLoggElevManager {
    public JsonArray getLoggar(int elev_id){
        try {
            
            Connection conn = ConnectionFactory.getConnection("local");
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM loggbok WHERE elev_id = %d", elev_id);
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while(data.next()){
                jsonArray.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("elev_id", data.getInt("elev_id"))
                        .add("innehall", data.getString("innehåll"))
                        .add("intryck", data.getInt("intryck"))
                        .add("datum", data.getInt("datum"))
                        .add("bild_id", data.getInt("bild_id"))
                        .build());
            }
            conn.close();
            return jsonArray.build();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

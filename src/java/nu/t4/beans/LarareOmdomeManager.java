/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.math.BigDecimal;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author maikwagner
 */

@Stateless
public class LarareOmdomeManager {
    public JsonObject getOmdome(int id) {
        String sql =""; 
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            sql = String.format(
                    "SELECT count(CASE WHEN intryck = 0 THEN 1 ELSE NULL END) as antal0,count(CASE WHEN intryck = 1 THEN 1 ELSE NULL END) as antal1,count(CASE WHEN intryck = 2 THEN 1 ELSE NULL END) as antal2 FROM loggbok WHERE elev_id = %d",id
            );
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            JsonObjectBuilder omdome = Json.createObjectBuilder()
                        .add("antal0", data.getInt("antal0"))
                        .add("antal1", data.getInt("antal1"))
                        .add("antal2", data.getInt("antal2"));
            

            conn.close();
            return omdome.build();

        } catch (Exception e) {
            System.out.println("LarareOmdomeManager");
            System.out.println(e.getMessage());
            return null;
        }

    }
}

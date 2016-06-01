/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.mysql.jdbc.Connection;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Daniel Lundberg
 */
@Stateless
public class ElevManager {
    
    public JsonArray getElevFranKlass(int klass_id){
        try {
            Connection conn = ConnectionFactory.getConnection();
            String sql = String.format("SELECT ID, namn, handledare_ID "
                    + " FROM aplapp.skolans_användare WHERE klass = %d AND behörighet = 0", klass_id);
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                arrayBuilder.add(
                        Json.createObjectBuilder()
                        .add("id",data.getInt("ID"))
                        .add("namn",data.getString("namn"))
                        .add("handledare_ID",data.getInt("handledare_ID"))
                        .build()
                );
                
            }
            conn.close();
            return arrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error from ElevManager:getElevFranKlass: "+e.getMessage());
            return null;
        }
        
    }
}

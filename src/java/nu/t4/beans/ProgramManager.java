/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author danlun2
 */
@Stateless
public class ProgramManager {

    public JsonArray getPrograms() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            String sql = "";
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            while (data.next()) {

                arrayBuilder.add(
                        Json.createObjectBuilder()
                        .add("namn","byt ut")
                        .add("id",1)
                        .build()
            ) ;
            }
            conn.close();
            return arrayBuilder.build();
        } catch (Exception e) {
            return null;
        }

    }

}

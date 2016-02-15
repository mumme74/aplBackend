/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author luan96001
 */
@Stateless
public class LarareEleverManager {

    public JsonArray getElever(int klass_id) {
        try {
            Connection conn = ConnectionFactory.getConnection("");
            Statement stmt = conn.createStatement();
            String sql = "SELECT namn, ID FROM skolans_användare WHERE klass = " + klass_id;
            ResultSet data = stmt.executeQuery(sql);
            System.out.println(sql);

            JsonArrayBuilder klass = Json.createArrayBuilder();

            while (data.next()) {
                klass.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("namn", data.getString("namn"))
                        .build()
                );
            }

            conn.close();
            return klass.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}

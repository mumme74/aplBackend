/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans.larare;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import nu.t4.beans.ConnectionFactory;

/**
 *
 * @author luan96001
 */
@Stateless
public class LarareEleverManager {

    public JsonArray getElever(int klass_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT namn, id, handledare_id FROM google_anvandare WHERE behorighet= 0 AND klass = " + klass_id;
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder klass = Json.createArrayBuilder();

            while (data.next()) {
                klass.add(Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("namn", data.getString("namn"))
                        .add("hl_id", data.getInt("handledare_id"))
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

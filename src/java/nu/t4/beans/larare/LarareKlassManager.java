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
public class LarareKlassManager {

    public JsonArray getKlasser(int larare_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT id, namn FROM aplapp.klass "
                    + "WHERE program_id = (SELECT program_id FROM klass "
                    + "WHERE id = (SELECT klass FROM google_anvandare "
                    + "WHERE id = %d));", larare_id);
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder klasser = Json.createArrayBuilder();

            while (data.next()) {
                klasser.add(Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("namn", data.getString("namn"))
                        .build()
                );
            }

            conn.close();
            return klasser.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public JsonArray getElever(int anv_id, int klass_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            //Select info from elever where klass = klass_id if lärare has access to it
            String sql = String.format("SELECT namn, id FROM google_anvandare "
                    + "WHERE behorighet = 0 AND klass = %d AND %d IN (SELECT id FROM klass "
                    + "WHERE program_id = (SELECT program_id FROM klass "
                    + "WHERE id = (SELECT klass FROM google_anvandare "
                    + "WHERE id = %d)))", klass_id, klass_id, anv_id);
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder elever = Json.createArrayBuilder();

            while (data.next()) {
                elever.add(Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("namn", data.getString("namn"))
                        .build()
                );
            }

            conn.close();
            return elever.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}

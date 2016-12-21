/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans.global;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import nu.t4.beans.ConnectionFactory;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class AnvInfoManager {

    public JsonObject getElevInfo(int elev_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM elevinfo WHERE ID = " + elev_id;
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            JsonObjectBuilder obj = Json.createObjectBuilder();
            obj.add("namn", data.getString("namn"))
                    .add("tfnr", data.getString("tfnr"))
                    .add("email", data.getString("email"))
                    .add("klass", data.getInt("klass"))
                    .add("hl_id", data.getInt("hl_id"));

            conn.close();
            return obj.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public JsonObject getHandledareInfo(int hl_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM hlinfo WHERE ID = " + hl_id;
            ResultSet data = stmt.executeQuery(sql);
            data.next();
            JsonObjectBuilder obj = Json.createObjectBuilder();
            obj.add("namn", data.getString("namn"))
                    .add("tfnr", data.getString("tfnr"))
                    .add("email", data.getString("email"))
                    .add("program_id", data.getInt("program_id"))
                    .add("foretag", data.getString("företag"))
                    .add("anvnamn", data.getString("användarnamn"));

            conn.close();
            return obj.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Hämtar alla lediga handledare
    public JsonArray getHandledare(int klass_id) { //Använder klassid för att hämta program

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT ID, namn, företag FROM handledare "
                    + "where handledare.ID NOT IN "
                    + "(SELECT handledare_ID FROM skolans_användare "
                    + "WHERE handledare.ID = aplapp.skolans_användare.handledare_ID) "
                    + "AND handledare.program_id = (SELECT klass.program_id FROM klass WHERE klass.ID = %d )"
                    + "ORDER BY namn", klass_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String namn_företag = data.getString("namn") + " - " + data.getString("företag");
                jBuilder.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("namn_foretag", namn_företag)
                        .build()
                );
            }

            conn.close();
            return jBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Hämtar alla handledare i ditt program
    public JsonArray getHandledareAlla(int klass_id) { //Använder klassid för att hämta program

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT ID, namn, företag FROM handledare "
                    + "WHERE handledare.program_id = (SELECT klass.program_id FROM klass WHERE klass.ID = %d ) "
                    + "ORDER BY namn", klass_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String namn_företag = data.getString("namn") + " - " + data.getString("företag");
                jBuilder.add(Json.createObjectBuilder()
                        .add("ID", data.getInt("ID"))
                        .add("namn_foretag", namn_företag)
                        .build()
                );
            }

            conn.close();
            return jBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

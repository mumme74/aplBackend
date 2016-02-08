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

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class lärareKontaktManager {

    public JsonArray getKontaktLärare(int klass_id) {

        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT "
                    + "skolans_användare.namn AS elevnamn, \n"
                    + "skolans_användare.email AS elevmail, \n"
                    + "skolans_användare.Telefonnummer AS elevnummer,\n"
                    + "handledare.namn AS hlnamn,\n"
                    + "handledare.email AS hlmail,\n"
                    + "handledare.telefonnummer AS hlnummer\n"
                    + "FROM skolans_användare, handledare\n"
                    + "WHERE skolans_användare.handledare_ID = handledare.ID\n"
                    + "AND skolans_användare.klass = %d", klass_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (data.next()) {
                jsonArray.add(Json.createObjectBuilder()
                        .add("elevanamn", data.getString("elevnamn"))
                        .add("elevmail", data.getString("elevmail"))
                        .add("elevnummer", data.getString("elevnummer"))
                        .add("hlnamn", data.getString("hlnamn"))
                        .add("hlmail", data.getString("hlmail"))
                        .add("hlnummer", data.getString("hlnummer"))
                        .build());
            }
            conn.close();
            return jsonArray.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
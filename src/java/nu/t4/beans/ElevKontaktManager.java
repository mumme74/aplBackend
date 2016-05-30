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
public class ElevKontaktManager {

    public JsonArray getElevKontakt(int elev_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT hlnamn AS namn, hlmail AS mail, "
                    + "hlnr AS tfnr FROM hlkontakt "
                    + "WHERE hlkontakt.ID = (SELECT handledare_ID FROM skolans_användare WHERE ID = %d) "
                    + "UNION "
                    + "SELECT lärarnamn AS namn, lärarmail AS mail, lärarnr AS tfnr FROM lärarekontakt "
                    + "WHERE klass = "
                    + "(SELECT klass FROM skolans_användare WHERE ID = %d)",
                    elev_id, elev_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jBuilder = Json.createArrayBuilder();
            
            while(data.next()){
                jBuilder.add(Json.createObjectBuilder()
                        .add("namn", data.getString("namn"))
                        .add("mail",data.getString("mail"))
                        .add("tfnr", data.getString("tfnr"))
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
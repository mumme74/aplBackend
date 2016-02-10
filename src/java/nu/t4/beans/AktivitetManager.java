/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.beans;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Daniel Nilsson
 */
@Stateless
public class AktivitetManager {

    public final int NARVARO = 0;
    public final int LOGGBOK = 1;
    public final int MOMENT = 2;
    
    public JsonArray getAktiviteter(int handledare_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM aktiviteter "
                    + "WHERE användar_id = (SELECT id FROM skolans_användare "
                    + "WHERE handledare_id = %d)", handledare_id);
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jBuilder = Json.createArrayBuilder();

            while (data.next()) {
                //Eftersom många kolumner kan vara (och är) null så måste det
                //hanteras genom att göra dem till Json null
                JsonObjectBuilder obuilder = Json.createObjectBuilder();
                int typ = data.getInt("typ");
                obuilder.add("typ", typ);
                int id = data.getInt("id");
                obuilder.add("id", id);
                int elev_id = data.getInt("användar_id");
                obuilder.add("elev_id", elev_id);
                String innehall = data.getString("innehåll");
                if (data.wasNull()) {
                    obuilder.add("innehall", JsonObject.NULL);
                } else {
                    obuilder.add("innehall", innehall);
                }
                int trafikljus = data.getInt("trafikljus");
                if (data.wasNull()) {
                    obuilder.add("trafikljus", JsonObject.NULL);
                } else {
                    obuilder.add("trafikljus", trafikljus);
                }
                String datum = data.getString("datum");
                if (data.wasNull()) {
                    obuilder.add("datum", JsonObject.NULL);
                } else {
                    obuilder.add("datum", datum);
                }
                int bild_id = data.getInt("bild_id");
                if (data.wasNull()) {
                    obuilder.add("bild_id", JsonObject.NULL);
                } else {
                    obuilder.add("bild_id", bild_id);
                }
                jBuilder.add(obuilder.build());
            }

            conn.close();
            return jBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean uppdateraAktivitet(int typ, int godkant, int aktivitets_id, int handledare_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "";
            switch (typ) {
                case NARVARO:
                    sql = String.format("UPDATE närvaro SET godkänt = %d "
                            + "WHERE närvaro_id = %d AND användar_id = (SELECT id FROM skolans_användare "
                            + "WHERE handledare_id = %d)", godkant, aktivitets_id, handledare_id);
                    break;
                case LOGGBOK:
                    sql = String.format("UPDATE loggbok SET godkänt = %d "
                            + "WHERE ID = %d AND elev_id = (SELECT id FROM skolans_användare "
                            + "WHERE handledare_id = %d)", godkant, aktivitets_id, handledare_id);
                    break;
                case MOMENT:
                    sql = String.format("UPDATE tilldela_moment SET godkänd = %d "
                            + "WHERE moment_id = %d AND användar_id = (SELECT id FROM skolans_användare "
                            + "WHERE handledare_id = %d)", godkant, aktivitets_id, handledare_id);
                    break;
                default:
                    break;
            }
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}

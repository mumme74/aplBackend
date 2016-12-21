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
public class AktivitetManager {

    public final int NARVARO = 0;
    public final int HANDLEDARE = 0;
    public final int LOGGBOK = 1;
    public final int NEKADE = 1;
    public final int MOMENT = 2;

    public JsonArray getAktiviteter(int anv_id, int tabell) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "";
            switch (tabell) {
                case HANDLEDARE:
                    sql = String.format("SELECT * FROM aktiviteter "
                            + "WHERE användar_id = (SELECT id FROM skolans_användare "
                            + "WHERE handledare_id = %d)", anv_id);
                    break;
                case NEKADE:
                    sql = String.format("SELECT * FROM nekade_aktiviteter "
                            + "WHERE användar_id = %d", anv_id);
                    break;
                default:
                    break;
            }
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
                String bild = data.getString("bild");
                if (data.wasNull()) {
                    obuilder.add("bild", JsonObject.NULL);
                } else {
                    obuilder.add("bild", bild);
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
                    godkant++;
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

    public boolean uppdateraElevAktivitet(int typ, int aktivitets_id, int elev_id, int trafikljus, String innehall) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "";
            switch (typ) {
                case NARVARO:
                    sql = String.format("UPDATE närvaro SET godkänt = 0, trafikljus = %d "
                            + "WHERE närvaro_id = %d AND användar_id = %d", trafikljus, aktivitets_id, elev_id);
                    break;
                case LOGGBOK:
                    sql = String.format("UPDATE loggbok SET godkänt = 0, innehåll = '%s'"
                            + "WHERE ID = %d AND elev_id = %d", innehall, aktivitets_id, elev_id);
                    break;
                case MOMENT:
                    sql = String.format("UPDATE tilldela_moment SET godkänd = 0 "
                            + "WHERE moment_id = %d AND användar_id = %d", aktivitets_id, elev_id);
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

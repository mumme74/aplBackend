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
public class APLManager {

    //ID för vår app
    private final String CLIENT_ID = "60685140292-vlvgllsnphie69dbm0qag4n4v4oqlned.apps.googleusercontent.com";
    //Namnet på appen, bundet till ID:t
    private final String APPLICATION_NAME = "APL Test";

    public boolean registerGoogleUser(String googleID, String namn, int klass, String tfnr, String email) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "INSERT INTO skolans_användare VALUES"
                    + "('%s',null,'%s','%s','%s',%d,1,1,0)",
                    googleID, namn, tfnr, email, klass
            );
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public GoogleIdToken.Payload googleAuth(String idTokenString) {
        //Varibler för verifiering
        HttpTransport httpTransport;
        JsonFactory jsonFactory;
        GoogleIdTokenVerifier verifier;
        try {
            jsonFactory = JacksonFactory.getDefaultInstance();
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Arrays.asList(CLIENT_ID))
                    .build();
        } catch (Exception e) {
            return null;
        }
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception ex) {
            return null;
        }

        //idToken blir null ifall den är felaktig
        if (idToken != null) {
            //Ta ut datan vi behöver från det verifierade idTokenet
            return idToken.getPayload();
            //if (payload.getHostedDomain().equals(APPS_DOMAIN_NAME)) {
            /*
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }*/
        } else {
            return null;
        }
    }

    public boolean handledarAuth(String basic_auth) {
        try {

            basic_auth = basic_auth.substring(basic_auth.indexOf(" ") + 1, basic_auth.length());

            byte[] decoded = Base64.getDecoder().decode(basic_auth);
            String userPass = new String(decoded);

            String anvandarnamn = userPass.substring(0, userPass.indexOf(":"));
            String losenord = userPass.substring(userPass.indexOf(":") + 1, userPass.length());
            
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "SELECT * FROM handledare WHERE användarnamn = '%s'",
                    anvandarnamn);
            System.out.println(sql);
            ResultSet result = stmt.executeQuery(sql);
            result.next();
            if (BCrypt.checkpw(losenord, result.getString("lösenord"))) {
                conn.close();
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public JsonObject getGoogleUser(String google_id) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "SELECT * FROM skolans_användare WHERE google_id = '%s'",
                    google_id);
            ResultSet result = stmt.executeQuery(sql);
            result.next();
            JsonObjectBuilder obuilder = Json.createObjectBuilder();
            obuilder.add("id", result.getInt("id"))
                    .add("namn", result.getString("namn"))
                    .add("tfnr", result.getString("Telefonnummer"))
                    .add("email", result.getString("email"))
                    .add("klass", result.getInt("klass"))
                    .add("handledare_ID", result.getInt("handledare_ID"))
                    .add("senast_inloggad", result.getInt("senast_inloggad"))
                    .add("behörighet", result.getInt("behörighet"));
            conn.close();
            return obuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    boolean deleteUser(String key, boolean googleUser) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql;
            if (googleUser) {
                sql = String.format(
                        "DELETE FROM skolans_användare WHERE google_id = '%s'",
                        key);
            } else {
                sql = String.format(
                        "DELETE FROM handledare WHERE användarnamn = '%s'",
                        key);
            }
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean registerHandledare(String användarnamn, String namn, String lösenord, String tfnr, String email, int program_id, String foretag) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String encrypted_lösenord = BCrypt.hashpw(lösenord, BCrypt.gensalt());
            String sql = String.format(
                    "INSERT INTO handledare VALUES"
                    + "(null, '%s','%s','%s','%s','%s', %d, '%s')",
                    namn, användarnamn, email, encrypted_lösenord, tfnr, program_id, foretag
            );
            stmt.executeUpdate(sql);

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public JsonArray getKlasser() {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql = String.format("SELECT * FROM klass");
            ResultSet data = stmt.executeQuery(sql);
            JsonArrayBuilder jBuilder = Json.createArrayBuilder();

            while (data.next()) {
                jBuilder.add(Json.createObjectBuilder()
                        .add("id", data.getInt("id"))
                        .add("namn", data.getString("namn"))
                        .build());
            }

            conn.close();
            return jBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean postLogg(int id, String innehall, String datum, int ljus) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = (Statement) conn.createStatement();
            String sql = String.format("INSERT INTO loggbok VALUES "
                    + "(null,%d,'%s',%d,'%s',null)", id, innehall, ljus, datum);
            stmt.executeUpdate(sql);
            conn.close();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    boolean deleteLogg(int id, String datum) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement();
            String sql;
            sql = String.format("DELETE FROM loggbok WHERE "
                    + "elev_id = %d AND datum = '%s'", id, datum);
            stmt.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}

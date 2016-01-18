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
    //Databas inloggning
    private final String DATABASE_USER = "aplapp";
    private final String DATABASE_PASS = "Teknikum123";
    
    public JsonArray getUser() {

        try {
            //koppla upp mot databas
            Class.forName("com.mysql.jdbc.Driver");
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost/aplapp", DATABASE_USER, DATABASE_PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM user";
            ResultSet data = stmt.executeQuery(sql);

            //skapa jsonArray
            JsonArrayBuilder aplApp = Json.createArrayBuilder();

            while (data.next()) {
                aplApp.add(Json.createObjectBuilder()
                        .add("sub", data.getObject("sub").toString())
                        .add("namn", data.getObject("namn").toString())
                        .add("klass", data.getObject("klass").toString())
                        .add("larare", data.getObject("larare").toString())
                        .add("tfnr", data.getObject("tfnr").toString())
                        .add("email", data.getObject("email").toString())
                        .add("handledare", data.getObject("handledare").toString())
                        .build()
                );
            }
            //skicka tillbaka den
            conn.close();//Stänger kopplingen till databasen
            return aplApp.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //om det misslyckas skickas inget tillbaka
        return null;
    }

    public boolean registerUser(String googleID, String namn, int klass, String tfnr, String email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost/aplapp", DATABASE_USER, DATABASE_PASS);
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "INSERT INTO skolans_användare VALUES"
                            + "('%s',null,'%s','%s','%s',%d,1,1,0)",
                    googleID, namn, tfnr, email, klass
            );
            stmt.executeUpdate(sql);

            conn.close();
            return true; //Matchen är tillagd*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false; //Matchen kunde ej läggas till
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

    public boolean handledarAuth(String användarnamn, String lösenord) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost/aplapp", DATABASE_USER, DATABASE_PASS);
            Statement stmt = conn.createStatement();
            String sql = String.format(
                    "SELECT * FROM handledare WHERE användarnamn = '%s'",
                    användarnamn);
            ResultSet result = stmt.executeQuery(sql);
            result.next();
            if (BCrypt.checkpw(lösenord, result.getString("lösenord"))) {
                conn.close();
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false; //Matchen kunde ej läggas till
        }
    }

    public JsonObject getGoogleUser(String google_id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost/aplapp", DATABASE_USER, DATABASE_PASS);
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

}

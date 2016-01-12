package nu.t4.services;

import java.io.StringReader;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.APLManager;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.json.MockJsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel Nilsson
 */
@Path("apl")
public class APLService {

    //ID för vår app
    private final String CLIENT_ID = "60685140292-vlvgllsnphie69dbm0qag4n4v4oqlned.apps.googleusercontent.com";
    //Namnet på appen, knytet till ID:t
    private final String APPLICATION_NAME = "APL Test";
    
    //Varibler för verifiering
    HttpTransport httpTransport;
    JsonFactory jsonFactory;
    GoogleIdTokenVerifier verifier;

    @EJB
    APLManager manager;

    public APLService() throws GeneralSecurityException, IOException {
        this.jsonFactory = JacksonFactory.getDefaultInstance();
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Arrays.asList(CLIENT_ID))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        JsonArray table = manager.getUser();
        if (table == null) {
            //FEL
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.ok(table).build();
        }

    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(String body) {
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        
        //Ta ut id token för verifiering
        String idTokenString = jsonObject.getString("id");
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception ex) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        String googleID;
        String email;
        //idToken blir null ifall den är felaktig
        if (idToken != null) {
            //Ta ut datan vi behöver från det verifierade idTokenet
            Payload payload = idToken.getPayload();
            //if (payload.getHostedDomain().equals(APPS_DOMAIN_NAME)) {
            googleID = payload.getSubject();
            email = payload.getEmail();
            /*
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }*/
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        String namn = jsonObject.getString("namn");
        int klass = jsonObject.getInt("klass");
        int tfnr = jsonObject.getInt("tfnr");

        if (manager.registerUser(googleID, namn, klass, tfnr, email)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }
}

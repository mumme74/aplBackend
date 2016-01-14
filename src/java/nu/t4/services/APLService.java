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
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Arrays;

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

    @EJB
    APLManager manager;

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
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkAuth(String body) {

        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String idTokenString = null;
        String användarnamn = null;
        try {
            idTokenString = jsonObject.getString("id");
        } catch (Exception e) {
            try {
                användarnamn = jsonObject.getString("användarnamn");
            } catch (Exception ee) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        if (idTokenString != null) {
            if (manager.googleAuth(idTokenString) != null) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else if (användarnamn != null) {
            String lösenord = jsonObject.getString("lösenord");
            if (manager.handledarAuth(användarnamn, lösenord) != null) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.serverError().build();
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

        Payload payload = manager.googleAuth(idTokenString);

        String googleID = payload.getSubject();
        String email = payload.getEmail();
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

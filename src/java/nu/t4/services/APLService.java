package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import java.io.StringReader;
import java.math.BigDecimal;
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
    @Path("/google/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response googleAuth(@Context HttpHeaders headers) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);

        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject user = manager.getGoogleUser(payload.getSubject());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int behörighet = -1;
        if (user != null) {
            behörighet = user.getInt("behörighet");
        }
        JsonObject data = Json.createObjectBuilder().add("behorighet", behörighet).build();
        return Response.ok(data).build();
    }

    @GET
    @Path("handledare/login")
    public Response handledarAuth(@Context HttpHeaders headers) {
        //Kollar att inloggningen är ok
        String basic_auth = headers.getHeaderString("Authorization");

        if (manager.handledarAuth(basic_auth)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerGoogleUser(String body) {

        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        //Ta ut id token för verifiering
        String idTokenString = jsonObject.getString("id");

        Payload payload = manager.googleAuth(idTokenString);
        if (payload == null) {
            //ID Token är fel.
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String google_id = payload.getSubject();
        if (manager.getGoogleUser(google_id) != null) {
            //Finns redan i databasen.
            return Response.status(Response.Status.CONFLICT).build();
        }
        String email = payload.getEmail();
        String namn = jsonObject.getString("namn");
        int klass = jsonObject.getInt("klass");
        String tfnr = jsonObject.getString("tfnr");

        if (manager.registerGoogleUser(google_id, namn, klass, tfnr, email)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/handledare")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerHandledare(@Context HttpHeaders headers, String body) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);

        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject user = manager.getGoogleUser(payload.getSubject());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if (user.getInt("behörighet") != 1) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String användarnamn = jsonObject.getString("anvandarnamn");
        String lösenord = jsonObject.getString("losenord");
        String email = jsonObject.getString("email");
        String namn = jsonObject.getString("namn");
        String tfnr = jsonObject.getString("tfnr");
        int program_id = jsonObject.getInt("program_id");
        String foretag = jsonObject.getString("foretag");

        if (manager.registerHandledare(användarnamn, namn, lösenord, tfnr, email, program_id, foretag)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("klass")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKlasser() {
        JsonArray data = manager.getKlasser();
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
}

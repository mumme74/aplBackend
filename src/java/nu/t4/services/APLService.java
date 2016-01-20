package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
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
                användarnamn = jsonObject.getString("anvandarnamn");
            } catch (Exception ee) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        if (idTokenString != null) {
            Payload payload = manager.googleAuth(idTokenString);
            if (payload != null) {
                if (manager.getGoogleUser(payload.getSubject()) != null) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.PRECONDITION_FAILED).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else if (användarnamn != null) {
            String lösenord = jsonObject.getString("losenord");
            if (manager.handledarAuth(användarnamn, lösenord)) {
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
    public Response registerHandledare(String body) {

        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        //Ta ut id token för verifiering
        /* 
         * TODO: KOLLA OM LÄRAREN ÄR LEGIT
         *
        String idTokenString = jsonObject.getString("id");

        Payload payload = manager.googleAuth(idTokenString);
        if(payload == null)
        {
            //ID Token är fel.
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
         */
        String användarnamn = jsonObject.getString("anvandarnamn");
        String lösenord = jsonObject.getString("losenord");
        String email = jsonObject.getString("email");
        String namn = jsonObject.getString("namn");
        String tfnr = jsonObject.getString("tfnr");

        if (manager.registerHandledare(användarnamn, namn, lösenord, tfnr, email)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("klass")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerHandledare() {
        JsonArray data = manager.getKlasser();
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
}

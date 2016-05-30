/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.StringReader;
import nu.t4.beans.ElevHandledare;
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
import nu.t4.beans.AktivitetManager;
import nu.t4.beans.LarareManager;

/**
 *
 * @author maikwagner
 */
@Path("get")
public class GetService {

    @EJB
    ElevHandledare elevHandledare;
    @EJB
    APLManager manager;
    @EJB
    AktivitetManager aktivitetManager;

//    @GET
//    @Path("/elever")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getElever(@Context HttpHeaders headers) {
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        if (manager.googleAuth(idTokenString) == null) {
//
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        JsonArray elever = elevHandledare.getElev();
//        if (elever != null) {
//            return Response.ok(elever).build();
//        } else {
//            return Response.serverError().build();
//        }
//    }

//    @GET
//    @Path("handledare")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getHandledare(@Context HttpHeaders headers) {
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        if (manager.googleAuth(idTokenString) == null) {
//
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        JsonArray handledare = elevHandledare.getHandledare();
//        if (handledare != null) {
//            return Response.ok(handledare).build();
//        } else {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GET
    @Path("aktiviteter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAktiviteter(@Context HttpHeaders headers) {
        String basic_auth = headers.getHeaderString("Authorization");

        if (!manager.handledarAuth(basic_auth)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int id = manager.getHandledarId(basic_auth);
        int HANDLEDARE = 0;
        
        JsonArray aktiviteter = aktivitetManager.getAktiviteter(id, HANDLEDARE);
        if (aktiviteter != null) {
            return Response.ok(aktiviteter).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("nekade_aktiviteter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNekadeAktiviteter(@Context HttpHeaders headers) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);

        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject elev = manager.getGoogleUser(payload.getSubject());
        if (elev == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int id = elev.getInt("id");
        int NEKADE = 1;

        JsonArray aktiviteter = aktivitetManager.getAktiviteter(id, NEKADE);
        if (aktiviteter != null) {
            return Response.ok(aktiviteter).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @EJB
    LarareManager larareManager;

    @GET
    @Path("/larare/klasser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKlasser(@Context HttpHeaders headers) {
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
        
        int behörighet = user.getInt("behörighet");

        if (behörighet != 1) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int id = user.getInt("id");

        JsonArray klasser = larareManager.getKlasser(id);
        if (klasser != null) {
            return Response.ok(klasser).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/larare/elever")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getElever(@Context HttpHeaders headers, String body) {
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
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();
        System.out.println(body);
        int anv_id = user.getInt("id");
        int klass_id = data.getInt("klass_id");
        System.out.println(klass_id);

        JsonArray aktiviteter = larareManager.getElever(anv_id, klass_id);
        if (aktiviteter != null) {
            return Response.ok(aktiviteter).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

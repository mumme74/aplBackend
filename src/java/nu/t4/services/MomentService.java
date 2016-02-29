/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.StringReader;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.APLManager;
import nu.t4.beans.ElevMomentManager;
import nu.t4.beans.MomentManager;

/**
 *
 * @author maikwagner
 * @author Daniel Lundberg
 */
@Path("moment")
public class MomentService {

    @EJB
    ElevMomentManager elevMomentManager;

    @EJB
    MomentManager momentManager;

    @EJB
    APLManager manager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMomentElev(@Context HttpHeaders headers) {

        String idTokenString = headers.getHeaderString("Authorization");

        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        if (payload
                == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject elev = manager.getGoogleUser(payload.getSubject());
        if (elev
                == null) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        int användar_id = elev.getInt("id");

        JsonArray moment = elevMomentManager.getMomentElev(användar_id);
        if (moment != null) {
            return Response.ok(moment).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

    }

    @GET
    @Path("/handledare")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMomentPerHandledare() {
        JsonArray moment = elevMomentManager.getMomentPerHandledare();
        if (moment != null) {
            return Response.ok(moment).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }

    @POST
    @Path("/tillHandledare")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response skickaMomentTillHandledare(@Context HttpHeaders headers, String body) {
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
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        int moment_id = object.getInt("id");
        int användar_id = elev.getInt("id");

        if (elevMomentManager.skickaMomentTillHandledare(moment_id, användar_id)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/tillElev")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response skickaMomentTillElev(@Context HttpHeaders headers, String body) {
        //Kollar att inloggningen är ok

        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        int id = object.getInt("id");

        if (elevMomentManager.skickaMomentTillElev(id)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/elev")
    @Produces(MediaType.APPLICATION_JSON)
    public Response visaElevsMoment(@Context HttpHeaders headers, String body) {
        System.out.println(body);
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        //Kol   lar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        int id = object.getInt("elev_id");
        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject user = manager.getGoogleUser(payload.getSubject());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonArray moment = momentManager.seMoment(id);
        if (moment != null) {
            return Response.ok(moment).build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response skapaMoment(@Context HttpHeaders headers, String body) {
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

        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        if (momentManager.skapaMoment(user.getInt("id"), object.getString("beskrivning"))) {
            return Response.status(201).build();
        } else {
            return Response.status(400).build();
        }

    }

    @POST
    @Path("elev_moment/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response kopplaMomentElev(@Context HttpHeaders headers, String body) {
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

        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        if (momentManager.kopplaElev_Moment(object.getInt("elev_id"), object.getInt("moment_id"))) {
            return Response.status(201).build();
        } else {
            return Response.status(400).build();
        }

    }

    @POST
    @Path("klass_moment/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response kopplaMomentKlass(@Context HttpHeaders headers, String body) {
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

        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        if (momentManager.kopplaKlass_Moment(object.getInt("klass_id"), object.getInt("moment_id"))) {
            return Response.status(201).build();
        } else {
            return Response.status(400).build();
        }

    }

    @GET
    @Path("/larare")
    @Produces(MediaType.APPLICATION_JSON)
    public Response seMomentLärare(@Context HttpHeaders headers) {

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
        int lärar_id = user.getInt("id");

        JsonArray moment = momentManager.seMomentLärare(lärar_id);
        if (moment != null) {
            return Response.ok(moment).build();
        } else {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/larare/{id}")
    public Response raderaMoment(@Context HttpHeaders headers, @PathParam("id") int moment_id) {

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
        int lärar_id = user.getInt("id");

        if (momentManager.raderaMomentLärare(moment_id, lärar_id)) {
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

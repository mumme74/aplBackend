/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

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

/**
 *
 * @author maikwagner
 */
@Path("handledare")
public class HandledareService {

    @EJB
    ElevHandledare elevHandledare;
    @EJB
    APLManager manager;
    @EJB
    AktivitetManager aktivitetManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledare() {
        JsonArray handledare = elevHandledare.getHandledare();
        if (handledare != null) {
            return Response.ok(handledare).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/program")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram() {
        JsonArray program = elevHandledare.getProgram();
        if (program != null) {
            return Response.ok(program).build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/aktivitet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response godkannNarvaro(@Context HttpHeaders headers, String body) {
        String basic_auth = headers.getHeaderString("Authorization");

        if (!manager.handledarAuth(basic_auth)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        int aktivitets_id = data.getInt("id");
        int handledare_id = manager.getHandledarId(basic_auth);
        int godkant = data.getInt("godkant");
        int typ = data.getInt("typ");
        
        if (aktivitetManager.uppdateraAktivitet(typ, godkant, aktivitets_id, handledare_id)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }
}

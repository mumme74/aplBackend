/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.APLManager;
import nu.t4.beans.ElevKontaktManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("elev")
public class ElevKontaktService {

//    @EJB
//    APLManager manager;
//
//    @EJB
//    ElevKontaktManager elevManager;
//
//    @GET
//    @Path("/kontakt")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getElevKontakt(@Context HttpHeaders headers) {
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
//        if (payload == null) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        JsonObject användare = manager.getGoogleUser(payload.getSubject());
//        if (användare == null) {
//
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        int användar_id = användare.getInt("id");
//
//        JsonArray data = elevManager.getElevKontakt(användar_id);
//        if (data != null) {
//            return Response.ok(data).build();
//        } else {
//            return Response.serverError().build();
//        }
//    }
}
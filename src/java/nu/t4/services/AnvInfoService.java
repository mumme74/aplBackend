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
import javax.ws.rs.Consumes;
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
import nu.t4.beans.AnvInfoManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("apl")
public class AnvInfoService {
    
//    @EJB
//    APLManager manager;
//    
//    @EJB
//    AnvInfoManager infoManager;
//    
//    @GET
//    @Path("/elev/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getElevInfo(@Context HttpHeaders headers, @PathParam("id") int id){
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
//        if (payload == null) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        
//        JsonObject data = infoManager.getElevInfo(id);
//        if (data != null) {
//            return Response.ok(data).build();
//        } else {
//            return Response.serverError().build();
//        }
//    }
//    
//    @GET
//    @Path("/handledare/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getHandledareInfo(@Context HttpHeaders headers, @PathParam("id") int id){
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
//        if (payload == null) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        
//        JsonObject data = infoManager.getHandledareInfo(id);
//        if (data != null) {
//            return Response.ok(data).build();
//        } else {
//            return Response.serverError().build();
//        }
//    }
//    
//    @GET
//    @Path("/handledare/lista")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getHandledare(@Context HttpHeaders headers){
//        //Kollar att inloggningen är ok
//        String idTokenString = headers.getHeaderString("Authorization");
//        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
//        if (payload == null) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        
//        JsonObject användare = manager.getGoogleUser(payload.getSubject());
//        if (användare == null) {
//
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        
//        JsonArray data = infoManager.getHandledare();
//        if (data != null) {
//            return Response.ok(data).build();
//        } else {
//            return Response.serverError().build();
//        }
//    }
}

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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
@Path("info")
public class AnvInfoService {
    
    @EJB
    APLManager manager;
    
    @EJB
    AnvInfoManager infoManager;
    
    @POST
    @Path("/elev")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElevInfo(@Context HttpHeaders headers, String body){
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject elevObjekt = jsonReader.readObject();
        jsonReader.close();

        int elev_id = elevObjekt.getInt("elev_id");
        
        JsonObject data = infoManager.getElevInfo(elev_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("/handledare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledareInfo(@Context HttpHeaders headers, String body){
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject handledareObjekt = jsonReader.readObject();
        jsonReader.close();
        System.out.println(handledareObjekt);

        int hl_id = handledareObjekt.getInt("hl_id");
        System.out.println(hl_id);
        JsonObject data = infoManager.getHandledareInfo(hl_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("/getHL")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledare(@Context HttpHeaders headers){
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        JsonObject användare = manager.getGoogleUser(payload.getSubject());
        if (användare == null) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        int klass = användare.getInt("klass");
        
        JsonArray data = infoManager.getHandledare(klass);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
}

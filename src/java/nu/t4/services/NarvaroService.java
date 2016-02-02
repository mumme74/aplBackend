/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import nu.t4.beans.APLManager;
import java.io.StringReader;
import nu.t4.beans.NarvaroManager;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
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

/**
 *
 * @author maikwagner
 */
@Path("narvaro")
public class NarvaroService {
    
    @EJB
    APLManager aplManager;
    @EJB
    NarvaroManager narvaroManager;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNarvaro(){
        JsonArray narvaro = narvaroManager.getNarvaro();
        if(narvaro != null){
            return Response.ok(narvaro).build();
        }else{
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNarvaro(@Context HttpHeaders headers, String body) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = aplManager.googleAuth(idTokenString);
        if (payload == null) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject elev = aplManager.getGoogleUser(payload.getSubject());
        if (elev == null) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        int id = elev.getInt("id");
        
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject array = jsonReader.readObject();
        jsonReader.close();
        
        if (narvaroManager.setNarvaro(array, id)) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }
    
    
}

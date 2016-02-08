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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.APLManager;
import nu.t4.beans.GetLoggLärareManager;

/**
 *
 * @author luan96001
 */
public class GetLoggLärareService {
    @EJB
    APLManager manager;
    
    @EJB
    GetLoggLärareManager loggManager;
    
    @GET
    @Path("/allaLoggar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggar(String body){
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String idTokenString = null;
        int användar_id = 0;
        try {
            idTokenString = jsonObject.getString("id");
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        if (idTokenString != null) {
            GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
            if (payload != null) {
                JsonObject användare = manager.getGoogleUser(payload.getSubject());
                if (användare != null) {
                    användar_id = användare.getInt("id");
                } else {
                    return Response.status(Response.Status.PRECONDITION_FAILED).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        
        JsonArray data = loggManager.getLoggar(användar_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
    
}

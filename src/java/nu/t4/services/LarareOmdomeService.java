/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.StringReader;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import nu.t4.beans.LarareOmdomeManager;


/**
 *
 * @author maikwagner
 */

@Path("/omdome")
public class LarareOmdomeService {
    
    @EJB
    LarareOmdomeManager LarareOmdomeManager;
    
    @EJB
    APLManager manager;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOmdome(@Context HttpHeaders headers, String body) {
        
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
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject omdomeObjekt = jsonReader.readObject();
        jsonReader.close();
        System.out.println(omdomeObjekt);

        int id = omdomeObjekt.getInt("id");

        JsonArray data = LarareOmdomeManager.getOmdome(id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    };
}
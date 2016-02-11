/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import nu.t4.beans.KommentarManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("kommentar")
public class KommentarService {
    
    @EJB
    APLManager manager;
    
    @EJB
    KommentarManager kommentarManager;
    
    @POST
    @Path("/postKommentar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLogg(@Context HttpHeaders headers, String body) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        System.out.println(idTokenString);

        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject användare = manager.getGoogleUser(payload.getSubject());
        if (användare == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject kommentarObjekt = jsonReader.readObject();
        jsonReader.close();
        
        
        //Hämtar användarens id mha den medskickade id_token
        int användar_id = användare.getInt("id");
        
        //Hämtar loggbok_id, innehållet och datum från objektet av indatan
        int loggbok_id = kommentarObjekt.getInt("loggbok_id");
        String innehåll = kommentarObjekt.getString("innehall");
        String datum = kommentarObjekt.getString("datum");        
        
        if (kommentarManager.postKommentar(användar_id, loggbok_id, innehåll, datum)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("/getKommentar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKommentar(@Context HttpHeaders headers, String body){
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
        
        //Skapa ett json objekt av indatan
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject kommentarObjekt = jsonReader.readObject();
        jsonReader.close();
        System.out.println(kommentarObjekt);
        
        int logg_id = kommentarObjekt.getInt("loggbok_id");
        
        JsonArray data = kommentarManager.getKommentar(logg_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
}

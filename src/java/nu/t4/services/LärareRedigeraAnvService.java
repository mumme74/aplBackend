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
import nu.t4.beans.LärareRedigeraAnvManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("redigera")
public class LärareRedigeraAnvService {
    
    @EJB
    APLManager manager;
    
    @EJB
    LärareRedigeraAnvManager anvManager;
    
    @POST
    @Path("/elev")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response redigeraElev(@Context HttpHeaders headers, String body){
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
        JsonObject obj = jsonReader.readObject();
        jsonReader.close();
        
        int ID = obj.getInt("ID");
        String namn = obj.getString("namn");
        String tfnr = obj.getString("tfnr");
        String email = obj.getString("email");
        int klass = obj.getInt("klass");
        int handledar_id = obj.getInt("hl_id");
        if(anvManager.redigeraElev(ID, namn, tfnr, email, klass, handledar_id)){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("/handledare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response redigeraHandledare(@Context HttpHeaders headers, String body){
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
        JsonObject obj = jsonReader.readObject();
        jsonReader.close();
        
        int ID = obj.getInt("ID");
        String namn = obj.getString("namn");
        String tfnr = obj.getString("tfnr");
        String email = obj.getString("email");
        String företag = obj.getString("foretag");
        String användarnamn = obj.getString("anvnamn");
        String lösenord = obj.getString("losenord");
        
        if(anvManager.redigeraHandledare(ID, namn, tfnr, email, företag, användarnamn, lösenord)){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.serverError().build();
        }
    }
}

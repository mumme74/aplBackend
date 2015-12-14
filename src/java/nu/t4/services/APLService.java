package nu.t4.services;

import java.io.StringReader;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel Nilsson
 */
@Path("apl")
public class APLService {
    @EJB
    APLManager manager;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        JsonArray table = manager.getUser();
        if (table == null) {
            //FEL
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.ok(table).build();
        }

    }
    
    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(String body){
                
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        int sub = jsonObject.getInt("sub");
        String namn = jsonObject.getString("namn");
        String klass = jsonObject.getString("klass");
        String larare = jsonObject.getString("larare");
        int tfnr = jsonObject.getInt("tfnr");
        String email = jsonObject.getString("email");
        int handledare = jsonObject.getInt("handledare");
        
        if(manager.registerUser(sub, namn, klass, larare, tfnr, email, handledare)){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.serverError().build();
        }
    }
}

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
import nu.t4.beans.HLKontaktManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("handledare")
public class HLKontaktService {
    @EJB
    APLManager manager;
    
    @EJB
    HLKontaktManager hlManager;
    
    @Path("/kontakt")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHLKontakt(@Context HttpHeaders headers){
        //Kollar att inloggningen är ok
        String basic_auth = headers.getHeaderString("Authorization");
        
        if (!manager.handledarAuth(basic_auth)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        int hl_id = manager.getHandledarId(basic_auth);
        
        if (hl_id == -1) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        JsonArray data = hlManager.getHLKontakt(hl_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
        
    }
}

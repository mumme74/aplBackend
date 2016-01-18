/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import nu.t4.beans.ElevHandledare;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author maikwagner
 */
@Path("elev")
public class ElevService {
  
    @EJB
    ElevHandledare elevHandledare;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElev(){
        JsonArray elever = elevHandledare.getElev();
        if(elever != null){
            return Response.ok(elever).build();
        }else{
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    
}

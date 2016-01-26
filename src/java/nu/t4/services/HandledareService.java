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
@Path("handledare")
public class HandledareService {
  
    @EJB
    ElevHandledare elevHandledare;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledare(){
        JsonArray handledare = elevHandledare.getHandledare();
        if(handledare != null){
            return Response.ok(handledare).build();
        }else{
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    
    
    @EJB
    @Path("/program")
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram(){
        JsonArray program = elevHandledare.getProgram();
        if(program != null){
            return Response.ok(program).build();
        }else{
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
}
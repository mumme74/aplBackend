/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.t4.services;


import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.ProgramManager;

/**
 *
 * @author danlun2
 */
@Path("program")
public class ProgramService {
    
    @EJB
    ProgramManager programManager;
            
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrograms(){
        
        //kod
        return Response.ok().build();
    }
}

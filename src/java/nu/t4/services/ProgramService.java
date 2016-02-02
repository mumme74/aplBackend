/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.t4.services;

import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.ProgramManager;
import org.primefaces.json.JSONArray;

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
    public Response getProgram() {
        JsonArray data = programManager.getProgram();
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();

        }
    }
}

package nu.t4.services.larare;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import nu.t4.beans.ElevHandledare;
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
import nu.t4.beans.AktivitetManager;

/**
 *
 * @author maikwagner
 */
@Path("larare")
public class LarareHandledareService {

    @EJB
    ElevHandledare elevHandledare;
    @EJB
    APLManager manager;
    @EJB
    AktivitetManager aktivitetManager;

    @GET
    @Path("/handledare/natverk")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram(@Context HttpHeaders headers) {
        //Kollar att inloggningen är ok
        String idTokenString = headers.getHeaderString("Authorization");
        GoogleIdToken.Payload payload = manager.googleAuth(idTokenString);
        if (payload == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        JsonObject user = manager.getGoogleUser(payload.getSubject());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        int behörighet = user.getInt("behörighet");

        if (behörighet != 1) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        JsonArray program = elevHandledare.getProgram();
        if (program != null) {
            return Response.ok(program).build();
        } else {
            return Response.serverError().build();
        }
    }
}

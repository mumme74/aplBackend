package nu.t4.services.global;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.t4.beans.APLManager;
import nu.t4.beans.AnvInfoManager;
import nu.t4.beans.ElevKontaktManager;
import nu.t4.beans.HLKontaktManager;
import nu.t4.beans.lärareKontaktManager;

/**
 *
 * @author Daniel Nilsson
 */
@Path("info")
public class InfoService {

    @EJB
    APLManager manager;

    @EJB
    AnvInfoManager infoManager;

    @GET
    @Path("/elev/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElevInfo(@Context HttpHeaders headers, @PathParam("id") int id) {
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

        JsonObject data = infoManager.getElevInfo(id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/handledare/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledareInfo(@Context HttpHeaders headers, @PathParam("id") int id) {
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

        JsonObject data = infoManager.getHandledareInfo(id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/handledare/lista")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledare(@Context HttpHeaders headers) {
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

        int klass_id = user.getInt("klass");

        JsonArray data = infoManager.getHandledare(klass_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/handledare/lista/alla")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHandledareAlla(@Context HttpHeaders headers) {
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

        int klass_id = user.getInt("klass");

        JsonArray data = infoManager.getHandledareAlla(klass_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }

    @EJB
    ElevKontaktManager elevManager;

    @GET
    @Path("/elev/kontakt")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getElevKontakt(@Context HttpHeaders headers) {
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
        int användar_id = användare.getInt("id");

        JsonArray data = elevManager.getElevKontakt(användar_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }

    @EJB
    HLKontaktManager hlManager;

    @Path("/handledare/kontakt")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHLKontakt(@Context HttpHeaders headers) {
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

    @EJB
    lärareKontaktManager lararManager;

    @GET
    @Path("/larare/kontakt")
    @Produces(MediaType.APPLICATION_JSON)
    public Response kontaktLärare(@Context HttpHeaders headers) {
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

        int klass_id = user.getInt("klass");

        JsonArray data = lararManager.getKontaktLärare(klass_id);
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.serverError().build();
        }
    }
}

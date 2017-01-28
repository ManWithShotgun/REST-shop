package ru.ilia.rest.service;

/**
 * Created by ILIA on 24.01.2017.
 */
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.dao.Factory;

@Path("/")
public class PredictionsRS {

    static final Logger log = Logger.getLogger("RS");

    @Context
    private ServletContext sctx; // dependency injection
    private static PredictionsList plist; // set in populate()

    public PredictionsRS() {
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/counts")
    public Response getCounts(){
        try {
            long countMonitors = Factory.getInstance().getMonitorDAO().getCountMonitors("","");
            long countCameras = Factory.getInstance().getCameraDAO().getCountCameras("","");
            log.info("Monitors: "+countMonitors+" | "+countCameras);
            return Response.ok(String.format("{\"countMonitors\":%d,\"countCameras\":%d}",countMonitors,countCameras)).build();
        } catch (ExceptionDAO exceptionDAO) {
            log.error("ExceptionDAO", exceptionDAO);
            return Response.ok("{\"countMonitors\":-1,\"countCameras\":-1}").build();
        }
    }

//    @GET
//    @Produces({ MediaType.APPLICATION_JSON })
//    @Path("/monitors")
//    public Response getMonitors(@QueryParam("limit") int limit){
//        log.info("getMonitors: "+limit);
//
//        return Response.ok("{\"meta\":{\"limit\":10,\"next\":\"?limit=10&offset=10\",\"offset\":\"0\",\"previous\":\"?limit=10&offset=0\",\"total_count\":49},\"products\":[{\"id\":0,\"pricePer\":10,\"name\":\"Monitor0\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #0\"},{\"id\":1,\"pricePer\":17,\"name\":\"Monitor1\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #1\"},{\"id\":2,\"pricePer\":24,\"name\":\"Monitor2\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #2\"}]}", "application/json").build();
//    }
//
//    @GET
//    @Produces({ MediaType.APPLICATION_JSON })
//    @Path("/counts")
//    public Response getCounts(){
//        log.info("getCounts");
//        return Response.ok("{\"countMonitors\":120,\"countCameras\":120}", "application/json").build();
//    }
//
//    @GET
//    @Produces({ MediaType.APPLICATION_JSON })
//    @Path("/monitors/{id: \\d+}")
//    public Response getDetails(){
//        log.info("getMoreInfo");
//        return Response.ok("{\"product\":{\"id\":1,\"pricePer\":17,\"name\":\"Monitor1\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #1\"}}", "application/json").build();
//    }
//
//    @PUT
//    @Consumes({MediaType.MULTIPART_FORM_DATA})
//    @Produces({ MediaType.APPLICATION_JSON })
//    @Path("/monitors/{id: \\d+}")
//    public Response updateMonitor(@DefaultValue("none") @FormDataParam("json") String json) {
//        log.info("UPDATE: "+json);
//        return Response.ok("{\"success\":true}", "application/json").build();
//    }

//    ///////////////////////////////////////////////////

    @GET
    @Path("/xml")
    @Produces({ MediaType.APPLICATION_XML })
    public PredictionsList getXml() {
        checkContext();
        return plist;
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({ MediaType.APPLICATION_XML }) // could use "application/xml"
    // instead
    public Response getXml(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/xml");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/json")
    public Response getJson() {
        checkContext();
        String strTmp=toJson(plist);
        log.info(strTmp);
        return Response.ok(strTmp, "application/json").build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/json/{id: \\d+}")
    public Response getJson(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/json");
    }

    @GET
    @Path("/plain")
    @Produces({ MediaType.TEXT_PLAIN })
    public String getPlain() {
        checkContext();
        return plist.toString();
    }

    @POST
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/create")
    public Response create(@FormParam("who") String who, @FormParam("what") String what) {
        checkContext();
        String msg = null;
        // Require both properties to create.
        if (who == null || what == null) {
            msg = "Property 'who' or 'what' is missing.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        }
        // Otherwise, create the Prediction and add it to the collection.
        int id = addPrediction(who, what);
        msg = "Prediction " + id + " created: (who = " + who + " what = " + what + ").\n";
        log.info(msg);
        return Response.ok(msg, "text/plain").build();
    }

    @PUT
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/update")
    public Response update(@FormParam("id") int id, @FormParam("who") String who, @FormParam("what") String what) {
        checkContext();

        // Check that sufficient data are present to do an edit.
        String msg = null;
        if (who == null && what == null)
            msg = "Neither who nor what is given: nothing to edit.\n";

        Prediction p = plist.find(id);
        if (p == null)
            msg = "There is no prediction with ID " + id + "\n";

        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        // Update.
        if (who != null)
            p.setWho(who);
        if (what != null)
            p.setWhat(what);
        msg = "Prediction " + id + " has been updated.\n";
        return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
        checkContext();
        String msg = null;
        Prediction p = plist.find(id);
        if (p == null) {
            msg = "There is no prediction with ID " + id + ". Cannot delete.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        }
        plist.getPredictions().remove(p);
        msg = "Prediction " + id + " deleted.\n";

        return Response.ok(msg, "text/plain").build();
    }

    // ** utilities
    private void checkContext() {
        if (plist == null)
            populate();
    }

    private void populate() {
        plist = new PredictionsList();

        String filename = "/WEB-INF/data/predictions.db";
        InputStream in = sctx.getResourceAsStream(filename);

        // Read the data into the array of Predictions.
        if (in != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                int i = 0;
                String record = null;
                while ((record = reader.readLine()) != null) {
                    String[] parts = record.split("!");
                    addPrediction(parts[0], parts[1]);
                }
            } catch (Exception e) {
                throw new RuntimeException("I/O failed!");
            }
        }
    }

    // Add a new prediction to the list.
    private int addPrediction(String who, String what) {
        int id = plist.add(who, what);
        return id;
    }

    // Prediction --> JSON document
    private String toJson(Prediction prediction) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(prediction);
        } catch (Exception e) {
        }
        return json;
    }

    // PredictionsList --> JSON document
    private String toJson(PredictionsList plist) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(plist);
        } catch (Exception e) {
        }
        return json;
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
        Prediction pred = plist.find(id);
        if (pred == null) {
            String msg = id + " is a bad ID.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        } else if (type.contains("json"))
            return Response.ok(toJson(pred), type).build();
        else
            return Response.ok(pred, type).build(); // toXml is automatic
    }
}

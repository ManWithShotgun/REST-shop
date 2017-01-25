package ru.ilia.rest;

/**
 * Created by ILIA on 25.01.2017.
 */
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.log4j.Logger;

@Path("/monitors")
public class MonitorsRS {

    static final Logger log = Logger.getLogger("MonitorsRS");

    public MonitorsRS() {
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getMonitors(@DefaultValue("10")@QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("offset") int offset,
                                @DefaultValue("") @QueryParam("filter") String filter,
                                @DefaultValue("") @QueryParam("filterName") String filterName){
        log.info(String.format("GET Monitors: limit=%d offset=%d filter=%s filterName=%s", limit, offset, filter, filterName));

        return Response.ok("{\"meta\":{\"limit\":10,\"next\":\"?limit=10&offset=10\",\"offset\":\"0\",\"previous\":\"?limit=10&offset=0\",\"total_count\":49},\"products\":[{\"id\":0,\"pricePer\":10,\"name\":\"Monitor0\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #0\"},{\"id\":1,\"pricePer\":17,\"name\":\"Monitor1\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #1\"},{\"id\":2,\"pricePer\":24,\"name\":\"Monitor2\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #2\"}]}", "application/json").build();
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/{id: \\d+}")
    public Response getMonitor(@PathParam("id") int id){
        log.info("GET ById: "+id);
        return Response.ok("{\"product\":{\"id\":1,\"pricePer\":17,\"name\":\"Монитор1\",\"img\":\"/dist/public/monitor-1.jpg\",\"inch\":23,\"description\":\"This is the description #1\"}}", "application/json").build();
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({ MediaType.APPLICATION_JSON })
    public Response createMonitor(@DefaultValue("none") @FormDataParam("json") String json) {
        log.info("CREATE: "+json);
        return Response.ok("{\"success\":true}", "application/json").build();
    }

    @PUT
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/{id: \\d+}")
    public Response updateMonitor(@DefaultValue("none") @FormDataParam("json") String json) {
        log.info("UPDATE: "+json);
        return Response.ok("{\"success\":true}", "application/json").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id: \\d+}")
    public Response deleteMonitor(@PathParam("id") int id){
        log.info("DELETE: "+id);
        return Response.ok("{\"success\":true}", "application/json").build();
    }

    // ** utilities

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/json")
    public Response justForTest(){
        return Response.ok(toJson(new Account(32,"login1","pass1",2)),"application/json").build();
    }


    private String toJson(Account account) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(account);
        } catch (Exception e) {
        }
        return json;
    }
}

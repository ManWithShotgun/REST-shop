package ru.ilia.rest.service;

/**
 * Created by ILIA on 25.01.2017.
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.log4j.Logger;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Monitor;
import ru.ilia.rest.model.util.MetaPagination;
import ru.ilia.rest.model.util.ProductListJson;

import java.io.IOException;
import java.util.ArrayList;

@Path("/monitors")
public class MonitorsRS {

    static final Logger log = Logger.getLogger("MonitorsRS");

    public MonitorsRS() {
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMonitors(@DefaultValue("10")@QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("offset") int offset,
                                @DefaultValue("") @QueryParam("filter") String filter,
                                @DefaultValue("") @QueryParam("filterName") String filterName){
        try {
            int nextOffset=offset+limit, previousOffset= (offset-limit < 1) ? 0 : offset-limit;
            log.info(String.format("GET Monitors: limit=%d offset=%d filter=%s filterName=%s", limit, offset, filter, filterName));
            MetaPagination metaPagination =new MetaPagination(limit, offset,
                    Factory.getInstance().getMonitorDAO().getCountMonitors(filter,filterName),
                    String.format("?limit=%d&offset=%d", limit, nextOffset),
                    String.format("?limit=%d&offset=%d", limit, previousOffset)
                    );
            ArrayList<Monitor> products= (ArrayList<Monitor>) Factory.getInstance().getMonitorDAO().selectListWithOffset(offset,limit,filter,filterName);
            return Response.ok(toJson(new ProductListJson<Monitor>(metaPagination,products))).build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseFailJson();
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException",e);
            return responseFailJson();
        }
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id: \\d+}")
    public Response getMonitor(@PathParam("id") int id){
        try {
            log.info("GET ById: " + id);
            return Response.ok(toJson(Factory.getInstance().getMonitorDAO().selectMonitorById(id))).build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseBadRequest("ExceptionDAO");
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException",e);
            return responseBadRequest("JsonProcessingException");
        }
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createMonitor(@DefaultValue("none") @FormDataParam("json") String json) {
        try {
            log.info("CREATE: " + json);
            Monitor monitor = new ObjectMapper().readValue(json, Monitor.class);
            Factory.getInstance().getMonitorDAO().createMonitor(monitor);
            return this.responseSuccessJson();

        } catch (JsonParseException e) {
            log.error("JsonParseException",e);
            return responseFailJson();
        } catch (JsonMappingException e) {
            log.error("JsonMappingException",e);
            return responseFailJson();
        } catch (IOException e) {
            log.error("IOException",e);
            return responseFailJson();
        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseFailJson();
        }
    }

    @PUT
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id: \\d+}")
    public Response updateMonitor(@DefaultValue("none") @FormDataParam("json") String json) {
        try {
            log.info("UPDATE: " + json);
            Monitor monitor = new ObjectMapper().readValue(json, Monitor.class);
            Factory.getInstance().getMonitorDAO().updateMonitor(monitor);
            return this.responseSuccessJson();

        } catch (JsonParseException e) {
            log.error("JsonParseException",e);
            return responseFailJson();
        } catch (JsonMappingException e) {
            log.error("JsonMappingException",e);
            return responseFailJson();
        } catch (IOException e) {
            log.error("IOException",e);
            return responseFailJson();
        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseFailJson();
        }
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{id: \\d+}")
    public Response deleteMonitor(@PathParam("id") int id){
        try {
            log.info("DELETE: " + id);
            Factory.getInstance().getMonitorDAO().deleteMonitorById(id);
            return this.responseSuccessJson();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseFailJson();
        }
    }

    // ** utilities

    private Response responseBadRequest(String msg){
        return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }

    private Response responseFailJson(){
        return Response.ok("{\"success\":false}").build();
    }

    private Response responseSuccessJson(){
        return Response.ok("{\"success\":true}").build();
    }

    private String toJson(Monitor monitor) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return objectMapper.writeValueAsString(monitor);

    }

    private String toJson(ProductListJson productListJson) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(productListJson);

    }
}

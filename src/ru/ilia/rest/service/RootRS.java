package ru.ilia.rest.service;

import org.apache.log4j.Logger;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Account;
import ru.ilia.rest.model.util.Role;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by ILIA on 29.01.2017.
 */
@Path("/")
public class RootRS {

    static final Logger log = Logger.getLogger("RootRS");

    public RootRS() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/counts")
    public Response getCounts(){
        try {
            long countMonitors = Factory.getInstance().getMonitorDAO().getCountMonitors();
            long countCameras = Factory.getInstance().getCameraDAO().getCountCameras();
            log.info("Monitors: "+countMonitors+" | "+countCameras);
            return Response.ok(String.format("{\"countMonitors\":%d,\"countCameras\":%d}",countMonitors,countCameras)).build();
        } catch (ExceptionDAO exceptionDAO) {
            log.error("ExceptionDAO", exceptionDAO);
            return Response.ok("{\"countMonitors\":-1,\"countCameras\":-1}").build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/login")
    public Response login(@DefaultValue("") @QueryParam("username") String username,
                          @DefaultValue("") @QueryParam("password") String password){
        try {
            log.info("LOGIN");
            if (username.isEmpty() || password.isEmpty()) {
                log.info("username or pass empty");
                return responseAuthFailJson("field-missing");
            }
            Account account = Factory.getInstance().getAccountDAO().selectAccountByName(username);
            if(account==null){
                log.info("account null");
                return responseAuthFailJson("user-doesnt-exist");
            }
            if(!account.getPassword().equals(password)){
                log.info("pass incorrect");
                return responseAuthFailJson("password-wrong");
            }
            if(account.isOnline()){
                log.info("account already online");
                return responseAuthFailJson("password-wrong");
            }
            String token=new BigInteger(130, new SecureRandom()).toString(32).substring(7);
            String role=account.getRole().toString();
            account.setToken(token);
            account.setOnline(true);
            Factory.getInstance().getAccountDAO().updateAccount(account);
            log.info(account);

            return Response.ok(String.format("{\"authenticated\":true,\"token\":\"%s\",\"role\":\"%s\"}",token,role)).build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseAuthFailJson("password-wrong");
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/register")
    public Response register(@DefaultValue("") @QueryParam("username") String username,
                             @DefaultValue("") @QueryParam("password") String password,
                             @DefaultValue("user") @QueryParam("role") String role){
        try {
            log.info("REGISTER");
            if (username.isEmpty() || password.isEmpty()) {
                log.info("username or pass empty");
                return responseRegFailJson("field-missing");
            }
            if (Factory.getInstance().getAccountDAO().isAccountByName(username)){
                log.info("username-exists");
                return responseRegFailJson("username-exists");
            }
            Account account = new Account(username,password,false,Role.valueOf(role));
            Factory.getInstance().getAccountDAO().createAccount(account);
            log.info(account);

            return Response.ok("{\"registered\":true}").build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseRegFailJson("username-exists");
        } catch (IllegalArgumentException e){
            log.error("Role isn't correct", e);
            return responseRegFailJson("field-missing");
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/logout")
    public Response logout(@DefaultValue("") @QueryParam("token") String token){
        try {
            log.info("LOGOUT");
            if(token.isEmpty()){
                log.info("token empty");
                return responseBadRequest("empty token");
            }
            Account account=Factory.getInstance().getAccountDAO().selectAccountByToken(token);
            if(account==null){
                log.info("account empty");
                return responseBadRequest("null account");
            }
            account.setOnline(false);
            account.setToken("");
            Factory.getInstance().getAccountDAO().updateAccount(account);
            log.info(account);

            return Response.ok("{\"logout\": true}").build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseBadRequest("exceptionDAO");
        }
    }

    private Response responseAuthFailJson(String type){
        return Response.ok(String.format("{\"authenticated\":false,\"error\":{\"type\":\"%s\"}}",type)).build();
    }
    private Response responseRegFailJson(String type){
        return Response.ok(String.format("{\"registered\":false,\"error\":{\"type\":\"%s\"}}",type)).build();
    }
    private Response responseBadRequest(String msg){
        return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }
}

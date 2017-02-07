package ru.ilia.rest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.log4j.Logger;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.exception.ExceptionLoadImage;
import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Account;
import ru.ilia.rest.model.entity.Camera;
import ru.ilia.rest.model.util.Config;
import ru.ilia.rest.model.util.Role;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Здесь содержутся методы отвечающте за заботу с аккаунтом: login, register, logout;
 * Метод, который отдает количество мониторов и камер.
 * path: /ws/
 * @author ILIA
 */
@Path("/")
public class RootRS {
    private static final Logger log = Logger.getLogger("RootRS");

    @Context ServletContext context;

    public RootRS() {
    }

    /**
     * Select count Monitors and Cameras in json format
     * @return json
     * */
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

    /**
     * Login in shop
     * @param username login
     * @param password pass
     * @return json fail or success; token - индитификатор пользоателя
     * */
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
            log.info(account);
            if(account==null){
                log.info("account null");
                return responseAuthFailJson("user-doesnt-exist");
            }
            if(!account.getPassword().equals(password)){
                log.info("pass incorrect");
                return responseAuthFailJson("password-wrong");
            }
            if(!account.getToken().trim().isEmpty()){
                log.info("account already online");
                return responseAuthFailJson("password-wrong");
            }
            String token= JWT.create().withClaim("role", account.getRole().name()).sign(Algorithm.HMAC256("privateKey"));
            account.setToken(token);
            Factory.getInstance().getAccountDAO().updateAccount(account);
            log.info(account);

            return Response.ok(String.format("{\"authenticated\":true,\"token\":\"%s\",\"account\": %s}",token, toJson(account))).build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseAuthFailJson("password-wrong");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException",e);
            return responseAuthFailJson("password-wrong");
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException",e);
            return responseAuthFailJson("password-wrong");
        }
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("profile/edit")
    public Response updateAccount(@DefaultValue("") @FormDataParam("username") String username,
                                  @DefaultValue("") @FormDataParam("password") String password,
                                  @DefaultValue("") @FormDataParam("account") String accountJson,
                                  @DefaultValue("") @FormDataParam("file") String file){
        try {
            log.info("UPDATE ACCOUNT");
            log.info(accountJson);
            if (username.isEmpty() || password.isEmpty()) {
                log.info("username or pass empty");
                return responseAuthFailJson("field-missing");
            }
            Account account = Factory.getInstance().getAccountDAO().selectAccountByName(username);
            log.info(account);
            if(account==null){
                log.info("account null");
                return responseAuthFailJson("user-doesnt-exist");
            }
            if(!account.getPassword().equals(password)){
                log.info("pass incorrect");
                return responseAuthFailJson("password-wrong");
            }
            Account accountFromClient=new ObjectMapper().readValue(accountJson, Account.class);
            ObjectNode objectNode=new ObjectMapper().readValue(accountJson, ObjectNode.class);
            String role=objectNode.get("role").toString();
            role=role.substring(1,role.length()-1);
            String newPassword=objectNode.get("password").toString();
            newPassword=newPassword.substring(1,newPassword.length()-1);

            /*Если с клиента приша картинка на загрузку.
            * Картика загрузится на сервер в account img будет относительный путь от сервера.
            * В ином случае account img будет строка из поля img URL на клиенте (эта строка будет из accountJson)*/
            if(!file.isEmpty()) {
                accountFromClient.setImg(this.uploadImage(file));
            }
            if(!newPassword.isEmpty()){
                accountFromClient.setPassword(newPassword);
            }else {
                accountFromClient.setPassword(account.getPassword());
            }
            accountFromClient.setRole(Role.valueOf(role));
            accountFromClient.setId(account.getId());
            accountFromClient.setToken(account.getToken());
            Factory.getInstance().getAccountDAO().updateAccount(accountFromClient);

            return Response.ok(String.format("{\"success\":true,\"account\": %s}", toJson(accountFromClient))).build();

        } catch (ExceptionDAO exceptionDAO) {
            log.error("exceptionDAO",exceptionDAO);
            return responseAuthFailJson("password-wrong");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException",e);
            return responseAuthFailJson("password-wrong");
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException",e);
            return responseAuthFailJson("password-wrong");
        } catch (IOException e) {
            log.error("IOException",e);
            return responseAuthFailJson("password-wrong");
        } catch (ExceptionLoadImage exceptionLoadImage) {
            log.error("ExceptionLoadImage",exceptionLoadImage);
            return responseAuthFailJson("password-wrong");
        }
    }

    /**
     * Register in shop
     * @param username login
     * @param password pass
     * @param role user role
     * @return json fail or success
     * */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/register")
    public Response register(@DefaultValue("") @QueryParam("username") String username,
                             @DefaultValue("") @QueryParam("password") String password,
                             @DefaultValue("") @QueryParam("name") String name,
                             @DefaultValue("") @QueryParam("email") String email,
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
            Account account = new Account(username,password,name,email,Role.valueOf(role));
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

    /**
     * Logout
     * @param token
     * @return json fail or success
     * */
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
//            account.setOnline(false);
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
    private Response responseUpdateFailJson(String type){
        return Response.ok(String.format("{\"success\":false,\"error\":{\"type\":\"%s\"}}",type)).build();
    }
    private Response responseBadRequest(String msg){
        return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
    }

    private String toJson(Account account) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(account);

    }

    /**
     * Method load on server image from base64
     *
     * @param file it is serialized class File (js: input.files[0]) from client js(<input type="file" .../>)
     *             format like: data:image/jpeg;base64,/9j/4QAYRXh...(base64)
     * @return String relative path to image on server (path like: /dist/public/image.png)
     * */
    /*Если деплоить проект через IDE то context.getRealPath("/") будет null, а картинки создадутся в tomcat/bin*/
    private String uploadImage(String file) throws ExceptionLoadImage {
        try {
            /*file format:  data:image/png;base64,{base64code}
            * where {base64code} is image in base64 */
            String[] fileSplit = file.split(",");
            int indexStart = fileSplit[0].indexOf("/");
            int indexEnd = fileSplit[0].indexOf(";");
            String expansion = fileSplit[0].substring(indexStart + 1, indexEnd);
            String nameFile = UUID.randomUUID().toString();
            String pathImgOnServer= Config.PATH_IMG + nameFile + "." + expansion;
            File imgFile = new File(context.getRealPath("/") + pathImgOnServer);
            imgFile.getParentFile().mkdirs();
            imgFile.createNewFile();
            log.info("Img create: " + imgFile.getPath());

            byte[] bytes = Base64.decode(fileSplit[1]);
            FileOutputStream fileOut = new FileOutputStream(imgFile);
            fileOut.write(bytes);
            fileOut.close();

            return pathImgOnServer;
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
            throw new ExceptionLoadImage("FileNotFoundException");
        } catch (IOException e) {
            log.error("IOException", e);
            throw new ExceptionLoadImage("IOException");
        }
    }
}

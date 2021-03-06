package ru.ilia.rest.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sun.jersey.core.util.Base64;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.dao.DAO;
import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Account;
import ru.ilia.rest.model.entity.Camera;
import ru.ilia.rest.model.entity.Monitor;
import ru.ilia.rest.model.util.Role;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ILIA on 03.02.2017.
 */
public class Test {
    public static void main(String[] args) {
        try {
//            StringBuffer file=new StringBuffer();
//            file.append("");
//            byte[] bytes= Base64.decode(file.toString());
//            FileOutputStream fileOut=new FileOutputStream("fileImg.png");
//            fileOut.write(bytes);
//            fileOut.close();
//            String token = JWT.create().withClaim("role","admin").sign(Algorithm.HMAC256("secret"));
//            System.out.println(token);

            for (int i=1; i<25; i++){
                Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Monitor"+i,23,50+i,"/dist/public/monitor-1.jpg","Monitor Description#"+i));
            }
            for (int i=25; i<50; i++){
                Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Monitor"+i,27,50+i,"/dist/public/monitor-1.jpg","Monitor Description#"+i));
            }
            for (int i=1; i<25; i++){
                Factory.getInstance().getCameraDAO().createCamera(new Camera("Camera"+i,18,70+i,"/dist/public/camera-1.jpg","Camera Description#"+i));
            }
            for (int i=25; i<59; i++){
                Factory.getInstance().getCameraDAO().createCamera(new Camera("Camera"+i,24,70+i,"/dist/public/camera-1.jpg","Camera Description#"+i));
            }


            Factory.getInstance().getAccountDAO().createAccount(new Account("1","$2a$10$MQAAAAAAAAAAAAAAAAAAA.8iX7Z1kXoAWhOQtH5LCCTxha5DQ1442","Name","Email",Role.admin));
        } catch (ExceptionDAO exceptionDAO) {
            exceptionDAO.printStackTrace();
        }
    }
}

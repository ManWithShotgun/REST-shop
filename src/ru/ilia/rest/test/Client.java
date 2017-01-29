package ru.ilia.rest.test;

import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Account;
import ru.ilia.rest.model.entity.Camera;
import ru.ilia.rest.model.entity.Monitor;
import ru.ilia.rest.model.util.Role;

/**
 * Created by ILIA on 27.01.2017.
 */
public class Client {
    public static void main(String[] args) {
        try {
//            for (int i=1;i<26;i++){
//                Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Monitor"+i,24,500+i, "/dist/public/monitor-1.jpg","Description#"+i));
//            }
//            for (int i=26;i<51;i++){
//                Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Monitor"+i,18,500+i, "/dist/public/monitor-1.jpg","Description#"+i));
//            }
//            for (int i=1;i<26;i++){
//                Factory.getInstance().getCameraDAO().createCamera(new Camera("Camera"+i,8,200+i, "/dist/public/camera-1.jpg","Description#"+i));
//            }
//            for (int i=26;i<51;i++){
//                Factory.getInstance().getCameraDAO().createCamera(new Camera("Camera"+i,14,200+i, "/dist/public/camera-1.jpg","Description#"+i));
//            }
//
//            Factory.getInstance().getAccountDAO().createAccount(new Account("1", "1", false, Role.admin));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

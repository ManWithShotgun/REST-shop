package ru.ilia.rest.test;

import ru.ilia.rest.model.dao.Factory;
import ru.ilia.rest.model.entity.Monitor;

/**
 * Created by ILIA on 27.01.2017.
 */
public class Client {
    public static void main(String[] args) {
        try {
            for (int i=0;i<25;i++){
                Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Name"+i,24,500+i, "/dist/public/monitor-1.jpg","Description#"+i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

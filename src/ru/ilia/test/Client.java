package ru.ilia.test;

import ru.ilia.model.dao.Factory;
import ru.ilia.model.entity.Account;
import ru.ilia.model.entity.Monitor;
import ru.ilia.model.entity.Role;

/**
 * Created by ILIA on 27.01.2017.
 */
public class Client {
    public static void main(String[] args) {
        try {
            Factory.getInstance().getMonitorDAO().createMonitor(new Monitor("Name1",23,555,"Description1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

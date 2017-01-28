package ru.ilia.model.dao;

/**
 * Created by ILIA on 27.01.2017.
 */
public class Factory {
    private static AccountDAO accountDAO=null;
    private static MonitorDAO monitorDAO=null;
    private static Factory instance = null;

    public static synchronized Factory getInstance(){
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }

    public AccountDAO getAccountDAO(){
        if(accountDAO==null){
            accountDAO=new AccountDAO();
        }
        return accountDAO;
    }

    public MonitorDAO getMonitorDAO(){
        if(monitorDAO==null){
            monitorDAO=new MonitorDAO();
        }
        return monitorDAO;
    }
}

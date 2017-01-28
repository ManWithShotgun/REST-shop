package ru.ilia.rest.model.dao;

import ru.ilia.soap.impls.ServicePrice;
import ru.ilia.soap.impls.ServicePriceImplService;

/**
 * Created by ILIA on 27.01.2017.
 */
public class Factory {
    private static AccountDAO accountDAO=null;
    private static MonitorDAO monitorDAO=null;
    private static CameraDAO cameraDAO=null;
    private static Factory instance = null;
    private static ServicePrice servicePrice=null;

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

    public CameraDAO getCameraDAO(){
        if(cameraDAO==null){
            cameraDAO=new CameraDAO();
        }
        return cameraDAO;
    }



    public ServicePrice getServicePrice(){
        if(servicePrice==null){
            ServicePriceImplService servicePriceImplService=new ServicePriceImplService();
            servicePrice=servicePriceImplService.getServicePriceImplPort();
        }
        return servicePrice;
    }
}

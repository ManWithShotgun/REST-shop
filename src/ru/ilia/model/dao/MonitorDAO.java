package ru.ilia.model.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import ru.ilia.model.entity.Monitor;

import java.util.List;

/**
 * Created by ILIA on 27.01.2017.
 */
public class MonitorDAO extends DAO{

    public Monitor createMonitor(Monitor monitor) throws Exception {
        try {
            //create price by id
            monitor.setIdPrice(1);
            begin();
            getSession().save(monitor);
            commit();
            return monitor;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("DAO error: "+e);
        }
    }

    public Monitor selectMonitorById(long id) throws Exception {
        try {
            begin();
            Query q = getSession().createQuery("from Monitor where id_monitor = :id");
            q.setLong("id", id);
            Monitor monitor = (Monitor) q.uniqueResult();
            commit();
            //select price by id
            monitor.setPrice(123);
            return monitor;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("DAO error: " + e);
        }
    }

    public void updateMonitor(Monitor monitor){
        //update price by id
        begin();
        getSession().update(monitor);
        commit();
    }

    public void deleteMonitor(Monitor monitor){
        //delete price by id
        begin();
        getSession().delete(monitor);
        commit();
    }

    public boolean deleteMonitorById(long id){
        //delete price by id
        int result;
        begin();
        Query q = getSession().createQuery("delete from Monitor where id_monitor = :id");
        q.setLong("id", id);
        result=q.executeUpdate();
        commit();
        return result==1;
    }

    public List<Monitor> selectListWithOffset(int offset, int limit, String filer, String filterName){
        List<Monitor> result;
        begin();
        Criteria criteria=getSession().createCriteria(Monitor.class);
        if(!filer.isEmpty()){
            criteria=criteria.add(Restrictions.eq("inch",filer));
        }
        if(!filterName.isEmpty()) {
            criteria = criteria.add(Restrictions.like("name", filterName));
        }
        criteria=criteria.setFirstResult(offset).setMaxResults(limit);
        result=criteria.list();
        commit();
        //select price by id for each
        return result;
    }
}

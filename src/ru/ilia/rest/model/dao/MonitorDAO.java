package ru.ilia.rest.model.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.entity.Monitor;
import ru.service_shop.Price;
import ru.service_shop.PriceList;
import ru.service_shop.PriceListRequest;

import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by ILIA on 27.01.2017.
 */
public class MonitorDAO extends DAO{
    private static final Logger log = Logger.getLogger("MonitorDAO");

    public Monitor createMonitor(Monitor monitor) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("create Price: "+monitor.getPrice());
            /*Создаем цену через soap и связываем id с продуктом*/
            Price price=Factory.getInstance().getServicePrice().createPrice(monitor.getPrice());
            monitor.setIdPrice(price.getId());
            log.info(monitor);
            session.save(monitor);
            commit(session);
            return monitor;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("create monitor error");
        }
//        if(monitor.getId()==0){
//            throw new ExceptionDAO("create monitor error");
//        }
    }

    public Monitor selectMonitorById(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("id: "+id);
            Query q = session.createQuery("from Monitor where id_monitor = :id");
            q.setLong("id", id);
            Monitor monitor = (Monitor) q.uniqueResult();
            commit(session);
            /*Запрос от SOAP цены по id*/
            Price price=Factory.getInstance().getServicePrice().selectPrice(monitor.getIdPrice());
            monitor.setPrice(price.getPrice());
            log.info(monitor);
            return monitor;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select monitor error");
        }
    }

    public void updateMonitor(Monitor monitor) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info(monitor);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromMonitorId(monitor.getId());
            monitor.setIdPrice(idPrice);
            /*По полученому id_price отдаем SOAP для обновы*/
            Factory.getInstance().getServicePrice().updatePrice(new Price(idPrice,monitor.getPrice()));
            session.update(monitor);
            commit(session);
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("update monitor error");
        }
    }

    /*Оказалось, что этот метод не нужен, его заменил эквивалентный с id. Но пусть остается, может найду применение:)*/
    public void deleteMonitor(Monitor monitor) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info(monitor);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromMonitorId(monitor.getId());
            /*По полученому id_price отдаем SOAP для удаления*/
            Factory.getInstance().getServicePrice().deletePrice(idPrice);
            session.delete(monitor);
            commit(session);
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete monitor error");
        }
    }

    public boolean deleteMonitorById(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("id: "+id);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromMonitorId(id);
            /*По полученому id_price отдаем SOAP для удаления*/
            Factory.getInstance().getServicePrice().deletePrice(idPrice);
            int result;
            Query q = session.createQuery("delete from Monitor where id_monitor = :id");
            q.setLong("id", id);
            result = q.executeUpdate();
            commit(session);
            return result == 1;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete monitor by id error");
        }
    }

    public List<Monitor> selectListWithOffset(int offset, int limit, String filer, String filterName) throws ExceptionDAO {
        Session session=begin();
        try {
            List<Monitor> result;
            Criteria criteria = session.createCriteria(Monitor.class);
            if (!filer.isEmpty()) {
                criteria = criteria.add(Restrictions.eq("inch", Integer.parseInt(filer)));
            }
            if (!filterName.isEmpty()) {
                criteria = criteria.add(Restrictions.like("name",filterName, MatchMode.ANYWHERE));
            }
            criteria = criteria.setFirstResult(offset).setMaxResults(limit);
            result = criteria.list();
            commit(session);

            PriceListRequest priceListRequest=new PriceListRequest();
            /*Формируется список id_price для запроса на SOAP*/
            for (Monitor m : result){
                priceListRequest.getIdList().add(m.getIdPrice());
            }
            /*Забирается список цен*/
            PriceList priceList=Factory.getInstance().getServicePrice().selectList(priceListRequest);
            /*Раздаем цены товарам по порядку и молимся, что цены пришли в том же порядке что и id в запросе.
            На локалке все корректно. Если в БД не будет цены по id_price, то SOAP вернет мельше элементов.
            Возможно стоит сделать проверку по limit и бросать ошибку, но с корректной БД все будет ок.*/
            for (int i=0;i<priceList.getPriceList().size();i++){
                result.get(i).setPrice(priceList.getPriceList().get(i).getPrice());
                log.info(result.get(i));
            }
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage(),e);
            rollback(session);
            throw new ExceptionDAO("select list monitors error");
        }
    }

    public long getCountMonitors(String filer, String filterName) throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Criteria criteria = session.createCriteria(Monitor.class);
            if (!filer.isEmpty()) {
                criteria = criteria.add(Restrictions.eq("inch", Integer.parseInt(filer)));
            }
            if (!filterName.isEmpty()) {
                criteria = criteria.add(Restrictions.like("name",filterName, MatchMode.ANYWHERE));
            }
            result = (long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            commit(session);
            log.info(result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("getCountMonitors");
        }
    }

    public long getCountMonitors() throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Criteria criteria = session.createCriteria(Monitor.class);
            result = (long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            commit(session);
            log.info(result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("getCountMonitors");
        }
    }

    // ** privates
    private long getIdPriceFromMonitorId(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Query q = session.createQuery("select idPrice from Monitor where id_monitor = :id");
            q.setLong("id", id);
            result = (Long) q.uniqueResult();
            commit(session);
            log.info("idPrice: "+result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("getIdPriceFromMonitorId");
        }
    }
}

package ru.ilia.rest.model.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.entity.Camera;
import ru.service_shop.Price;
import ru.service_shop.PriceList;
import ru.service_shop.PriceListRequest;

import java.util.List;

/**
 * Created by ILIA on 28.01.2017.
 */
public class CameraDAO extends DAO {
    static final Logger log = Logger.getLogger("CameraDAO");

    public Camera createCamera(Camera camera) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("create Price: "+camera.getPrice());
            /*Создаем цену через soap и связываем id с продуктом*/
            Price price=Factory.getInstance().getServicePrice().createPrice(camera.getPrice());
            camera.setIdPrice(price.getId());
            log.info(camera);
            session.save(camera);
            commit(session);
            return camera;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("create camera error");
        }
//        if(camera.getId()==0){
//            throw new ExceptionDAO("create camera error");
//        }
    }

    public Camera selectCameraById(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("id: "+id);
            Query q = session.createQuery("from Camera where id_camera = :id");
            q.setLong("id", id);
            Camera camera = (Camera) q.uniqueResult();
            commit(session);
            /*Запрос от SOAP цены по id*/
            Price price=Factory.getInstance().getServicePrice().selectPrice(camera.getIdPrice());
            camera.setPrice(price.getPrice());
            log.info(camera);
            return camera;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select camera error");
        }
    }

    public void updateCamera(Camera camera) throws Exception {
        Session session=begin();
        try {
            log.info(camera);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromCameraId(camera.getId());
            camera.setIdPrice(idPrice);
            /*По полученому id_price отдаем SOAP для обновы*/
            Factory.getInstance().getServicePrice().updatePrice(new Price(idPrice,camera.getPrice()));
            session.update(camera);
            commit(session);
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("update camera error");
        }
    }

    /*Оказалось, что этот метод не нужен, его заменил эквивалентный с id. Но пусть остается, может найду применение:)*/
    public void deleteCamera(Camera camera) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info(camera);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromCameraId(camera.getId());
            /*По полученому id_price отдаем SOAP для удаления*/
            Factory.getInstance().getServicePrice().deletePrice(idPrice);
            session.delete(camera);
            commit(session);
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete camera error");
        }
    }

    public boolean deleteCameraById(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("id: "+id);
            /*Получаем id_price из таблицы с продуктом*/
            long idPrice=this.getIdPriceFromCameraId(id);
            /*По полученому id_price отдаем SOAP для удаления*/
            Factory.getInstance().getServicePrice().deletePrice(idPrice);
            int result;
            Query q = session.createQuery("delete from Camera where id_camera = :id");
            q.setLong("id", id);
            result = q.executeUpdate();
            commit(session);
            return result == 1;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete camera by id error");
        }
    }

    public List<Camera> selectListWithOffset(int offset, int limit, String filer, String filterName) throws ExceptionDAO {
        Session session=begin();
        try {
            List<Camera> result;
            Criteria criteria = session.createCriteria(Camera.class);
            if (!filer.isEmpty()) {
                criteria = criteria.add(Restrictions.eq("MP", Integer.parseInt(filer)));
            }
            if (!filterName.isEmpty()) {
                criteria = criteria.add(Restrictions.like("name",filterName, MatchMode.ANYWHERE));
            }
            criteria = criteria.setFirstResult(offset).setMaxResults(limit);
            result = criteria.list();
            commit(session);

            PriceListRequest priceListRequest=new PriceListRequest();
            /*Формируется список id_price для запроса на SOAP*/
            for (Camera m : result){
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
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select list cameras error");
        }
    }

    public long getCountCameras(String filer, String filterName) throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Criteria criteria = session.createCriteria(Camera.class);
            if (!filer.isEmpty()) {
                criteria = criteria.add(Restrictions.eq("MP", Integer.parseInt(filer)));
            }
            if (!filterName.isEmpty()) {
                criteria = criteria.add(Restrictions.like("name",filterName, MatchMode.ANYWHERE));
            }
            result = (long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            commit(session);
            log.info(result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage(),e);
            rollback(session);
            throw new ExceptionDAO("getCountCameras");
        }
    }

    public long getCountCameras() throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Criteria criteria = session.createCriteria(Camera.class);
            result = (long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            commit(session);
            log.info(result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage(),e);
            rollback(session);
            throw new ExceptionDAO("getCountCameras");
        }
    }

    // ** privates
    private long getIdPriceFromCameraId(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            long result;
            Query q = session.createQuery("select idPrice from Camera where id_camera = :id");
            q.setLong("id", id);
            result = (Long) q.uniqueResult();
            commit(session);
            log.info("idPrice: "+result);
            return result;
        }catch (HibernateException e){
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("getIdPriceFromCameraId");
        }
    }
}

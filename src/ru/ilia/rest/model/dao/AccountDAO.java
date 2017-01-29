package ru.ilia.rest.model.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.ilia.rest.exception.ExceptionDAO;
import ru.ilia.rest.model.entity.Account;

/**
 * Created by ILIA on 27.01.2017.
 */
public class AccountDAO extends DAO {

    public Account createAccount(Account account) throws ExceptionDAO {
        Session session=begin();
        try {
            session.save(account);
            commit(session);
            log.info(account);
            return account;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("create account error");
        }
    }

    public Account selectAccountById(long id) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("id: "+id);
            Query q = session.createQuery("from Account where id_account = :id");
            q.setLong("id", id);
            Account account = (Account) q.uniqueResult();
            commit(session);
            log.info(account);
            return account;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select by id account error");
        }
    }

    public Account selectAccountByName(String name) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("name: "+name);
            Criteria criteria=session.createCriteria(Account.class).add(Restrictions.eq("username", name));
            Account account = (Account) criteria.uniqueResult();
            commit(session);
            log.info(account);
            return account;//return null if not exist
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select by Username account error");
        }
    }

    public Account selectAccountByToken(String token) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info("token: "+token);
            Criteria criteria=session.createCriteria(Account.class).add(Restrictions.eq("token", token));
            Account account = (Account) criteria.uniqueResult();
            commit(session);
            log.info(account);
            return account;//return null if not exist
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("select by Token account error");
        }
    }

    public boolean isAccountByName(String name) throws ExceptionDAO {
        Session session=begin();
        try {
            long count;
            log.info("name: "+name);
            Criteria criteria=session.createCriteria(Account.class).add(Restrictions.eq("username", name));
            count=(long)criteria.setProjection(Projections.rowCount()).uniqueResult();
            commit(session);
            log.info("count: "+count);
            return count >0;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("already exist account error");
        }
    }

    public void updateAccount(Account account) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info(account);
            session.update(account);
            commit(session);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("update account error");
        }
    }

    public void deleteAccount(Account account) throws ExceptionDAO {
        Session session=begin();
        try {
            log.info(account);
            session.delete(account);
            commit(session);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete account error");
        }
    }

    public boolean deleteAccountById(long id) throws ExceptionDAO {
        int result;
        Session session=begin();
        try {
            log.info("id: "+id);
            Query q = session.createQuery("delete from Account where id_account = :id");
            q.setLong("id", id);
            result = q.executeUpdate();
            commit(session);
            return result == 1;
        } catch (HibernateException e) {
            log.error(e.getMessage());
            rollback(session);
            throw new ExceptionDAO("delete by id account error");
        }
    }

}

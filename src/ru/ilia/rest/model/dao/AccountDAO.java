package ru.ilia.rest.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import ru.ilia.rest.model.entity.Account;

/**
 * Created by ILIA on 27.01.2017.
 */
public class AccountDAO extends DAO {

    public Account createAccount(Account account) throws Exception {
        Session session=begin();
        try {
            session.save(account);
            commit(session);
            return account;
        } catch (HibernateException e) {
            rollback(session);
            throw new Exception("DAO error: "+e);
        }
    }

    public Account selectAccountById(long id) throws Exception {
        Session session=begin();
        try {
            Query q = session.createQuery("from Account where id_account = :id");
            q.setLong("id", id);
            Account account = (Account) q.uniqueResult();
            commit(session);
            return account;
        } catch (HibernateException e) {
            rollback(session);
            throw new Exception("DAO error: " + e);
        }
    }

    public void updateAccount(Account account){
        Session session=begin();
        session.update(account);
        commit(session);
    }

    public void deleteAccount(Account account){
        Session session=begin();
        session.delete(account);
        commit(session);
    }

    public boolean deleteAccountById(long id){
        int result;
        Session session=begin();
        Query q = session.createQuery("delete from Account where id_account = :id");
        q.setLong("id", id);
        result=q.executeUpdate();
        commit(session);
        return result==1;
    }

}

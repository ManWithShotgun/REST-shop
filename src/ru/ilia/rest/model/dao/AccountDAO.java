package ru.ilia.rest.model.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import ru.ilia.rest.model.entity.Account;

/**
 * Created by ILIA on 27.01.2017.
 */
public class AccountDAO extends DAO {

    public Account createAccount(Account account) throws Exception {
        try {
            begin();
            getSession().save(account);
            commit();
            return account;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("DAO error: "+e);
        }
    }

    public Account selectAccountById(long id) throws Exception {
        try {
            begin();
            Query q = getSession().createQuery("from Account where id_account = :id");
            q.setLong("id", id);
            Account account = (Account) q.uniqueResult();
            commit();
            return account;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("DAO error: " + e);
        }
    }

    public void updateAccount(Account account){
        begin();
        getSession().update(account);
        commit();
    }

    public void deleteAccount(Account account){
        begin();
        getSession().delete(account);
        commit();
    }

    public boolean deleteAccountById(long id){
        int result;
        begin();
        Query q = getSession().createQuery("delete from Account where id_account = :id");
        q.setLong("id", id);
        result=q.executeUpdate();
        commit();
        return result==1;
    }

}

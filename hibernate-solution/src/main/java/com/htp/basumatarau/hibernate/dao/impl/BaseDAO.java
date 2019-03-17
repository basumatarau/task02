package com.htp.basumatarau.hibernate.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public abstract class BaseDAO {
    private static SessionFactory sessionFactory;
    private Session currentSession;
    private Transaction currentTransaction;

    private SessionFactory getSessionFactory(){
        if(sessionFactory == null) {
            synchronized (BaseDAO.class) {
                if (sessionFactory == null) {
                    sessionFactory
                            = new Configuration().configure("hibernate.cfg.xml")
                            .buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    protected Session openCurrentSession(){
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }
    protected Session openCurrentSessionWithTransaction(){
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }
    protected void closeCurrentSession(){
        currentSession.close();
    }
    protected void closeCurrenSessionWithTransaction(){
        currentTransaction.commit();
        currentSession.close();
    }

    protected Session getCurrentSession(){
        return currentSession;
    }
    protected Transaction getCurrentTransaction(){
        return currentTransaction;
    }
}

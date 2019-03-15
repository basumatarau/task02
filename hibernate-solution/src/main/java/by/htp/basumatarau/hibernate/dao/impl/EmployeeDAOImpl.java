package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.query.Query;

import java.util.*;

public class EmployeeDAOImpl extends BaseDAO implements DAO<Employee, Integer> {

    @Override
    public boolean create(Employee entity) throws PersistenceException {
        return false;
    }

    @Override
    public Employee read(Integer id) {
        //TODO ?!
        openCurrentSession();
        Query query = getCurrentSession().createQuery(
                "from Employee emp " +
                "join fetch emp.registeredEmployees " +
                "join fetch emp.currentAddress " +
                "where emp.id=:id");
        query.setParameter("id", id);
        Object result = query.getSingleResult();
        closeCurrentSession();
        return ((Employee) result);
    }

    @Override
    public boolean delete(Employee entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Employee> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

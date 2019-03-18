package com.htp.basumatarau.hibernate.dao.impl;

import com.htp.basumatarau.hibernate.dao.DAO;
import com.htp.basumatarau.hibernate.dao.beans.Employee;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.query.Query;

import java.util.*;

public class EmployeeDAOImpl extends BaseDAO implements DAO<Employee, Integer> {

    @Override
    public boolean create(Employee entity) throws PersistenceException {
        return false;
    }

    @Override
    public Employee read(Integer id) {
        Employee result;
        try {
            openCurrentSession();
            Query query = getCurrentSession().createQuery(
                    "from Employee emp " +
                            "join fetch emp.registeredEmployees " +
                            "join fetch emp.currentAddress " +
                            "join fetch emp.currentAddress.city " +
                            "join fetch emp.currentAddress.city.country " +
                            "where emp.id=:id");
            query.setParameter("id", id);
            result = (Employee) query.getSingleResult();
        }finally {
            closeCurrentSession();
        }
        return result;
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

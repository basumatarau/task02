package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import java.util.List;

public class EmployeeDAOImpl implements DAO<Employee, Integer> {
    @Override
    public boolean create(Employee entity) throws PersistenceException {
        return false;
    }

    @Override
    public Employee read(Integer integer) throws PersistenceException {
        return null;
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

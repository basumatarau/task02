package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.Company;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import java.util.List;

public class CompanyDAOImpl implements DAO<Company, Integer> {

    @Override
    public boolean create(Company entity) throws PersistenceException {
        return false;
    }

    @Override
    public Company read(Integer integer) throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Company entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Company> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

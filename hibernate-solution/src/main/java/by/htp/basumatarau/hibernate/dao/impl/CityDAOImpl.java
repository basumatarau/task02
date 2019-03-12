package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.City;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import java.util.List;

public class CityDAOImpl implements DAO<City, Integer> {
    @Override
    public boolean create(City entity) throws PersistenceException {
        return false;
    }

    @Override
    public City read(Integer integer) throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(City entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<City> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

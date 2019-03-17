package com.htp.basumatarau.hibernate.dao.impl;

import com.htp.basumatarau.hibernate.dao.DAO;
import com.htp.basumatarau.hibernate.dao.beans.Country;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;

import java.util.List;

public class CountryDAOImpl implements DAO<Country, Integer> {

    @Override
    public boolean create(Country entity) throws PersistenceException {
        return false;
    }

    @Override
    public Country read(Integer integer) throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Country entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Country> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

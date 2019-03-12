package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.Address;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;

import java.util.List;


public class AddressDAOImpl implements DAO<Address, Integer> {

    @Override
    public boolean create(Address entity) throws PersistenceException {
        return false;
    }

    @Override
    public Address read(Integer integer) throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Address entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Address> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

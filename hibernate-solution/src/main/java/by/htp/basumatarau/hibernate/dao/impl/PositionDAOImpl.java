package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.Position;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import java.util.List;

public class PositionDAOImpl implements DAO<Position, Integer> {

    @Override
    public boolean create(Position entity) throws PersistenceException {
        return false;
    }

    @Override
    public Position read(Integer integer) throws PersistenceException {
        return null;
    }

    @Override
    public boolean delete(Position entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Position> read(int entries, int startingFrom) throws PersistenceException {
        return null;
    }
}

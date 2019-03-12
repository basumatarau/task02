package by.htp.basumatarau.jdbc.dao;

import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;

import java.io.Serializable;
import java.util.List;

public interface DAO<T, Id extends Serializable> {
    boolean create(T entity) throws PersistenceException;
    T read(Id id) throws PersistenceException;
    boolean delete(T entity) throws PersistenceException;
    List<T> read(int entries, int startingFrom) throws PersistenceException;
}

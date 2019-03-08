package by.htp.basumatarau.sql.dao;

import java.io.Serializable;
import java.util.List;

public interface DAO<T, Id extends Serializable> {
    boolean create(T entity);
    T read(Id id);
    boolean delete(T entity);
    List<T> read(int entries, int startingFrom);
}

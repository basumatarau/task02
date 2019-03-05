package by.htp.basumatarau.DAO;

import java.io.Serializable;
import java.util.List;

public interface IDAO<T, Id extends Serializable> {
    void persist(T entity);
    T read(Id id);
    List<T> lookUp(Id from, Id to);
}

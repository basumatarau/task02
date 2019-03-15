package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.RegisteredEmployee;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.query.Query;

import java.util.List;

public class RegisteredEmployeeDAOImpl
        extends BaseDAO implements DAO<RegisteredEmployee, Integer> {

    @Override
    public boolean create(RegisteredEmployee entity) throws PersistenceException {
        return false;
    }

    @Override
    public RegisteredEmployee read(Integer id) throws PersistenceException {
        openCurrentSession();

        Query query = getCurrentSession().createQuery(
                "from RegisteredEmployee emp " +
                        "join fetch emp.employee " +
                        "join fetch emp.address " +
                        "join fetch emp.company " +
                        "join fetch emp.position " +
                        "where emp.id=:id");
        query.setParameter("id", id);
        Object result = query.getSingleResult();

        closeCurrentSession();
        return ((RegisteredEmployee) result);
    }

    @Override
    public boolean delete(RegisteredEmployee entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<RegisteredEmployee> read(int entries, int startingFrom) throws PersistenceException {
        openCurrentSession();

        Query query = getCurrentSession().createQuery(
                "from RegisteredEmployee emp " +
                        "join fetch emp.employee " +
                        "join fetch emp.address " +
                        "join fetch emp.company " +
                        "join fetch emp.position " +
                        "where emp.id>:lowLimit " +
                        "and emp.id<:uppLimit " +
                        "order by emp.employee.employeeId");
        query.setParameter("lowLimit", startingFrom);
        query.setParameter("uppLimit", startingFrom + entries);
        List resultList = query.getResultList();

        closeCurrentSession();
        return ((List<RegisteredEmployee>) resultList);
    }
}

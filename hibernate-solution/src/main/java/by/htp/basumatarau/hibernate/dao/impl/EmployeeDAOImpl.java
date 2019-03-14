package by.htp.basumatarau.hibernate.dao.impl;

import by.htp.basumatarau.hibernate.dao.DAO;
import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.dto.EmployeeDetailDTO;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;

public class EmployeeDAOImpl extends BaseDAO implements DAO<Employee, Integer> {

    @Override
    public boolean create(Employee entity) {
        return false;
    }

    @Override
    public Employee read(Integer id) {
        Session currentSession = openCurrentSession();

        Query query = getCurrentSession()
                .createQuery(
                        "select emp from Employee emp " +
                                "join fetch emp.addresses " +
                                "join fetch emp.companies " +
                                "join fetch emp.positions " +
                                "where emp.employeeId=:id "
                );
        query.setParameter("id", id);
        Object singleResult = query.getSingleResult();

        closeCurrentSession();
        return ((Employee) singleResult);
    }

    public List<EmployeeDetailDTO> getEmployeeDTO(int numEntries, int startingFrom) {
        List<Employee> employees
                = openCurrentSession()
                .createQuery("from Employee as emp order by emp.employeeId ", Employee.class)
                .setFirstResult(startingFrom)
                .setMaxResults(numEntries)
                .getResultList();

        ArrayList<EmployeeDetailDTO> result = new ArrayList<>();
        for (Employee employee : employees) {
            result.add(EmployeeDetailDTO.makeDTO(employee));
        }
        closeCurrentSession();
        return result;
    }

    @Override
    public boolean delete(Employee entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<Employee> read(int entries, int startingFrom) throws PersistenceException {
        List result = openCurrentSession()
                .createQuery(
                        "select emp from Employee emp " +
                        "join fetch emp.addresses " +
                        "join fetch emp.companies " +
                        "join fetch emp.positions " +
                        "order by emp.employeeId")
                .setFirstResult(startingFrom)
                .setMaxResults(entries)
                .getResultList();

        closeCurrentSession();
        return result;
    }

}

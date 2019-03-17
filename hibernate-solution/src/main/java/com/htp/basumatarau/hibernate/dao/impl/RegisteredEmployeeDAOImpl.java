package com.htp.basumatarau.hibernate.dao.impl;

import com.htp.basumatarau.hibernate.dao.DAO;
import com.htp.basumatarau.hibernate.dao.beans.RegisteredEmployee;
import com.htp.basumatarau.hibernate.dao.dto.EmployeeDTO;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
                        "join fetch emp.employee.currentAddress " +
                        "join fetch emp.employee.currentAddress.city " +
                        "join fetch emp.employee.currentAddress.city.country " +
                        "join fetch emp.address " +
                        "join fetch emp.address.city " +
                        "join fetch emp.address.city.country " +
                        "join fetch emp.company " +
                        "where emp.employee.employeeId>:lowLimit " +
                        "and emp.employee.employeeId<=:uppLimit " +
                        "order by emp.employee.employeeId");
        query.setParameter("lowLimit", startingFrom);
        query.setParameter("uppLimit", startingFrom + entries);
        List resultList = query.getResultList();

        closeCurrentSession();
        return ((List<RegisteredEmployee>) resultList);
    }

    public List<EmployeeDTO> readDTO(int entries, int startingFrom) throws PersistenceException {
        openCurrentSession();
        //TODO numOfficeStuff query fix
        Query<EmployeeDTO> query = getCurrentSession().createQuery(
                "select new com.htp.basumatarau.hibernate.dao.dto.EmployeeDTO(" +
                        "emp.employee.firstName, " +
                        "emp.employee.lastName, " +
                        "emp.employee.currentAddress.address, " +
                        "emp.employee.currentAddress.city.city, " +
                        "emp.employee.currentAddress.city.country.country, " +
                        "emp.company.name, " +
                        "emp.address.address, " +
                        "emp.address.city.city, " +
                        "emp.address.city.country.country, " +
                        "9999, " +
                        "emp.jobPosition" +
                        ") from RegisteredEmployee emp " +
                        "where emp.employee.employeeId>:lowLimit " +
                        "and emp.employee.employeeId<=:uppLimit " ,
                EmployeeDTO.class);

        query.setParameter("lowLimit", 10);
        query.setParameter("uppLimit", 10 + 10);
        List<EmployeeDTO> resultList = query.getResultList();
        closeCurrentSession();
        return resultList;
    }

}

package com.htp.basumatarau.hibernate.dao.impl;

import com.htp.basumatarau.hibernate.dao.DAO;
import com.htp.basumatarau.hibernate.dao.beans.RegisteredEmployee;
import com.htp.basumatarau.hibernate.dao.dto.EmployeeDetailDTO;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

public class RegisteredEmployeeDAOImpl
        extends BaseDAO implements DAO<RegisteredEmployee, Integer> {

    @Override
    public boolean create(RegisteredEmployee entity) throws PersistenceException {
        return false;
    }

    @Override
    public RegisteredEmployee read(Integer id) throws PersistenceException {
        RegisteredEmployee result;
        try{
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
                            "where emp.employee.employeeId=:id ");
            query.setParameter("id", id);
            result = (RegisteredEmployee) query.getSingleResult();
        }finally {
            closeCurrentSession();
        }
        return result;
    }

    @Override
    public boolean delete(RegisteredEmployee entity) throws PersistenceException {
        return false;
    }

    @Override
    public List<RegisteredEmployee> read(int entries, int startingFrom) throws PersistenceException {
        List<RegisteredEmployee> resultList;
        try {
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
            resultList = (List<RegisteredEmployee>) query.getResultList();
        }finally {
            closeCurrentSession();
        }
        return resultList;
    }

    public List<EmployeeDetailDTO> readDTO(int entries, int startingFrom) throws PersistenceException {
        ArrayList<EmployeeDetailDTO> resultList;
        try {
            openCurrentSession();
            Query<Tuple> query = getCurrentSession().createQuery(
                    "select " +
                            "emp.employee.firstName as firstName, " +
                            "emp.employee.lastName as lastName, " +
                            "emp.employee.currentAddress.address as currentAddress, " +
                            "emp.employee.currentAddress.city.city as currentCity, " +
                            "emp.employee.currentAddress.city.country.country as currentCountry, " +
                            "emp.company.name as companyName, " +
                            "emp.address.address as companyAddress, " +
                            "emp.address.city.city as companyCity, " +
                            "emp.address.city.country.country as companyCountry, " +
                            "count(distinct emp.employee.employeeId) as officeStaff, " +
                            "emp.jobPosition as jobPosition " +
                            "from RegisteredEmployee emp " +
                            "group by emp.company.companyId, emp.address.address.id " +
                            "order by emp.employee.employeeId ",
                    Tuple.class);
            query.setFirstResult(startingFrom);
            query.setMaxResults(entries);
            resultList = new ArrayList<>();
            for (Tuple tuple : query.getResultList()) {
                String firstName = (String) tuple.get("firstName");
                String lastName = (String) tuple.get("lastName");
                String currentAddress = (String) tuple.get("currentAddress");
                String currentCity = (String) tuple.get("currentCity");
                String currentCountry = (String) tuple.get("currentCountry");
                String companyName = (String) tuple.get("companyName");
                String companyAddress = (String) tuple.get("companyAddress");
                String companyCity = (String) tuple.get("companyCity");
                String companyCountry = (String) tuple.get("companyCountry");
                Long officeStaff = (Long) tuple.get("officeStaff");
                String jobPosition = (String) tuple.get("jobPosition");
                resultList.add(
                        new EmployeeDetailDTO(firstName,
                                lastName,
                                currentAddress,
                                currentCity,
                                currentCountry,
                                companyName,
                                companyAddress,
                                companyCity,
                                companyCountry,
                                officeStaff,
                                jobPosition)
                );
            }
        }finally {
            closeCurrentSession();
        }

        return resultList;
    }

}

package com.htp.basumtarau.solution02;

import com.htp.basumatarau.hibernate.dao.beans.Employee;
import com.htp.basumatarau.hibernate.dao.beans.RegisteredEmployee;
import com.htp.basumatarau.hibernate.dao.dto.EmployeeDTO;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import com.htp.basumatarau.hibernate.dao.impl.EmployeeDAOImpl;
import com.htp.basumatarau.hibernate.dao.impl.RegisteredEmployeeDAOImpl;
import org.junit.Test;

import java.util.List;

public class DAOTest02 {

    @Test
    public void employeeDAOTest01() throws PersistenceException {
        RegisteredEmployee empEntry = new RegisteredEmployeeDAOImpl().read(1);
        System.out.print(empEntry.getEmployee().getFirstName() + " ");
        System.out.print(empEntry.getEmployee().getLastName() + " ");
        System.out.print(empEntry.getAddress().getAddress());
    }

    @Test
    public void employeeDAOTest02() throws PersistenceException {
        List<RegisteredEmployee> result = new RegisteredEmployeeDAOImpl().read(10, 10);
        for (RegisteredEmployee empEntry : result) {
            System.out.print(empEntry.getEmployee().getEmployeeId() + " ");
            System.out.print(empEntry.getEmployee().getFirstName() + " ");
            System.out.print(empEntry.getEmployee().getLastName() + " ");
            System.out.print(empEntry.getEmployee().getCurrentAddress().getAddress() + " ");
            System.out.print(empEntry.getEmployee().getCurrentAddress().getCity().getCity() + " ");
            System.out.print(empEntry.getEmployee().getCurrentAddress().getCity().getCountry().getCountry() + " ");
            System.out.print(empEntry.getCompany().getName() + " ");
            System.out.print(empEntry.getAddress().getCity().getCity() + " ");
            System.out.print(empEntry.getAddress().getCity().getCountry().getCountry() + " ");
            System.out.print(empEntry.getAddress().getAddress() + " ");
            System.out.print(empEntry.getJobPosition() + "\n");
        }
    }


    //too lazy to be tested...
    @Test
    public void employeeDAOTest03() throws PersistenceException {
        Employee empEntry = new EmployeeDAOImpl().read(1);
        System.out.print(empEntry.getFirstName() + " ");
        System.out.print(empEntry.getLastName() + " ");
        System.out.print(empEntry.getCurrentAddress().getAddress() + " \n");
        for (RegisteredEmployee regEmp : empEntry.getRegisteredEmployees()) {
            System.out.print(regEmp.getCompany().getName() + " ");
            System.out.print(regEmp.getAddress().getCity().getCity() + " ");
            System.out.print(regEmp.getAddress().getCity().getCountry().getCountry() + " ");
            System.out.print(regEmp.getAddress().getAddress() + " ");
            System.out.print(regEmp.getJobPosition() + "\n");
        }
    }


    @Test
    public void employeeDAOTest05() throws PersistenceException {
        List<EmployeeDTO> resultList
                = new RegisteredEmployeeDAOImpl().readDTO(10, 10);
        for (EmployeeDTO dto : resultList) {
            System.out.println(dto);
        }
    }

}

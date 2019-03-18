package com.htp.basumtarau.solution02;

import com.htp.basumatarau.hibernate.dao.beans.Employee;
import com.htp.basumatarau.hibernate.dao.beans.RegisteredEmployee;
import com.htp.basumatarau.hibernate.dao.dto.EmployeeDetailDTO;
import com.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import com.htp.basumatarau.hibernate.dao.impl.EmployeeDAOImpl;
import com.htp.basumatarau.hibernate.dao.impl.RegisteredEmployeeDAOImpl;
import org.junit.Test;

import java.util.List;

public class DAOTest02 {

    //too lazy to be tested...
    @Test
    public void employeeDAOTest03() throws PersistenceException {
        Employee empEntry = new EmployeeDAOImpl().read(1);
        System.out.println(empEntry);
    }

    @Test
    public void employeeDAOTest01() throws PersistenceException {
        RegisteredEmployee empEntry = new RegisteredEmployeeDAOImpl().read(77);
        System.out.println(empEntry);
    }

    @Test
    public void employeeDAOTest02() throws PersistenceException {
        List<RegisteredEmployee> result = new RegisteredEmployeeDAOImpl().read(100, 0);
        for (RegisteredEmployee empEntry : result) {
            System.out.println(empEntry);
        }
    }

    @Test
    public void employeeDAOTest05() throws PersistenceException {
        List<EmployeeDetailDTO> resultList
                = new RegisteredEmployeeDAOImpl().readDTO(100, 0);
        for (EmployeeDetailDTO dto : resultList) {
            System.out.println(dto);
        }
    }

}

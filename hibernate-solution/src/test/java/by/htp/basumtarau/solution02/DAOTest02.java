package by.htp.basumtarau.solution02;

import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import by.htp.basumatarau.hibernate.dao.impl.EmployeeDAOImpl;
import by.htp.basumatarau.hibernate.dao.util.TupleOfSix;
import by.htp.basumatarau.hibernate.dao.util.TupleOfTwo;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DAOTest02 {

    @Test
    public void EmployeeQueryTest() throws PersistenceException {
        for (Map.Entry<TupleOfTwo<Employee, Address>,
                List<TupleOfSix<Company, City, Country, Address, Integer, Position>>>
                entry
                : new EmployeeDAOImpl().getDetailed(100, 0).entrySet()) {

            Employee employee = entry.getKey().one;
            int employeeId =employee.getEmployeeId();
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            Address currAddress = entry.getKey().two;
            String currAddr = currAddress.getAddress();

            System.out.println(String.format("%3d. %s %s (%s), employed at: ",
                    employeeId,
                    firstName,
                    lastName,
                    currAddr));

            int officeNum = 1;
            for (TupleOfSix<Company, City, Country, Address, Integer, Position>
                    tupleSix : entry.getValue()) {
                String companyName = tupleSix.company.getName();
                String city = tupleSix.city.getCity();
                String address = tupleSix.address.getAddress();
                Integer personnelCount = tupleSix.personnelCount;
                String position = tupleSix.employeePosition.getName();
                String country = tupleSix.country.getCountry();

                System.out.print(
                        String.format(
                                "\toffice %d: %s; location: %s, %s, %s; staff: %s; pos.: %s\n",
                                officeNum++, companyName, city, country, address, personnelCount, position
                        )
                );
            }

        }
    }

}

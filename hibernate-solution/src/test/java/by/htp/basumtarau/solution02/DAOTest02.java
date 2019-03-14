package by.htp.basumtarau.solution02;

import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.dto.EmployeeDetailDTO;
import by.htp.basumatarau.hibernate.dao.exception.PersistenceException;
import by.htp.basumatarau.hibernate.dao.impl.EmployeeDAOImpl;
import org.junit.Test;

import java.util.List;

public class DAOTest02 {


    @Test
    public void employeeDAOTest01() throws PersistenceException {
        List<EmployeeDetailDTO> result = new EmployeeDAOImpl().getEmployeeDTO(10, 10);
        for (EmployeeDetailDTO employeeDetailDTO : result) {
            System.out.println(employeeDetailDTO.getFirstName() + " " + employeeDetailDTO.getLastName() + " "
                    + employeeDetailDTO.getCurrentAddress());
        }
    }

    @Test
    public void employeeDAOTest02() throws PersistenceException {
        Employee emp = new EmployeeDAOImpl().read(1);

        printEmployee(emp);
    }

    @Test
    public void employeeDAOTest03() throws PersistenceException {
        List<Employee> result
                = new EmployeeDAOImpl().read(10, 10);
        for (Employee employee : result) {
            printEmployee(employee);
        }
    }

    private void printEmployee(Employee emp) {
        System.out.println(
                String.format(
                        "%3d. %s %s (%s), employed at: ",
                        emp.getEmployeeId(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getCurrentAddress().getAddress()
                )
        );

        List<Address> addresses = emp.getAddresses();
        List<Position> positions = emp.getPositions();
        List<Company> companies = emp.getCompanies();
        int j = 0;
        for (int i = 0; i < addresses.size(); i++) {
            if(positions.get(i) == null
                    || addresses.get(i) == null
                    || companies.get(i) == null){
                continue;
            }
            System.out.print(
                    String.format(
                            "\toffice %d: %s; location: %s, %s, %s; staff: %s; pos.: %s\n",
                            ++j,
                            companies.get(i).getName(),
                            addresses.get(i).getCity().getCity(),
                            addresses.get(i).getCity().getCountry().getCountry(),
                            addresses.get(i).getAddress(),
                            9999999,
                            positions.get(i).getName()
                    )
            );
        }
    }

}

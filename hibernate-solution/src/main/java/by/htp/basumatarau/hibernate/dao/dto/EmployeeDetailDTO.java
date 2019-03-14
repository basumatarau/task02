package by.htp.basumatarau.hibernate.dao.dto;

import by.htp.basumatarau.hibernate.dao.beans.Address;
import by.htp.basumatarau.hibernate.dao.beans.Company;
import by.htp.basumatarau.hibernate.dao.beans.Employee;
import by.htp.basumatarau.hibernate.dao.beans.Position;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EmployeeDetailDTO {
    private String firstName;
    private String lastName;
    private String currentAddress;
    private Set<OfficePositionDTO> offices;

    public static EmployeeDetailDTO makeDTO(Employee employee) {
        EmployeeDetailDTO empDTO
                = new EmployeeDetailDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getCurrentAddress().getAddress()
        );

        Set<OfficePositionDTO> officeDTOs = new HashSet<>();
        Iterator<Company> companyIterator = employee.getCompanies().iterator();
        Iterator<Address> addressIterator = employee.getAddresses().iterator();
        Iterator<Position> positionIterator = employee.getPositions().iterator();
        while (companyIterator.hasNext()
                && addressIterator.hasNext()
                && positionIterator.hasNext()) {
            Company com = companyIterator.next();
            Address adr = addressIterator.next();
            Position pos = positionIterator.next();

            if (com == null || adr == null || pos == null) {
                continue;
            }

            officeDTOs.add(
                    new OfficePositionDTO(
                            com.getName(),
                            adr.getCity().getCity(),
                            adr.getCity().getCountry().getCountry(),
                            adr.getAddress(),
                            99999,
                            pos.getName()
                    )
            );
        }
        empDTO.setOffices(officeDTOs);
        return empDTO;
    }

    private EmployeeDetailDTO(String firstName, String lastName, String currentAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentAddress = currentAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Set<OfficePositionDTO> getOffices() {
        return offices;
    }

    public void setOffices(Set<OfficePositionDTO> offices) {
        this.offices = offices;
    }
}

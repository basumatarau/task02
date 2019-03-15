package by.htp.basumatarau.hibernate.dao.dto;

import java.util.Set;

public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String currentAddress;
    private Set<OfficeDetailDTO> offices;

    public Set<OfficeDetailDTO> getOffices() {
        return offices;
    }

    public void setOffices(Set<OfficeDetailDTO> offices) {
        this.offices = offices;
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
}

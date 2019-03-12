package by.htp.basumatarau.hibernate.dao.beans;

import java.util.List;
import java.util.Objects;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private int fidAddress;
    private Address currentAddress;

    private List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }


    public int getFidAddress() {
        return fidAddress;
    }

    public void setFidAddress(int fidAddress) {
        this.fidAddress = fidAddress;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId &&
                fidAddress == employee.fidAddress &&
                firstName.equals(employee.firstName) &&
                lastName.equals(employee.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, fidAddress);
    }
}

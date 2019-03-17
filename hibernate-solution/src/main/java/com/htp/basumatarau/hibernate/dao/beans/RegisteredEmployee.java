package com.htp.basumatarau.hibernate.dao.beans;

import java.util.Objects;

public class RegisteredEmployee {
    private int id;
    private Employee employee;
    private Address address;
    private Company company;
    private String jobPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredEmployee that = (RegisteredEmployee) o;
        return id == that.id &&
                Objects.equals(employee, that.employee) &&
                Objects.equals(address, that.address) &&
                Objects.equals(company, that.company) &&
                Objects.equals(jobPosition, that.jobPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employee, address, company, jobPosition);
    }
}

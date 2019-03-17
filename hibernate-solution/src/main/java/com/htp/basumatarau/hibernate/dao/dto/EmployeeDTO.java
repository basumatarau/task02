package com.htp.basumatarau.hibernate.dao.dto;

public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String currentAddress;
    private String currentCity;
    private String currentCountry;
    private String company;
    private String city;
    private String country;
    private String address;
    private Integer numStaff;
    private String jobPosition;

    public EmployeeDTO(String firstName,
                       String lastName,
                       String currentAddress,
                       String currentCity,
                       String currentCountry,
                       String company,
                       String city,
                       String country,
                       String address,
                       Integer numStaff,
                       String jobPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentAddress = currentAddress;
        this.currentCity = currentCity;
        this.currentCountry = currentCountry;
        this.company = company;
        this.city = city;
        this.country = country;
        this.address = address;
        this.numStaff = numStaff;
        this.jobPosition = jobPosition;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", currentAddress='" + currentAddress + '\'' +
                ", currentCity='" + currentCity + '\'' +
                ", currentCountry='" + currentCountry + '\'' +
                ", company='" + company + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", numStaff=" + numStaff +
                ", jobPosition='" + jobPosition + '\'' +
                '}';
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumStaff() {
        return numStaff;
    }

    public void setNumStaff(Integer numStaff) {
        this.numStaff = numStaff;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }
}

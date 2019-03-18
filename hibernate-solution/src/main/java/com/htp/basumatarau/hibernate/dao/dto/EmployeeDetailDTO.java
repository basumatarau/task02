package com.htp.basumatarau.hibernate.dao.dto;

public class EmployeeDetailDTO {
    private final String firstName;
    private final String lastName;
    private final String currentAddress;
    private final String currentCity;
    private final String currentCountry;
    private final String company;
    private final String city;
    private final String country;
    private final String address;
    private final Long numStaff;
    private final String jobPosition;

    public EmployeeDetailDTO(String firstName,
                             String lastName,
                             String currentAddress,
                             String currentCity,
                             String currentCountry,
                             String company,
                             String city,
                             String country,
                             String address,
                             Long numStaff,
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
        return  '\'' + firstName + " " + lastName + '\'' + " " +
                '\'' + currentAddress + " " + currentCity + " " + currentCountry + '\'' +
                " " + '\'' + company + '\'' +
                " " + '\'' + city + '\'' +
                " " + '\'' + country + '\'' +
                " " + '\'' + address + '\'' +
                ", staff: " + numStaff +
                ", job:'" + jobPosition + '\'';
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public String getCompany() {
        return company;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public Long getNumStaff() {
        return numStaff;
    }

    public String getJobPosition() {
        return jobPosition;
    }
}

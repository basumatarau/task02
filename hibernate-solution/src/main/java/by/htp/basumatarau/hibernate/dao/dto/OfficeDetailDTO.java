package by.htp.basumatarau.hibernate.dao.dto;

public class OfficeDetailDTO {
    private String company;
    private String city;
    private String country;
    private String address;
    private Integer numStaff;
    private String jobPosition;

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

package by.htp.basumatarau.hibernate.dao.dto;

public class OfficePositionDTO {
    private String company;
    private String city;
    private String country;
    private String address;
    private Integer numEmployees;
    private String position;

    public OfficePositionDTO(String company,
                             String city,
                             String country,
                             String address,
                             Integer numEmployees,
                             String position){
        this.company = company;
        this.city = city;
        this.country = country;
        this.address = address;
        this.numEmployees = numEmployees;
        this.position = position;
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

    public Integer getNumEmployees() {
        return numEmployees;
    }

    public String getPosition() {
        return position;
    }
}

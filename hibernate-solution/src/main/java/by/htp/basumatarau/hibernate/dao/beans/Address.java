package by.htp.basumatarau.hibernate.dao.beans;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue
    @Column(name="id_address")
    private int id;
    @Column(name="address")
    private String address;
    private City city;
    private Set<Company> companies;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return id == address1.id &&
                Objects.equals(address, address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}

package by.htp.basumatarau.hibernate.dao.beans;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue
    @Column(name="id_city")
    private int cityId;
    @Column(name="city")
    private String city;
    private Country country;
    private Set<Address> addresses;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city1 = (City) o;
        return cityId == city1.cityId &&
                Objects.equals(city, city1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, city);
    }

}

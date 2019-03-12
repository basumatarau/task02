package by.htp.basumatarau.hibernate.dao.beans;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue
    @Column(name="id_country")
    private int countryId;
    @Column(name="country")
    private String country;
    private Set<City> cities;

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return countryId == country1.countryId &&
                Objects.equals(country, country1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, country);
    }
}

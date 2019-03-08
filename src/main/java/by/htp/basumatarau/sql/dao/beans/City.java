package by.htp.basumatarau.sql.dao.beans;

import java.util.Objects;

public class City {
    private int cityId;
    private String city;
    private int fidCountry;

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

    public int getFidCountry() {
        return fidCountry;
    }

    public void setFidCountry(int fidCountry) {
        this.fidCountry = fidCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city1 = (City) o;
        return cityId == city1.cityId &&
                fidCountry == city1.fidCountry &&
                Objects.equals(city, city1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, city, fidCountry);
    }

}

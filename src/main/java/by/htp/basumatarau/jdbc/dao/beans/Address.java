package by.htp.basumatarau.jdbc.dao.beans;

import java.util.Objects;

public class Address {
    private int id;
    private String address;
    private int fidCity;

    public int getFidCity() {
        return fidCity;
    }

    public void setFidCity(int fidCity) {
        this.fidCity = fidCity;
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
                Objects.equals(address, address1.address) &&
                Objects.equals(fidCity, address1.fidCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}

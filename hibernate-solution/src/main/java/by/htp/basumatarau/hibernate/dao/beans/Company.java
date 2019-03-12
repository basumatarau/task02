package by.htp.basumatarau.hibernate.dao.beans;

import java.util.Objects;
import java.util.Set;

public class Company {
    private int companyId;
    private String name;
    private Set<Address> addresses;

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return companyId == company.companyId &&
                Objects.equals(name, company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, name);
    }
}

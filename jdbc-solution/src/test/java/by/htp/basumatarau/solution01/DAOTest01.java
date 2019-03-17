package by.htp.basumatarau.solution01;

import by.htp.basumatarau.jdbc.dao.DAO;
import by.htp.basumatarau.jdbc.dao.DAOProvider;
import by.htp.basumatarau.jdbc.dao.beans.*;
import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import by.htp.basumatarau.jdbc.dao.impl.EmployeeDAOImpl;
import by.htp.basumatarau.jdbc.dao.util.TupleOfFour;
import by.htp.basumatarau.jdbc.dao.util.TupleOfSix;
import org.junit.Assert;
import org.junit.Test;


import java.util.List;
import java.util.Map;

public class DAOTest01 {

    @Test
    public void EmployeeQueryTest02() throws PersistenceException {

        for (String tuple : new EmployeeDAOImpl().getSimipleDetailed(100, 0)) {
            System.out.println(tuple);
        }
    }

    @Test
    public void EmployeeQueryTest01() throws PersistenceException {
        for (Map.Entry<TupleOfFour<Employee, Address, City, Country>,
                List<TupleOfSix<Company, City, Country, Address, Integer, RegisteredEmployee>>>
                entry
                : new EmployeeDAOImpl().getDetailed(100, 0).entrySet()) {

            Employee employee = entry.getKey().one;
            int employeeId =employee.getEmployeeId();
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            Address currAddress = entry.getKey().two;
            String currAddr = currAddress.getAddress();

            System.out.println(String.format("%3d. %s %s (%s), employed at: ",
                    employeeId,
                    firstName,
                    lastName,
                    currAddr));

            int officeNum = 1;
            for (TupleOfSix<Company, City, Country, Address, Integer, RegisteredEmployee>
                    tupleSix : entry.getValue()) {
                String companyName = tupleSix.company.getName();
                String city = tupleSix.city.getCity();
                String address = tupleSix.address.getAddress();
                Integer personnelCount = tupleSix.personnelCount;
                String position = tupleSix.regEmployee.getJobPosition();
                String country = tupleSix.country.getCountry();

                System.out.print(
                        String.format(
                                "\toffice %d: %s; location: %s, %s, %s; staff: %s; pos.: %s\n",
                                officeNum++, companyName, city, country, address, personnelCount, position
                        )
                );
            }

        }
    }

    @Test
    public void CityDAOTest01() throws PersistenceException {
        DAO<City, Integer> cityDAO = DAOProvider.getProvider().getCityDAO();

        List<City> cities = cityDAO.read(20, 0);
        Assert.assertEquals(20, cities.size());
        for (City city : cities) {
            System.out.println(city.getCity());
        }

        City city = cityDAO.read(1);
        Assert.assertNotEquals(null, city);
        System.out.println(city.getCity());

        City newCity = new City();
        newCity.setFidCountry(1);
        newCity.setCity("newCity");
        cityDAO.create(newCity);

        System.out.println(cityDAO.read(newCity.getCityId()).getCity());
    }

    @Test
    public void CountryDAOTest01() throws PersistenceException {
        DAO<Country, Integer> countryDAO = DAOProvider.getProvider().getCountryDAO();

        List<Country> cities = countryDAO.read(3, 0);
        Assert.assertEquals(3, cities.size());
        for (Country country : cities) {
            System.out.println(country.getCountry());
        }

        Country country = countryDAO.read(1);
        Assert.assertNotEquals(null, country);
        System.out.println(country.getCountry());

        Country newCountry = new Country();
        newCountry.setCountry("newCity");
        countryDAO.create(newCountry);

        System.out.println(countryDAO.read(newCountry.getCountryId()).getCountry());
    }

    @Test
    public void AddressDAOTest01() throws PersistenceException {
        DAO<Address, Integer> addressDAO = DAOProvider.getProvider().getAddressDAO();

        List<Address> addresses = addressDAO.read(3, 0);
        Assert.assertEquals(3, addresses.size());
        for (Address address : addresses) {
            System.out.println(address.getAddress());
        }

        Address address = addressDAO.read(1);
        Assert.assertNotEquals(null, address);
        System.out.println(address.getAddress());

        Address newAddress = new Address();
        newAddress.setAddress("newAddress");
        newAddress.setFidCity(20);
        addressDAO.create(newAddress);

        System.out.println(addressDAO.read(newAddress.getId()).getAddress());
    }

    @Test
    public void CompanyDAOTest01() throws PersistenceException {
        DAO<Company, Integer> companyDAO = DAOProvider.getProvider().getCompanyDAO();

        List<Company> companies = companyDAO.read(3, 0);
        Assert.assertEquals(3, companies.size());
        for (Company company : companies) {
            System.out.println(company.getName());
        }

        Company company = companyDAO.read(1);
        Assert.assertNotEquals(null, company);
        System.out.println(company.getName());

        Company newCompany = new Company();
        newCompany.setName("newCompany");
        companyDAO.create(newCompany);

        System.out.println(companyDAO.read(newCompany.getCompanyId()).getName());
    }

    @Test
    public void EmployeeDAOTest01() throws PersistenceException {
        DAO<Employee, Integer> employeeDAO = DAOProvider.getProvider().getEmployeeDAO();

        List<Employee> companies = employeeDAO.read(3, 0);
        Assert.assertEquals(3, companies.size());
        for (Employee em : companies) {
            System.out.println(em.getFirstName() + " " + em.getLastName());
        }

        Employee employee = employeeDAO.read(1);
        Assert.assertNotEquals(null, employee);
        System.out.println(employee.getFirstName() + " " + employee.getLastName());

        Employee newEmployee = new Employee();
        newEmployee.setFirstName("newEmpFirstName");
        newEmployee.setLastName("newEmpLastName");
        newEmployee.setFidAddress(10);
        employeeDAO.create(newEmployee);

        System.out.println(employeeDAO.read(newEmployee.getEmployeeId()).getFirstName());
    }

}

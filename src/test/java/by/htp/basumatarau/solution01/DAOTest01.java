package by.htp.basumatarau.solution01;

import by.htp.basumatarau.sql.dao.DAO;
import by.htp.basumatarau.sql.dao.DAOProvider;
import by.htp.basumatarau.sql.dao.beans.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DAOTest01 {

    @Test
    public void EmployeeQueryTest(){

    }

    @Test
    public void CityDAOTest01(){
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
    public void CountryDAOTest01(){
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
    public void AddressDAOTest01(){
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
    public void CompanyDAOTest01(){
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
    public void EmployeeDAOTest01(){
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

    @Test
    public void PositionDAOTest01(){
        DAO<Position, Integer> positionDAO = DAOProvider.getProvider().getPositionDAO();

        List<Position> positions = positionDAO.read(3, 0);
        Assert.assertEquals(3, positions.size());
        for (Position em : positions) {
            System.out.println(em.getName());
        }

        Position employee = positionDAO.read(1);
        Assert.assertNotEquals(null, employee);
        System.out.println(employee.getName());

        Position newPosition = new Position();
        newPosition.setName("newPosition");
        positionDAO.create(newPosition);

        System.out.println(positionDAO.read(newPosition.getId()).getName());
    }
}

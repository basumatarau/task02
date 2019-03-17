package by.htp.basumatarau.hibernate.dao;

import by.htp.basumatarau.hibernate.dao.beans.*;
import by.htp.basumatarau.hibernate.dao.impl.*;

public class DAOProvider {

    private static DAOProvider providerInstance;
    private static DAO<City, Integer> cityDAO;
    private static DAO<Country, Integer> countryDAO;
    private static DAO<Address, Integer> addressDAO;
    private static DAO<Company, Integer> companyDAO;
    private static DAO<Employee, Integer> employeeDAO;

    private DAOProvider(){}

    public static DAOProvider getProviderInstance() {
        return providerInstance;
    }

    public DAO<City, Integer> getCityDAO() {
        return cityDAO;
    }

    public DAO<Country, Integer> getCountryDAO() {
        return countryDAO;
    }

    public DAO<Address, Integer> getAddressDAO() {
        return addressDAO;
    }

    public DAO<Company, Integer> getCompanyDAO() {
        return companyDAO;
    }

    public DAO<Employee, Integer> getEmployeeDAO() {
        return employeeDAO;
    }

    public static DAOProvider getProvider(){
        if(providerInstance == null){
            synchronized (DAOProvider.class){
                if(providerInstance == null){
                    providerInstance = new DAOProvider();
                    countryDAO = new CountryDAOImpl();
                    cityDAO = new CityDAOImpl();
                    addressDAO = new AddressDAOImpl();
                    companyDAO = new CompanyDAOImpl();
                    employeeDAO = new EmployeeDAOImpl();
                }
            }
        }
        return providerInstance;
    }
}

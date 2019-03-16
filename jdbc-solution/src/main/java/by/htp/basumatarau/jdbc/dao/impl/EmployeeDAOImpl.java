package by.htp.basumatarau.jdbc.dao.impl;

import by.htp.basumatarau.jdbc.dao.DAO;
import by.htp.basumatarau.jdbc.dao.beans.*;
import by.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import by.htp.basumatarau.jdbc.dao.util.TupleOfFour;
import by.htp.basumatarau.jdbc.dao.util.TupleOfSix;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

public class EmployeeDAOImpl implements DAO<Employee, Integer> {

    private static final Logger log = Logger.getLogger(EmployeeDAOImpl.class);

    private final static String INSERT_NEW_EMPLOYEE_INTO_EMPLOYEES
            = "INSERT INTO `employees` (`first_name`, `last_name`, `fid_address`) VALUES(?,?,?)";
    private final static String SELECT_EMPLOYEE_BY_ID
            = "SELECT * FROM `employees` WHERE `id_employee`=?";
    private final static String DELETE_EMPLOYEE
            = "DELETE FROM `employees` WHERE (`id_employee`, `first_name`, `last_name`, `fid_address`) VALUES(?,?,?,?)";
    private final static String SELECT_EMPLOYEES
            = "SELECT * FROM `employees` LIMIT ?,? ";
    private final static String SELECT_EMPLOYEES_DETAILED_WITH_IDS
            = "SELECT \n" +
            "id_employee,\n" +
            "first_name,\n" +
            "last_name,\n" +
            "id_address,\n" +
            "address, \n" +
            "id_city,\n" +
            "city, \n" +
            "id_country,\n" +
            "country,\n" +
            "id_company,\n" +
            "company_name,\n" +
            "id_officeAddress,\n" +
            "officeAddress,\n" +
            "id_officeCity,\n" +
            "officeCity,\n" +
            "id_officeCountry,\n" +
            "officeCountry,\n" +
            "officeStaff,\n" +
            "job_position\n" +
            "FROM (SELECT *\n" +
            "\tFROM employees\n" +
            "\tJOIN addresses \n" +
            "\t\tON employees.fid_address = addresses.id_address\n" +
            "\tJOIN cities \n" +
            "\t\tON addresses.fid_city = cities.id_city\n" +
            "\tJOIN countries \n" +
            "\t\tON cities.fid_country = countries.id_country\n" +
            "\tORDER BY employees.id_employee\n" +
            "\tLIMIT ?,?) \n" +
            "\tAS interim \n" +
            "\t\tJOIN (SELECT fid_address AS fid_officeAddress, fid_company, job_position, fid_employee FROM employee_register) AS reg\n" +
            "\t\t\tON interim.id_employee = reg.fid_employee\n" +
            "\t\tJOIN (SELECT address AS officeAddress, id_address as id_officeAddress, fid_city as fid_officeCity FROM addresses) AS officeAddress\n" +
            "\t\t\tON officeAddress.id_officeAddress = reg.fid_officeAddress\n" +
            "\t\tJOIN (SELECT city AS officeCity, id_city as id_officeCity, fid_country as fid_officeCountry FROM cities) AS cty\n" +
            "\t\t\tON officeAddress.fid_officeCity = cty.id_officeCity\n" +
            "\t\tJOIN (SELECT country AS officeCountry, id_country AS id_officeCountry FROM countries) AS cntry\n" +
            "\t\t\tON cty.fid_officeCountry = cntry.id_officeCountry\n" +
            "\t\tJOIN (SELECT name AS company_name, id_company FROM companies) AS cmp\n" +
            "\t\t\tON cmp.id_company = reg.fid_company\n" +
            "\t\tJOIN (SELECT fid_company, COUNT(DISTINCT fid_address) as officeStaff FROM employee_register GROUP BY fid_company) countSheet\n" +
            "\t\t\tON countSheet.fid_company = reg.fid_company ";

    private final static String SELECT_EMPLOYEES_DETAILED_SIMPLE
            ="SELECT \n" +
            "id_employee,\n" +
            "first_name,\n" +
            "last_name,\n" +
            "address, \n" +
            "city, \n" +
            "country,\n" +
            "company_name,\n" +
            "officeAddress,\n" +
            "officeCity,\n" +
            "officeCountry,\n" +
            "officeStaff,\n" +
            "job_position\n" +
            "FROM (SELECT *\n" +
            "\tFROM employees\n" +
            "\tJOIN addresses \n" +
            "\t\tON employees.fid_address = addresses.id_address\n" +
            "\tJOIN cities \n" +
            "\t\tON addresses.fid_city = cities.id_city\n" +
            "\tJOIN countries \n" +
            "\t\tON cities.fid_country = countries.id_country\n" +
            "\tORDER BY employees.id_employee\n" +
            "\tLIMIT ?,?) \n" +
            "\tAS interim \n" +
            "\t\tJOIN (SELECT fid_address AS fid_officeAddress, fid_company, job_position, fid_employee FROM employee_register) AS reg\n" +
            "\t\t\tON interim.id_employee = reg.fid_employee\n" +
            "\t\tJOIN (SELECT address AS officeAddress, id_address as id_officeAddress, fid_city as fid_officeCity FROM addresses) AS officeAddress\n" +
            "\t\t\tON officeAddress.id_officeAddress = reg.fid_officeAddress\n" +
            "\t\tJOIN (SELECT city AS officeCity, id_city as id_officeCity, fid_country as fid_officeCountry FROM cities) AS cty\n" +
            "\t\t\tON officeAddress.fid_officeCity = cty.id_officeCity\n" +
            "\t\tJOIN (SELECT country AS officeCountry, id_country AS id_officeCountry FROM countries) AS cntry\n" +
            "\t\t\tON cty.fid_officeCountry = cntry.id_officeCountry\n" +
            "\t\tJOIN (SELECT name AS company_name, id_company FROM companies) AS cmp\n" +
            "\t\t\tON cmp.id_company = reg.fid_company\n" +
            "\t\tJOIN (SELECT fid_company, COUNT(DISTINCT fid_address) as officeStaff FROM employee_register GROUP BY fid_company) countSheet\n" +
            "\t\t\tON countSheet.fid_company = reg.fid_company";

    public Map<TupleOfFour<Employee, Address, City, Country>,
            List<TupleOfSix<Company, City, Country, Address, Integer, RegisteredEmployee>>>
                getDetailed(int numEntries, int startingFrom) throws PersistenceException {

        Map<TupleOfFour<Employee, Address, City, Country>,
                List<TupleOfSix<Company, City, Country, Address, Integer, RegisteredEmployee>>
                > result = new LinkedHashMap<>();

        log.debug("detailed employee query execution");

        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_EMPLOYEES_DETAILED_WITH_IDS);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Employee employee = new Employee();
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setEmployeeId(resultSet.getInt("id_employee"));
                employee.setFidAddress(resultSet.getInt("id_address"));

                Address currentAddress = new Address();
                currentAddress.setFidCity(resultSet.getInt("id_city"));
                currentAddress.setAddress(resultSet.getString("address"));
                currentAddress.setId(resultSet.getInt("id_address"));

                City city = new City();
                city.setFidCountry(resultSet.getInt("id_country"));
                city.setCity(resultSet.getString("city"));
                city.setCityId(resultSet.getInt("id_city"));

                Country country = new Country();
                country.setCountry(resultSet.getString("country"));
                country.setCountryId(resultSet.getInt("id_country"));

                Company company = new Company();
                company.setName(resultSet.getString("company_name"));
                company.setCompanyId(resultSet.getInt("id_company"));

                Address address = new Address();
                address.setId(resultSet.getInt("id_address"));
                address.setAddress(resultSet.getString("address"));
                address.setFidCity(resultSet.getInt("id_city"));

                City officeCity = new City();
                officeCity.setFidCountry(resultSet.getInt("id_officeCountry"));
                officeCity.setCity(resultSet.getString("officeCity"));
                officeCity.setCityId(resultSet.getInt("id_officeCity"));

                Country officeCountry = new Country();
                officeCountry.setCountry(resultSet.getString("officeCountry"));
                officeCountry.setCountryId(resultSet.getInt("id_officeCountry"));

                Integer empCount = resultSet.getInt("officeStaff");

                RegisteredEmployee regEmp = new RegisteredEmployee();
                regEmp.setJobPosition(resultSet.getString("job_position"));
                regEmp.setFidAddress(resultSet.getInt("id_address"));
                regEmp.setFidCompany(resultSet.getInt("id_company"));
                regEmp.setFidEmployee(resultSet.getInt("id_employee"));

                TupleOfFour<Employee, Address, City, Country> tt
                        = new TupleOfFour<>(employee, currentAddress, city, country);

                TupleOfSix<Company, City, Country, Address, Integer, RegisteredEmployee> ts
                        = new TupleOfSix<>(company, officeCity, officeCountry, address, empCount, regEmp);

                if(!result.containsKey(tt)){
                    result.put(tt, new ArrayList<>());
                }
                for (Map.Entry<TupleOfFour<Employee, Address, City, Country>,
                        List<TupleOfSix<Company, City, Country,
                                Address, Integer, RegisteredEmployee>>>
                        entry : result.entrySet()) {
                    if(tt.equals(entry.getKey())){
                        if(entry.getValue()!=null) {
                            entry.getValue().add(ts);
                            break;
                        }
                    }
                }

            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public boolean create(Employee entity) throws PersistenceException {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_EMPLOYEE_INTO_EMPLOYEES,
                    Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setInt(3, entity.getFidAddress());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    entity.setEmployeeId(id);
                    result = true;
                }
                con.commit();
            }catch (SQLException e){
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public Employee read(Integer id) throws PersistenceException {
        Employee employee = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_EMPLOYEE_BY_ID, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                employee = new Employee();
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setEmployeeId(resultSet.getInt("id_employee"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return employee;
    }

    @Override
    public boolean delete(Employee entity) throws PersistenceException {
        int changedEntries;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_EMPLOYEE);
            try{
                ps.setInt(1, entity.getEmployeeId());
                ps.setString(2, entity.getFirstName());
                ps.setString(3, entity.getLastName());
                ps.setInt(4, entity.getFidAddress());
                changedEntries = ps.executeUpdate();
                con.commit();
            }catch (SQLException e){
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return changedEntries == 1;
    }

    @Override
    public List<Employee> read(int numEntries, int startingFrom) throws PersistenceException {
        List<Employee> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_EMPLOYEES);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Employee employee = new Employee();
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setEmployeeId(resultSet.getInt("id_employee"));
                result.add(employee);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }
}

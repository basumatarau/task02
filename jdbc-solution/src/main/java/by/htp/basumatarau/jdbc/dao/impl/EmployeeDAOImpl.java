package by.htp.basumatarau.jdbc.dao.impl;

import by.htp.basumatarau.jdbc.dao.DAO;
import by.htp.basumatarau.jdbc.dao.beans.*;
import by.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import by.htp.basumatarau.jdbc.dao.util.TupleOfSix;
import by.htp.basumatarau.jdbc.dao.util.TupleOfTwo;
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
    private final static String SELECT_EMPLOYEES_DETAILED_SIMPLE
            = "SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " company_name,\n" +
            " city,\n" +
            " country,\n" +
            " address,\n" +
            " numEmployed,\n" +
            " position  \n" +
            "FROM(SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " company_name,\n" +
            " city,\n" +
            " fid_country,\n" +
            " address,\n" +
            " numEmployed,\n" +
            " position \n" +
            "FROM (SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " company_name,\n" +
            " fid_city,\n" +
            " address,\n" +
            " numEmployed,\n" +
            " position\n" +
            "FROM (SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address_id,\n" +
            " fid_company,\n" +
            " fid_address,\n" +
            " fid_job_position\n" +
            "\tFROM (SELECT id_employee,\n" +
            "\tfirst_name,\n" +
            "\tlast_name,\n" +
            "\tfid_address AS curr_address_id FROM task02.employees\n" +
            "    ORDER BY id_employee\n" +
            "\tLIMIT ?, ?)\n" +
            "\temp \n" +
            "\tJOIN task02.employee_register er\n" +
            "\t\tON emp.id_employee=er.fid_employee)\n" +
            "        res1 \n" +
            "\tJOIN (SELECT address AS curr_address, id_address AS id_curr_addr FROM task02.addresses) AS addr  \n" +
            "\t\tON res1.curr_address_id=addr.id_curr_addr\n" +
            "\tJOIN task02.job_positions pos\n" +
            "\t\tON res1.fid_job_position=pos.id_job_position\n" +
            "\tJOIN (SELECT `name` AS company_name, id_company FROM task02.companies) AS comp\n" +
            "\t\tON res1.fid_company=comp.id_company\n" +
            "\tJOIN task02.addresses addr\n" +
            "\t\tON res1.fid_address=addr.id_address\n" +
            "\tJOIN (SELECT fid_employee, fid_address, COUNT(DISTINCT fid_address) as numEmployed FROM task02.employee_register GROUP BY fid_company) AS count\n" +
            "\t\tON res1.id_employee=count.fid_employee AND res1.fid_address=count.fid_address)\n" +
            "        res2 \n" +
            "\tJOIN task02.cities cts\n" +
            "\t\tON res2.fid_city=cts.id_city)\n" +
            "        res3 \n" +
            "\tJOIN task02.countries cns\n" +
            "\t\tON res3.fid_country=cns.id_country\n";

    private final static String SELECT_EMPLOYEES_DETAILED_WITH_IDS
            ="SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " id_curr_addr,\n" +
            " curr_fid_city,\n" +
            " company_name,\n" +
            " id_company,\n" +
            " city,\n" +
            " id_city,\n" +
            " country,\n" +
            " id_country,\n" +
            " address,\n" +
            " id_address,\n" +
            " numEmployed,\n" +
            " position,\n" +
            " id_job_position\n" +
            "FROM(SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " id_curr_addr,\n" +
            " curr_fid_city,\n" +
            " company_name,\n" +
            " id_company,\n" +
            " city,\n" +
            " id_city,\n" +
            " fid_country,\n" +
            " address,\n" +
            " id_address,\n" +
            " numEmployed,\n" +
            " position,\n" +
            " id_job_position\n" +
            "FROM (SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address,\n" +
            " id_curr_addr,\n" +
            " company_name,\n" +
            " id_company,\n" +
            " fid_city,\n" +
            " address,\n" +
            " id_address,\n" +
            " numEmployed,\n" +
            " position,\n" +
            " id_job_position\n" +
            "FROM (SELECT id_employee,\n" +
            " first_name,\n" +
            " last_name,\n" +
            " curr_address_id,\n" +
            " fid_company,\n" +
            " fid_address,\n" +
            " fid_job_position\n" +
            "\tFROM (SELECT id_employee,\n" +
            "\tfirst_name,\n" +
            "\tlast_name,\n" +
            "\tfid_address AS curr_address_id FROM task02.employees\n" +
            "\tLIMIT ?, ?)\n" +
            "\temp \n" +
            "\tJOIN task02.employee_register er\n" +
            "\t\tON emp.id_employee=er.fid_employee)\n" +
            "        res1 \n" +
            "\tJOIN (SELECT address AS curr_address, id_address AS id_curr_addr FROM task02.addresses) AS addr  \n" +
            "\t\tON res1.curr_address_id=addr.id_curr_addr\n" +
            "\tJOIN task02.job_positions pos\n" +
            "\t\tON res1.fid_job_position=pos.id_job_position\n" +
            "\tJOIN (SELECT `name` AS company_name, id_company FROM task02.companies) AS comp\n" +
            "\t\tON res1.fid_company=comp.id_company\n" +
            "\tJOIN task02.addresses addr\n" +
            "\t\tON res1.fid_address=addr.id_address\n" +
            "\tJOIN (SELECT fid_company, COUNT(DISTINCT fid_address) as numEmployed FROM task02.employee_register GROUP BY fid_company) AS count\n" +
            "\t\tON res1.fid_company=count.fid_company)\n" +
            "        res2 \n" +
            "\tJOIN task02.cities cts\n" +
            "\t\tON res2.fid_city=cts.id_city\n" +
            "\tJOIN (SELECT fid_city AS curr_fid_city, id_address AS _id_address FROM  task02.addresses) as dd\n" +
            "\t\tON res2.id_curr_addr=dd._id_address)\n" +
            "        res3 \n" +
            "\tJOIN task02.countries cns\n" +
            "\t\tON res3.fid_country=cns.id_country\n" +
            "\tORDER BY id_employee";

    public Map<TupleOfTwo<Employee, Address>,
            List<TupleOfSix<Company, City, Country, Address, Integer, Position>>>
                getDetailed(int numEntries, int startingFrom) throws PersistenceException {

        Map<TupleOfTwo<Employee, Address>,
                List<TupleOfSix<Company, City, Country, Address, Integer, Position>>
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
                employee.setFidAddress(resultSet.getInt("id_curr_addr"));

                Address currentAddress = new Address();
                currentAddress.setFidCity(resultSet.getInt("curr_fid_city"));
                currentAddress.setAddress(resultSet.getString("curr_address"));
                currentAddress.setId(resultSet.getInt("id_curr_addr"));


                Company company = new Company();
                company.setName(resultSet.getString("company_name"));
                company.setCompanyId(resultSet.getInt("id_company"));

                Country country = new Country();
                country.setCountry(resultSet.getString("country"));
                country.setCountryId(resultSet.getInt("id_country"));

                City city = new City();
                city.setFidCountry(resultSet.getInt("id_country"));
                city.setCity(resultSet.getString("city"));
                city.setCityId(resultSet.getInt("id_city"));

                Address address = new Address();
                address.setId(resultSet.getInt("id_address"));
                address.setAddress(resultSet.getString("address"));
                address.setFidCity(resultSet.getInt("id_city"));

                Integer empCount = resultSet.getInt("numEmployed");

                Position position = new Position();
                position.setId(resultSet.getInt("id_job_position"));
                position.setName(resultSet.getString("position"));


                TupleOfTwo<Employee, Address> tt = new TupleOfTwo<>(employee, currentAddress);

                TupleOfSix<Company, City, Country, Address, Integer, Position> ts
                        = new TupleOfSix<>(company, city, country, address, empCount, position);

                if(!result.containsKey(tt)){
                    result.put(tt, new ArrayList<>());
                }
                for (Map.Entry<
                        TupleOfTwo<Employee, Address>,
                        List<TupleOfSix
                                <Company,
                                City,
                                Country,
                                Address,
                                Integer,
                                Position>
                            >
                        >
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

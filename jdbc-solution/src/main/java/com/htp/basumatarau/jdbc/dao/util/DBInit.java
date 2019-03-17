package com.htp.basumatarau.jdbc.dao.util;

import com.htp.basumatarau.jdbc.dao.beans.City;
import com.htp.basumatarau.jdbc.dao.beans.Country;
import com.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DBInit {
    private final static String INSERT_NEW_COUNTRY_INTO_COUNTRIES
            = "INSERT INTO `task02`.`countries` (`country`) VALUES(?)";

    private final static String INSERT_NEW_CITY_INTO_CITIES
            = "INSERT INTO `task02`.`cities` (`city`, `fid_country`) VALUES(?,?)";

    private final static String INSERT_NEW_ADDRESS_INTO_ADDRESSES
            = "INSERT INTO `task02`.`addresses` (`address`, `fid_city`) VALUES(?,?)";

    private final static String INSERT_NEW_COMPANY_INTO_COMPANIES
            = "INSERT INTO `task02`.`companies` (`name`) VALUES(?)";

    private final static String INSERT_NEW_EMPLOYEE_INTO_EMPLOYEES
            = "INSERT INTO `task02`.`employees` (`first_name`, `last_name`, `fid_address`) VALUES(?,?,?)";

    private final static String INSERT_NEW_POSITION_INTO_POSITIONS
            = "INSERT INTO `task02`.`job_positions` (`position`) VALUES(?)";

    private final static String SELECT_COUNTRY_BY_ID
            = "SELECT * FROM task02.countries AS c WHERE c.id_country = ? ";

    private final static String SELECT_CITY_BY_ID
            = "SELECT * FROM task02.cities AS c WHERE c.id_city = ? ";

    private static Random random = new Random();

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        setUpDB();
        populateDB();
    }

    private static void setUpDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?useSSL=false", "root",
                "password")) {
            new ScriptRunner(connection).runScript(new FileReader("task02-db-init-script.sql"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void populateDB() throws SQLException {
        Connection con = ConnectionSource.yieldConnection();

        PreparedStatement psCountries = con.prepareStatement(
                INSERT_NEW_COUNTRY_INTO_COUNTRIES,
                Statement.RETURN_GENERATED_KEYS
        );
        PreparedStatement psCities = con.prepareStatement(
                INSERT_NEW_CITY_INTO_CITIES,
                Statement.RETURN_GENERATED_KEYS
        );
        PreparedStatement psAddresses = con.prepareStatement(
                INSERT_NEW_ADDRESS_INTO_ADDRESSES,
                Statement.RETURN_GENERATED_KEYS
        );
        PreparedStatement psCompanies = con.prepareStatement(
                INSERT_NEW_COMPANY_INTO_COMPANIES,
                Statement.RETURN_GENERATED_KEYS
        );
        PreparedStatement psEmployees = con.prepareStatement(
                INSERT_NEW_EMPLOYEE_INTO_EMPLOYEES,
                Statement.RETURN_GENERATED_KEYS
        );
        PreparedStatement psPosition = con.prepareStatement(
                INSERT_NEW_POSITION_INTO_POSITIONS
        );

        PreparedStatement psSelectCountryById
                = con.prepareStatement(SELECT_COUNTRY_BY_ID);
        PreparedStatement psSelectCityById
                = con.prepareStatement(SELECT_CITY_BY_ID);

        populateCountries(con, psCountries, 2);
        populateCities(con, psCities, psSelectCountryById, 4);
        populateAddresses(con, psAddresses, psSelectCityById, 8);
        populateCompanies(con, psCompanies, 40);
        populateEmployees(con, psEmployees, 2000);

        //30% of the employees table entries is taken for each overlay iteration
        populateEmployeeRegister(
                "employee_register",
                2,
                con
        );
    }

    private static void populateEmployeeRegister(String joinTableName,
                                                 int maxOverlay,
                                                 Connection con) throws SQLException {

        List<String> jobPos = new ArrayList<>();
        RandomGenerators.PositionNameGen positionGen = new RandomGenerators.PositionNameGen();
        for (int i = 0; i < 300; i++) {
            jobPos.add(positionGen.next());
        }

        List<Integer> idsEmployee = getTablePKEntries("employees", con, "id_employee");
        List<Integer> idsCompany = getTablePKEntries("companies", con, "id_company");
        List<Integer> idsAddress = getTablePKEntries("addresses", con, "id_address");

        String updateJoinTableStatement = String.format(
                "INSERT INTO `task02`.`%s` (`%s`, `%s`, `%s`, `%s`) VALUES(?,?,?,?)",
                joinTableName,
                "fid_employee",
                "job_position",
                "fid_company",
                "fid_address"
        );

        PreparedStatement psUpdateJoinTable = con.prepareStatement(updateJoinTableStatement);
        con.setAutoCommit(false);

        try {
            HashSet<TupleOfFour<Integer, String, Integer, Integer>> duplicateRegister = new HashSet<>();
            HashSet<TupleOfFour<Integer, String, Integer, Integer>> toBePermuted = new HashSet<>();
            for (int i = 0; i < maxOverlay; i++) {
                LinkedList<Integer> cpIdsEmployee = new LinkedList<>(idsEmployee);
                List<String> cpJobPos = new ArrayList<>(jobPos);
                List<Integer> cpIdsCompany = new ArrayList<>(idsCompany);
                List<Integer> cpIdsAddress = new ArrayList<>(idsAddress);

                for (TupleOfFour<Integer, String, Integer, Integer> tuple : toBePermuted) {
                    cpIdsEmployee.add(tuple.one);
                    cpJobPos.add(tuple.two);
                    cpIdsCompany.add(tuple.three);
                    cpIdsAddress.add(tuple.four);
                }
                toBePermuted.clear();

                while (!cpIdsEmployee.isEmpty()) {

                    //30% of total registered employees have more than one place of work
                    if (i > 0 && random.nextInt(10) < 6) {
                        cpIdsEmployee.pop();
                        continue;
                    }

                    Integer fidEmployee = cpIdsEmployee.pop();
                    String fidJobPos = cpJobPos.remove(random.nextInt(cpJobPos.size()));
                    Integer fidCompany = cpIdsCompany.remove(random.nextInt(cpIdsCompany.size()));
                    Integer fidAddress = cpIdsAddress.remove(random.nextInt(cpIdsAddress.size()));
                    if (cpJobPos.isEmpty()) {
                        cpJobPos.addAll(jobPos);
                    }
                    if (cpIdsCompany.isEmpty()) {
                        cpIdsCompany.addAll(idsCompany);
                    }
                    if (cpIdsAddress.isEmpty()) {
                        cpIdsAddress.addAll(idsAddress);
                    }

                    TupleOfFour<Integer, String, Integer, Integer> tuple
                            = new TupleOfFour<>(fidEmployee, fidJobPos, fidCompany, fidAddress);
                    if(duplicateRegister.contains(tuple)){
                        toBePermuted.add(tuple);
                        continue;
                    }
                    duplicateRegister.add(tuple);

                    psUpdateJoinTable.setInt(1, fidEmployee);
                    psUpdateJoinTable.setString(2, fidJobPos);
                    psUpdateJoinTable.setInt(3, fidCompany);
                    psUpdateJoinTable.setInt(4, fidAddress);
                    psUpdateJoinTable.executeUpdate();
                }
                con.commit();
            }
        } catch (SQLException e) {
            con.rollback();
            throw new RuntimeException("join table population failure", e);
        }
    }

    private static List<Integer> getTablePKEntries(String tableName,
                                                   Connection con,
                                                   String PK) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT task02.%s.%s FROM task02.%s;", tableName, PK, tableName)
        );
        ArrayList<Integer> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getInt(1));
        }
        return result;
    }

    private static List<String> getPKNames(String tableOneName, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                String.format("SHOW KEYS FROM %s WHERE Key_name = 'PRIMARY'", tableOneName)
        );
        List<String> pkOne = new ArrayList<>();
        while (resultSet.next()) {
            pkOne.add(resultSet.getString("Column_name"));
        }
        return pkOne;
    }

    private static void populateEmployees(Connection con,
                                          PreparedStatement psEmployees,
                                          int capacity) throws SQLException {

        List<String> addrPKs = getPKNames("addresses", con);
        List<Integer> addrPKEntries = getTablePKEntries("addresses", con, addrPKs.remove(0));
        List<Integer> cpyAddrPKEntries = new ArrayList<>(addrPKEntries);
        try {
            for (int i = 1; i <= capacity; i++) {
                RandomGenerators.EmployeeGen employeeGen = new RandomGenerators.EmployeeGen();
                if (cpyAddrPKEntries.size() == 0) {
                    cpyAddrPKEntries.addAll(addrPKEntries);
                }
                psEmployees.setString(1, employeeGen.next().getFirstName());
                psEmployees.setString(2, employeeGen.next().getLastName());
                psEmployees.setInt(3, cpyAddrPKEntries.remove(Math.abs(random.nextInt() % cpyAddrPKEntries.size())));
                psEmployees.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw new RuntimeException();
        }
    }

    private static void populateCompanies(Connection con,
                                          PreparedStatement psCompanies,
                                          int capacity) throws SQLException {
        try {
            for (int i = 1; i <= capacity; i++) {
                RandomGenerators.CompanyGen companyGen = new RandomGenerators.CompanyGen();
                psCompanies.setString(1, companyGen.next().getName());
                psCompanies.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw new RuntimeException();
        }
    }

    private static void populateAddresses(Connection con,
                                          PreparedStatement psAddresses,
                                          PreparedStatement psSelectCityById,
                                          int maxCapacity) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM task02.cities;");

        int citiesInDB;
        if (resultSet.next()) {
            citiesInDB = resultSet.getInt(1);
        } else {
            throw new RuntimeException("DB init error: COUNT(*) query failed");
        }

        for (int cityId = 1; cityId <= citiesInDB; cityId++) {
            psSelectCityById.setInt(1, cityId);
            ResultSet rs = psSelectCityById.executeQuery();
            City city = new City();

            if (rs.next()) {
                city.setCity(rs.getString("city"));
                city.setCityId(cityId);
            } else {
                throw new RuntimeException("DB population failure");
            }

            try {
                RandomGenerators.AddressGen cityGen = new RandomGenerators.AddressGen(city);
                for (int i = 0; i < maxCapacity; i++) {
                    psAddresses.setString(1, cityGen.next().getAddress());
                    psAddresses.setInt(2, cityId);
                    psAddresses.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException();
            }
        }

    }

    private static void populateCities(Connection con,
                                       PreparedStatement psCities,
                                       PreparedStatement psSelectCountryById,
                                       int maxCapacity) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM task02.countries;");

        int countriesInDB;
        if (resultSet.next()) {
            countriesInDB = resultSet.getInt(1);
        } else {
            throw new RuntimeException("DB init error: COUNT(*) query failed");
        }

        for (int countryId = 1; countryId <= countriesInDB; countryId++) {
            psSelectCountryById.setInt(1, countryId);
            ResultSet rs = psSelectCountryById.executeQuery();
            Country country = new Country();

            if (rs.next()) {
                country.setCountry(rs.getString("country"));
                country.setCountryId(countryId);
            } else {
                throw new RuntimeException("DB table population failure");
            }

            try {
                RandomGenerators.CityGen cityGen = new RandomGenerators.CityGen(country);
                for (int i = 0; i < maxCapacity && i < cityGen.getCapacity(); i++) {
                    psCities.setString(1, cityGen.next().getCity());
                    psCities.setInt(2, countryId);
                    psCities.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException();
            }
        }
    }

    private static void populateCountries(Connection con,
                                          PreparedStatement psCountries,
                                          int maxCapacity) throws SQLException {
        con.setAutoCommit(false);
        try {
            for (int i = 1; i <= maxCapacity; i++) {
                RandomGenerators.CountryGen countryGen = new RandomGenerators.CountryGen();
                psCountries.setString(1, countryGen.next().getCountry());
                psCountries.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw new RuntimeException();
        }
    }

}

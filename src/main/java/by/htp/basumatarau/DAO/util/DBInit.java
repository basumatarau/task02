package by.htp.basumatarau.DAO.util;

import by.htp.basumatarau.DAO.beans.City;
import by.htp.basumatarau.DAO.beans.Country;
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
            = "INSERT INTO `task02`.`employees` (`first_name`, `last_name`, `position`) VALUES(?,?,?)";

    private final static String INSERT_NEW_ENTRY_INTO_ADDRESS_BOOK
            = "INSERT INTO `task02`.`address_book` (`id_address`, `id_company`) VALUES(?,?)";

    private final static String INSERT_NEW_ENTRY_TINO_EMPLOYEE_REGISTER
            = "INSERT INTO `task02`.`employee_register` (`id_company`, `id_employee`) VALUES(?,?)";

    private final static String SELECT_COUNTRY_BY_ID
            = "SELECT * FROM task02.countries as c WHERE c.id_country = ? ";

    private final static String SELECT_CITY_BY_ID
            = "SELECT * FROM task02.cities as c WHERE c.id_city = ? ";


    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?useSSL=false", "root",
                "password")){
            new ScriptRunner(conection).runScript(new FileReader("task02-db-init-script.sql"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/task02?useSSL=false", "root",
                "password");

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
        PreparedStatement psAddressBook = con.prepareStatement(
                INSERT_NEW_ENTRY_INTO_ADDRESS_BOOK
        );
        PreparedStatement psEmployeeRegister = con.prepareStatement(
                INSERT_NEW_ENTRY_TINO_EMPLOYEE_REGISTER
        );

        PreparedStatement psSelectCountryById
                = con.prepareStatement(SELECT_COUNTRY_BY_ID);
        PreparedStatement psSelectCityById
                = con.prepareStatement(SELECT_CITY_BY_ID);

        populateCountries(con, psCountries, 15);
        populateCities(con, psCities, psSelectCountryById, 10);
        populateAddresses(con, psAddresses, psSelectCityById, 5);
        populateCompanies(con, psCompanies, 100);
        populateEmployees(con, psEmployees, 2000);

        makeJoinTable(
                "companies",
                1,
                "employees",
                10,
                psEmployeeRegister,
                con
        );

        makeJoinTable(
                "companies",
                1,
                "addresses",
                4,
                psAddressBook,
                con
        );
    }

    private static void makeJoinTable(String tableOneName,
                                      int overLayOne,
                                      String tableTwoName,
                                      int overLayTwo,
                                      PreparedStatement ps,
                                      Connection con) throws SQLException {
        Random random = new Random();

        String pkOne = getPKName(tableOneName, con);
        String pkTwo = getPKName(tableTwoName, con);

        List<Integer> fIdsOne = getTablePKEntries(tableOneName, con, pkOne);
        List<Integer> fIdsTwo = getTablePKEntries(tableTwoName, con, pkTwo);
        HashMap<Integer, List<Integer>> memo = new HashMap<>();
        for (Integer fIdTwo : fIdsTwo) {
            memo.put(fIdTwo, null);
        }

        con.setAutoCommit(false);
        try {
            for (int j = 0; j < overLayOne; j++) {
                for (Integer idOne : fIdsOne) {
                    List<Integer> fIdsTwoCopy = new ArrayList<>();
                    for (int i = 0; i < overLayTwo; i++) {
                        if(fIdsTwoCopy.size() == 0){
                            fIdsTwoCopy.addAll(fIdsTwo);
                        }

                        Integer rm;
                        do {
                            rm = fIdsTwoCopy.get(Math.abs(random.nextInt() % fIdsTwoCopy.size()));
                        }while(isDuplicate(memo, idOne, rm));
                        fIdsTwoCopy.remove(rm);

                        ps.setInt(1, idOne);
                        ps.setInt(2, rm);
                        ps.executeUpdate();
                    }
                    con.commit();
                }
            }
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException("join table population failure", e);
        }
    }

    private static boolean isDuplicate(HashMap<Integer, List<Integer>> memo, Integer idOne, Integer rm) {
        for (Map.Entry<Integer, List<Integer>> entry : memo.entrySet()) {
            if(entry.getKey().equals(rm)){
                if(entry.getValue() == null){
                    entry.setValue(new ArrayList<>());
                    return false;
                }else if(entry.getValue().contains(idOne)){
                    return true;
                }else{
                    entry.getValue().add(idOne);
                    return false;
                }
            }
        }
        throw new RuntimeException("PK lost");
    }

    private static List<Integer> getTablePKEntries(String tableName,
                                                   Connection con,
                                                   String PK) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(
                String.format("SELECT task02.%s.%s FROM task02.%s;", tableName, PK, tableName)
        );
        ArrayList<Integer> result = new ArrayList<>();
        while (rs.next()){
            result.add(rs.getInt(1));
        }
        return result;
    }

    private static String getPKName(String tableOneName, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                String.format("SHOW KEYS FROM %s WHERE Key_name = 'PRIMARY'", tableOneName)
        );
        String pkOne;
        if(resultSet.next()){
            pkOne = resultSet.getString("Column_name");
        }else{
            throw new RuntimeException("no primary key found");
        }
        return pkOne;
    }

    private static void populateEmployees(Connection con,
                                          PreparedStatement psEmployees,
                                          int capacity) throws SQLException {
        try {
            for (int i = 1; i < capacity; i++) {
                RandomGenerators.EmployeeGen employeeGen = new RandomGenerators.EmployeeGen();
                psEmployees.setString(1, employeeGen.next().getFirstName());
                psEmployees.setString(2, employeeGen.next().getLastName());
                psEmployees.setString(3, employeeGen.next().getPosition());
                psEmployees.executeUpdate();
            }
            con.commit();
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException();
        }
    }

    private static void populateCompanies(Connection con,
                                                     PreparedStatement psCompanies,
                                                     int capacity) throws SQLException {
        try {
            for (int i = 1; i < capacity; i++) {
                RandomGenerators.CompanyGen companyGen = new RandomGenerators.CompanyGen();
                psCompanies.setString(1, companyGen.next().getName());
                psCompanies.executeUpdate();
            }
            con.commit();
        }catch (SQLException e){
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
        if(resultSet.next()) {
            citiesInDB = resultSet.getInt(1);
        }else{
            throw new RuntimeException("DB init error: COUNT(*) query failed");
        }

        for (int cityId = 1; cityId < citiesInDB; cityId++) {
            psSelectCityById.setInt(1, cityId);
            ResultSet rs = psSelectCityById.executeQuery();
            City city = new City();

            if(rs.next()) {
                city.setCity(rs.getString("city"));
                city.setCityId(cityId);
            }else {
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
            }catch (SQLException e){
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
        if(resultSet.next()) {
            countriesInDB = resultSet.getInt(1);
        }else{
            throw new RuntimeException("DB init error: COUNT(*) query failed");
        }

        for (int countryId = 1; countryId < countriesInDB; countryId++) {
            psSelectCountryById.setInt(1, countryId);
            ResultSet rs = psSelectCountryById.executeQuery();
            Country country = new Country();

            if(rs.next()) {
                country.setCountry(rs.getString("country"));
                country.setCountryId(countryId);
            }else {
                throw new RuntimeException("DB population failure");
            }

            try {
                RandomGenerators.CityGen cityGen = new RandomGenerators.CityGen(country);
                for (int i = 0; i < maxCapacity && i < cityGen.getCapacity(); i++) {
                    psCities.setString(1, cityGen.next().getCity());
                    psCities.setInt(2, countryId);
                    psCities.executeUpdate();
                }
                con.commit();
            }catch (SQLException e){
                con.rollback();
                throw new RuntimeException();
            }
        }
    }

    private static void populateCountries(Connection con,
                                          PreparedStatement psCountries,
                                          int maxCapacity) throws SQLException {
        con.setAutoCommit(false);
        try{
            for (int i = 0; i < maxCapacity; i++) {
                RandomGenerators.CountryGen countryGen = new RandomGenerators.CountryGen();
                psCountries.setString(1, countryGen.next().getCountry());
                psCountries.executeUpdate();
            }
            con.commit();
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException();
        }
    }

}

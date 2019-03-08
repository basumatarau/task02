package by.htp.basumatarau.sql.dao.util;

import by.htp.basumatarau.sql.dao.beans.City;
import by.htp.basumatarau.sql.dao.beans.Country;
import by.htp.basumatarau.sql.dao.connection.ConnectionSource;
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
            = "SELECT * FROM task02.countries as c WHERE c.id_country = ? ";

    private final static String SELECT_CITY_BY_ID
            = "SELECT * FROM task02.cities as c WHERE c.id_city = ? ";

    private final static String SELECT_ADDRESS_BY_ID
            = "SELECT * FROM task02.addresses as c WHERE c.id_address = ? ";

    private static Random random = new Random();

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        setUpDB();
        populateDB();
    }

    private static void setUpDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/?useSSL=false", "root",
                "password")){
            new ScriptRunner(connection).runScript(new FileReader("task02-db-init-script.sql"));
        }catch (IOException e){
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

        populateCountries(con, psCountries, 5);
        populateCities(con, psCities, psSelectCountryById, 6);
        populateAddresses(con, psAddresses, psSelectCityById, 20);
        populateCompanies(con, psCompanies, 150);
        populatePositions(con, psPosition, 300);
        populateEmployees(con, psEmployees, 2000);

        populateAddressBook(
                "address_book",
                2,
                con
        );

        populateEmployeeRegister(
                "employee_register",
                2,
                con
        );
    }

    private static void populateEmployeeRegister(String joinTableName,
                                            int overlay,
                                            Connection con) throws SQLException {
        List<String> joinTablePKNames = getPKNames(joinTableName, con);
        PreparedStatement psJoinTableInfoQuery = con.prepareStatement(
                "SELECT *\n" +
                "FROM\n" +
                "\t`information_schema`.`KEY_COLUMN_USAGE`\n" +
                "WHERE \n" +
                "\tTABLE_NAME = ?\n" +
                "    AND COLUMN_NAME = ?|?|? \n" +
                "\tAND CONSTRAINT_NAME != 'PRIMARY'"
        );

        psJoinTableInfoQuery.setString(1, joinTableName);
        psJoinTableInfoQuery.setString(2, joinTablePKNames.remove(0));
        psJoinTableInfoQuery.setString(3, joinTablePKNames.remove(0));
        psJoinTableInfoQuery.setString(4, joinTablePKNames.remove(0));
        ResultSet rs = psJoinTableInfoQuery.executeQuery();


        Map<String, LinkedList<String>> joinedTableData = new HashMap<>();
        while(rs.next()){
            LinkedList<String> data = new LinkedList<>();
            data.push(rs.getString("COLUMN_NAME"));
            data.push(rs.getString("REFERENCED_COLUMN_NAME"));
            joinedTableData.put(rs.getString("REFERENCED_TABLE_NAME"), data);
        }

        List<Integer> fIdsOne = getTablePKEntries("employees", con, joinedTableData.get("employees").pop());
        List<Integer> fIdsTwo = getTablePKEntries("job_positions", con, joinedTableData.get("job_positions").pop());


        Map<Integer, Set<Integer>> addressBook = new HashMap<>();
        ResultSet rSet = con.createStatement().executeQuery("SELECT * FROM task02.address_book ");
        while(rSet.next()){
            int fid_company = rSet.getInt("fid_company");
            int fid_address = rSet.getInt("fid_address");
            boolean found = false;
            for (Map.Entry<Integer, Set<Integer>> entry : addressBook.entrySet()) {
                if(entry.getKey().equals(fid_address)){
                    entry.getValue().add(fid_company);
                    found = true;
                    break;
                }
            }
            if(!found){
                HashSet<Integer> allowedCompanies = new HashSet<>();
                allowedCompanies.add(fid_company);
                addressBook.put(fid_address, allowedCompanies);
            }
        }

        Map<Integer, Set<Integer>> allowedCompanies = new HashMap<>();
        ResultSet rSet2 = con.createStatement().executeQuery("SELECT * FROM task02.employees ");
        while(rSet2.next()){
            int id_employee = rSet2.getInt("id_employee");
            int fid_address = rSet2.getInt("fid_address");
            for (Map.Entry<Integer, Set<Integer>> entry : addressBook.entrySet()) {
                if(entry.getKey().equals(fid_address)){
                    allowedCompanies.put(id_employee, entry.getValue());
                }
            }
        }
        joinedTableData.get("companies").pop();
        String updateJoinTableStatement;
        if(fIdsOne.size() > fIdsTwo.size()){
            List<Integer> tmp = fIdsOne;
            fIdsOne = fIdsTwo;
            fIdsTwo = tmp;
            updateJoinTableStatement = String.format(
                    "INSERT INTO `task02`.`%s` (`%s`, `%s`, `%s`) VALUES(?,?,?)",
                    joinTableName,
                    joinedTableData.get("job_positions").pop(),
                    joinedTableData.get("employees").pop(),
                    joinedTableData.get("companies").pop()
            );
        }else {
            updateJoinTableStatement = String.format(
                    "INSERT INTO `task02`.`%s` (`%s`, `%s`, `%s`) VALUES(?,?,?)",
                    joinTableName,
                    joinedTableData.get("employees").pop(),
                    joinedTableData.get("job_positions").pop(),
                    joinedTableData.get("companies").pop()
            );
        }

        PreparedStatement psUpdateJoinTable = con.prepareStatement(updateJoinTableStatement);
        con.setAutoCommit(false);

        Map<Integer, List<Integer>> duplicateRegister = new HashMap<>();
        for (Integer idOne : fIdsOne) {
            duplicateRegister.put(idOne, new ArrayList<>());
        }

        try {
            for (int i = 0; i < overlay; i++) {
                LinkedList<Integer> cpfIdsOne = new LinkedList<>(fIdsOne);
                LinkedList<Integer> cpfIdsTwo = new LinkedList<>(fIdsTwo);
                while (!cpfIdsOne.isEmpty()) {
                    Integer idOne = cpfIdsOne.pop();
                    Integer idTwo = cpfIdsTwo.remove(Math.abs(random.nextInt() % cpfIdsTwo.size()));

                    if(isDuplicate(idOne, idTwo, duplicateRegister)){
                        continue;
                    }

                    Set<Integer> allowedComIds = allowedCompanies.get(idOne);
                    ArrayList<Integer> ids = new ArrayList<>(allowedComIds);

                    psUpdateJoinTable.setInt(1, idOne);
                    psUpdateJoinTable.setInt(2, idTwo);
                    psUpdateJoinTable.setInt(3, ids.get(Math.abs(random.nextInt() % ids.size())));
                    psUpdateJoinTable.executeUpdate();
                }
                while (!cpfIdsTwo.isEmpty()){
                    Integer idOne = fIdsOne.get(Math.abs(random.nextInt() % fIdsOne.size()));
                    Integer idTwo = cpfIdsTwo.remove(Math.abs(random.nextInt() % cpfIdsTwo.size()));

                    if(isDuplicate(idOne, idTwo, duplicateRegister)){
                        continue;
                    }

                    Set<Integer> allowedComIds = allowedCompanies.get(idOne);
                    ArrayList<Integer> ids = new ArrayList<>(allowedComIds);

                    psUpdateJoinTable.setInt(1, idOne);
                    psUpdateJoinTable.setInt(2, idTwo);
                    psUpdateJoinTable.setInt(3, ids.get(Math.abs(random.nextInt() % ids.size())));
                    psUpdateJoinTable.executeUpdate();
                }
                con.commit();
            }
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException("join table population failure", e);
        }
    }


    private static void populateAddressBook(String joinTableName,
                                            int overlay,
                                            Connection con) throws SQLException {
        List<String> joinTablePKNames = getPKNames(joinTableName, con);
        PreparedStatement psJoinTableInfoQuery = con.prepareStatement("SELECT *\n" +
                "FROM\n" +
                "\t`information_schema`.`KEY_COLUMN_USAGE`\n" +
                "WHERE \n" +
                "\tTABLE_NAME = ?\n" +
                "    AND COLUMN_NAME = ?|?\n" +
                "\tAND CONSTRAINT_NAME != 'PRIMARY'"
        );

        psJoinTableInfoQuery.setString(1, joinTableName);
        psJoinTableInfoQuery.setString(2, joinTablePKNames.remove(0));
        psJoinTableInfoQuery.setString(3, joinTablePKNames.remove(0));
        ResultSet rs = psJoinTableInfoQuery.executeQuery();
        rs.next();
        String tableOneName = rs.getString("REFERENCED_TABLE_NAME");
        String pkOne = rs.getString("REFERENCED_COLUMN_NAME");
        String fkOne = rs.getString("COLUMN_NAME");
        rs.next();
        String tableTwoName = rs.getString("REFERENCED_TABLE_NAME");
        String pkTwo = rs.getString("REFERENCED_COLUMN_NAME");
        String fkTwo = rs.getString("COLUMN_NAME");

        List<Integer> fIdsOne = getTablePKEntries(tableOneName, con, pkOne);
        List<Integer> fIdsTwo = getTablePKEntries(tableTwoName, con, pkTwo);

        String updateJoinTableStatement;
        if(fIdsOne.size() > fIdsTwo.size()){
            List<Integer> tmp = fIdsOne;
            fIdsOne = fIdsTwo;
            fIdsTwo = tmp;
            updateJoinTableStatement = String.format(
                    "INSERT INTO `task02`.`%s` (`%s`, `%s`) VALUES(?,?)",
                    joinTableName,
                    fkTwo,
                    fkOne
                    );
        }else {
            updateJoinTableStatement = String.format(
                    "INSERT INTO `task02`.`%s` (`%s`, `%s`) VALUES(?,?)",
                    joinTableName,
                    fkOne,
                    fkTwo
                    );
        }
        PreparedStatement psUpdateJoinTable = con.prepareStatement(updateJoinTableStatement);
        con.setAutoCommit(false);

        Map<Integer, List<Integer>> duplicateRegister = new HashMap<>();
        for (Integer idOne : fIdsOne) {
            duplicateRegister.put(idOne, new ArrayList<>());
        }

        try {
            for (int i = 0; i < overlay; i++) {
                LinkedList<Integer> cpfIdsOne = new LinkedList<>(fIdsOne);
                LinkedList<Integer> cpfIdsTwo = new LinkedList<>(fIdsTwo);
                while (!cpfIdsOne.isEmpty()) {
                    Integer idOne = cpfIdsOne.pop();
                    Integer idTwo = cpfIdsTwo.remove(Math.abs(random.nextInt() % cpfIdsTwo.size()));

                    if(isDuplicate(idOne, idTwo, duplicateRegister)){
                        continue;
                    }

                    psUpdateJoinTable.setInt(1, idOne);
                    psUpdateJoinTable.setInt(2, idTwo);
                    psUpdateJoinTable.executeUpdate();
                }
                while (!cpfIdsTwo.isEmpty()){
                    Integer idOne = fIdsOne.get(Math.abs(random.nextInt() % fIdsOne.size()));
                    Integer idTwo = cpfIdsTwo.remove(Math.abs(random.nextInt() % cpfIdsTwo.size()));

                    if(isDuplicate(idOne, idTwo, duplicateRegister)){
                        continue;
                    }

                    psUpdateJoinTable.setInt(1, idOne);
                    psUpdateJoinTable.setInt(2, idTwo);
                    psUpdateJoinTable.executeUpdate();
                }
                con.commit();
            }
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException("join table population failure", e);
        }
    }

    private static boolean isDuplicate(Integer idOne,
                                       Integer idTwo,
                                       Map<Integer, List<Integer>> duplicateRegister) {
        for (Map.Entry<Integer, List<Integer>> entry : duplicateRegister.entrySet()) {
            if(entry.getKey().equals(idOne)){
                if(entry.getValue().contains(idTwo)) {
                    return true;
                }else{
                    entry.getValue().add(idTwo);
                    return false;
                }
            }
        }
        throw new RuntimeException("unknown entry");
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

    private static List<String> getPKNames(String tableOneName, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                String.format("SHOW KEYS FROM %s WHERE Key_name = 'PRIMARY'", tableOneName)
        );
        List<String> pkOne = new ArrayList<>();
        while(resultSet.next()){
            pkOne.add(resultSet.getString("Column_name"));
        }
        return pkOne;
    }

    private static void populatePositions(Connection con,
                                          PreparedStatement psPositions,
                                          int capacity) throws SQLException {
        try {
            for (int i = 1; i <= capacity; i++) {
                RandomGenerators.PositionGen positionGen = new RandomGenerators.PositionGen();
                psPositions.setString(1, positionGen.next().getName());
                psPositions.executeUpdate();
            }
            con.commit();
        }catch (SQLException e){
            con.rollback();
            throw new RuntimeException();
        }
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
                if(cpyAddrPKEntries.size() == 0){
                    cpyAddrPKEntries.addAll(addrPKEntries);
                }
                psEmployees.setString(1, employeeGen.next().getFirstName());
                psEmployees.setString(2, employeeGen.next().getLastName());
                psEmployees.setInt(3, cpyAddrPKEntries.remove(Math.abs(random.nextInt() % cpyAddrPKEntries.size())));
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
            for (int i = 1; i <= capacity; i++) {
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

        for (int cityId = 1; cityId <= citiesInDB; cityId++) {
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

        for (int countryId = 1; countryId <= countriesInDB; countryId++) {
            psSelectCountryById.setInt(1, countryId);
            ResultSet rs = psSelectCountryById.executeQuery();
            Country country = new Country();

            if(rs.next()) {
                country.setCountry(rs.getString("country"));
                country.setCountryId(countryId);
            }else {
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
            for (int i = 1; i <= maxCapacity; i++) {
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

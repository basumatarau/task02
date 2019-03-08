package by.htp.basumatarau.sql.dao.impl;

import by.htp.basumatarau.sql.dao.DAO;
import by.htp.basumatarau.sql.dao.beans.*;
import by.htp.basumatarau.sql.dao.connection.ConnectionSource;
import by.htp.basumatarau.sql.dao.util.Tuple;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDAOImpl implements DAO<Employee, Integer> {

    private final static String INSERT_NEW_EMPLOYEE_INTO_EMPLOYEES
            = "INSERT INTO `employees` (`first_name`, `last_name`, `fid_address`) VALUES(?,?,?)";
    private final static String SELECT_EMPLOYEE_BY_ID
            = "SELECT * FROM `employees` WHERE `id_employee`=?";
    private final static String DELETE_EMPLOYEE
            = "DELETE FROM `employees` WHERE (`id_employee`, `first_name`, `last_name`, `fid_address`) VALUES(?,?,?,?)";
    private final static String SELECT_EMPLOYEES = "SELECT * FROM `employees` LIMIT ?,? ";

    private final static String SELECT_EMPLOYEES_DETAILED = "";

    public Map<
            Employee,
            List<Tuple<Company, City, Country, Address, Integer, Position>>
            > getDetailed(int numEntries, int startingFrom){
        //TODO to be implemented

        Map<Employee,
                List<Tuple<Company, City, Country, Address, Integer, Position>>
                > result = new HashMap<>();

        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_EMPLOYEES_DETAILED);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Employee employee = new Employee();
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setEmployeeId(resultSet.getInt("id_employee"));
                employee.setFidAddress(resultSet.getInt("fid_address"));



            }
        } catch (SQLException e) {
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Employee entity) {
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
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Employee read(Integer id) {
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
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public boolean delete(Employee entity) {
        int changedEntries = -1;
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
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return changedEntries == 1;
    }

    @Override
    public List<Employee> read(int numEntries, int startingFrom) {
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
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return result;
    }
}

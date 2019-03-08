package by.htp.basumatarau.sql.dao.impl;

import by.htp.basumatarau.sql.dao.DAO;
import by.htp.basumatarau.sql.dao.beans.Country;
import by.htp.basumatarau.sql.dao.connection.ConnectionSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAOImpl implements DAO<Country, Integer> {
    private final static String INSERT_NEW_COUNTRY_INTO_COUNTRIES
            = "INSERT INTO `countries` (`country`) VALUES(?)";
    private final static String SELECT_COUNTRY_BY_ID
            = "SELECT * FROM `countries` WHERE `id_country`=?";
    private final static String DELETE_COUNTRY
            = "DELETE FROM `countries` WHERE (`id_country`, `country`) VALUES(?,?)";
    private final static String SELECT_COUNTRIES = "SELECT * FROM `countries` LIMIT ?,? ";


    @Override
    public boolean create(Country entity) {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_COUNTRY_INTO_COUNTRIES, Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getCountry());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    entity.setCountryId(id);
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
    public Country read(Integer id) {
        Country country = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_COUNTRY_BY_ID, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                country = new Country();
                country.setCountry(resultSet.getString("country"));
                country.setCountryId(resultSet.getInt("id_country"));
            }
        } catch (SQLException e) {
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return country;
    }

    @Override
    public boolean delete(Country entity) {
        int changedEntries = -1;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_COUNTRY);
            try{
                ps.setInt(1, entity.getCountryId());
                ps.setString(2, entity.getCountry());
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
    public List<Country> read(int numEntries, int startingFrom) {
        List<Country> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_COUNTRIES);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Country country = new Country();
                country.setCountry(resultSet.getString("country"));
                country.setCountryId(resultSet.getInt("id_country"));
                result.add(country);
            }
        } catch (SQLException e) {
            //TODO dao exception to be thrown here
            e.printStackTrace();
        }
        return result;
    }
}

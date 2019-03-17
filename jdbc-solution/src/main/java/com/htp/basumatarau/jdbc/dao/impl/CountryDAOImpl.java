package com.htp.basumatarau.jdbc.dao.impl;

import com.htp.basumatarau.jdbc.dao.DAO;
import com.htp.basumatarau.jdbc.dao.beans.Country;
import com.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import com.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAOImpl implements DAO<Country, Integer> {
    private static final Logger log = Logger.getLogger(CountryDAOImpl.class);

    private final static String INSERT_NEW_COUNTRY_INTO_COUNTRIES
            = "INSERT INTO `countries` (`country`) VALUES(?)";
    private final static String SELECT_COUNTRY_BY_ID
            = "SELECT * FROM `countries` WHERE `id_country`=?";
    private final static String DELETE_COUNTRY
            = "DELETE FROM `countries` WHERE (`id_country`, `country`) VALUES(?,?)";
    private final static String SELECT_COUNTRIES = "SELECT * FROM `countries` LIMIT ?,? ";


    @Override
    public boolean create(Country entity) throws PersistenceException {
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
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public Country read(Integer id) throws PersistenceException {
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
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return country;
    }

    @Override
    public boolean delete(Country entity) throws PersistenceException {
        int changedEntries;
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
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return changedEntries == 1;
    }

    @Override
    public List<Country> read(int numEntries, int startingFrom) throws PersistenceException {
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
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }
}

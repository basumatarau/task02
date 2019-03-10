package by.htp.basumatarau.jdbc.dao.impl;

import by.htp.basumatarau.jdbc.dao.DAO;
import by.htp.basumatarau.jdbc.dao.beans.City;
import by.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAOImpl implements DAO<City, Integer> {
    private static final Logger log = Logger.getLogger(CityDAOImpl.class);
    private final static String INSERT_NEW_CITY_INTO_CITIES
            = "INSERT INTO `cities` (`city`, `fid_country`) VALUES(?,?)";
    private final static String SELECT_CITY_BY_ID
            = "SELECT * FROM `cities` WHERE `id_city`=?";
    private final static String DELETE_CITY
            = "DELETE FROM `cities` WHERE (`id_city`, `city`, `fid_country`) VALUES(?,?,?)";
    private final static String SELECT_CITIES = "SELECT * FROM `cities` LIMIT ?,? ";

    @Override
    public boolean create(City entity) throws PersistenceException {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_CITY_INTO_CITIES, Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getCity());
                ps.setInt(2, entity.getFidCountry());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    entity.setCityId(id);
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
    public City read(Integer id) throws PersistenceException {
        City city = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_CITY_BY_ID, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                city = new City();
                city.setCity(resultSet.getString("city"));
                city.setCityId(resultSet.getInt("id_city"));
                city.setFidCountry(resultSet.getInt("fid_country"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return city;
    }

    @Override
    public boolean delete(City entity) throws PersistenceException {
        int changedEntries;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_CITY);
            try{
                ps.setInt(1, entity.getCityId());
                ps.setString(2, entity.getCity());
                ps.setInt(3, entity.getFidCountry());
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
    public List<City> read(int numEntries, int startingFrom) throws PersistenceException {
        List<City> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_CITIES);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                City city = new City();
                city.setCity(resultSet.getString("city"));
                city.setCityId(resultSet.getInt("id_city"));
                city.setFidCountry(resultSet.getInt("fid_country"));
                result.add(city);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }

}

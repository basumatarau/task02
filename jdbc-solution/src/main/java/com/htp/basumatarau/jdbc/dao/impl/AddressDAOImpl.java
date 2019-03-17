package com.htp.basumatarau.jdbc.dao.impl;

import com.htp.basumatarau.jdbc.dao.DAO;
import com.htp.basumatarau.jdbc.dao.beans.Address;
import com.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import com.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements DAO<Address, Integer> {
    private static final Logger log = Logger.getLogger(AddressDAOImpl.class);
    private final static String INSERT_NEW_ADDRESS_INTO_ADDRESSES
            = "INSERT INTO `addresses` (`address`, `fid_city`) VALUES(?,?)";
    private final static String SELECT_ADDRESS_BY_ID
            = "SELECT * FROM `addresses` WHERE `id_address`=?";
    private final static String DELETE_ADDRESS
            = "DELETE FROM `addresses` WHERE (`id_address`, `address`, `fid_city`) VALUES(?,?,?)";
    private final static String SELECT_ADDRESSES = "SELECT * FROM `addresses` LIMIT ?,? ";

    @Override
    public boolean create(Address entity) throws PersistenceException {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_ADDRESS_INTO_ADDRESSES,
                    Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getAddress());
                ps.setInt(2, entity.getFidCity());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    entity.setId(id);
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
    public Address read(Integer id) throws PersistenceException {
        Address address = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_ADDRESS_BY_ID,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                address = new Address();
                address.setAddress(resultSet.getString("address"));
                address.setFidCity(resultSet.getInt("fid_city"));
                address.setId(resultSet.getInt("id_address"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return address;
    }

    @Override
    public boolean delete(Address entity) throws PersistenceException {
        int changedEntries = -1;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_ADDRESS);
            try{
                ps.setInt(1, entity.getId());
                ps.setString(2, entity.getAddress());
                ps.setInt(3, entity.getFidCity());
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
    public List<Address> read(int numEntries, int startingFrom) throws PersistenceException {
        List<Address> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_ADDRESSES);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Address address = new Address();
                address.setAddress(resultSet.getString("address"));
                address.setId(resultSet.getInt("id_address"));
                address.setFidCity(resultSet.getInt("fid_city"));
                result.add(address);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }
}

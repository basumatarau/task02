package com.htp.basumatarau.jdbc.dao.impl;

import com.htp.basumatarau.jdbc.dao.DAO;
import com.htp.basumatarau.jdbc.dao.beans.Company;
import com.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import com.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements DAO<Company, Integer> {
    private static final Logger log = Logger.getLogger(CompanyDAOImpl.class);

    private final static String INSERT_NEW_COMPANY_INTO_COMPANIES
            = "INSERT INTO `companies` (`name`) VALUES(?)";
    private final static String SELECT_COMPANY_BY_ID
            = "SELECT * FROM `companies` WHERE `id_company`=?";
    private final static String DELETE_COMPANY
            = "DELETE FROM `companies` WHERE (`id_company`, `name`) VALUES(?,?)";
    private final static String SELECT_COMPANIES = "SELECT * FROM `companies` LIMIT ?,? ";

    @Override
    public boolean create(Company entity) throws PersistenceException {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_COMPANY_INTO_COMPANIES,
                    Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getName());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    entity.setCompanyId(id);
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
    public Company read(Integer id) throws PersistenceException {
        Company company = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_COMPANY_BY_ID,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                company = new Company();
                company.setName(resultSet.getString("name"));
                company.setCompanyId(resultSet.getInt("id_company"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return company;
    }

    @Override
    public boolean delete(Company entity) throws PersistenceException {
        int changedEntries;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_COMPANY);
            try{
                ps.setInt(1, entity.getCompanyId());
                ps.setString(2, entity.getName());
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
    public List<Company> read(int numEntries, int startingFrom) throws PersistenceException {
        List<Company> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_COMPANIES);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Company company = new Company();
                company.setName(resultSet.getString("name"));
                company.setCompanyId(resultSet.getInt("id_company"));
                result.add(company);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }
}

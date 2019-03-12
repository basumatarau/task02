package by.htp.basumatarau.jdbc.dao.impl;

import by.htp.basumatarau.jdbc.dao.DAO;
import by.htp.basumatarau.jdbc.dao.beans.Position;
import by.htp.basumatarau.jdbc.dao.connection.ConnectionSource;
import by.htp.basumatarau.jdbc.dao.exception.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionDAOImpl implements DAO<Position, Integer> {
    private static final Logger log = Logger.getLogger(PositionDAOImpl.class);

    private final static String INSERT_NEW_POSITION_INTO_POSITIONS
            = "INSERT INTO `job_positions` (`position`) VALUES(?)";
    private final static String SELECT_POSITION_BY_ID
            = "SELECT * FROM `job_positions` WHERE `id_job_position`=?";
    private final static String DELETE_POSITION
            = "DELETE FROM `job_positions` WHERE (`id_job_position`, `position`) VALUES(?,?)";
    private final static String SELECT_POSITION = "SELECT * FROM `job_positions` LIMIT ?,? ";

    @Override
    public boolean create(Position entity) throws PersistenceException {
        boolean result = false;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_POSITION_INTO_POSITIONS,
                    Statement.RETURN_GENERATED_KEYS);
            try{
                ps.setString(1, entity.getName());
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
    public Position read(Integer id) throws PersistenceException {
        Position position = null;
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_POSITION_BY_ID, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                position = new Position();
                position.setName(resultSet.getString("position"));
                position.setId(resultSet.getInt("id_job_position"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return position;
    }

    @Override
    public boolean delete(Position entity) throws PersistenceException {
        int changedEntries;
        try (Connection con = ConnectionSource.yieldConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(DELETE_POSITION);
            try{
                ps.setInt(1, entity.getId());
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
    public List<Position> read(int numEntries, int startingFrom) throws PersistenceException {
        List<Position> result = new ArrayList<>();
        try (Connection con = ConnectionSource.yieldConnection()){
            PreparedStatement ps = con.prepareStatement(SELECT_POSITION);
            ps.setInt(1, startingFrom);
            ps.setInt(2, numEntries);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                Position position = new Position();
                position.setName(resultSet.getString("position"));
                position.setId(resultSet.getInt("id_job_position"));
                result.add(position);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new PersistenceException(e);
        }
        return result;
    }
}

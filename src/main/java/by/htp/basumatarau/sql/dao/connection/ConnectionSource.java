package by.htp.basumatarau.sql.dao.connection;

import org.apache.commons.dbcp2.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionSource {

    private static BasicDataSource basicDataSource;

    static {
        dataSourceInit();
    }

    private static void dataSourceInit(){
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(ConnectionProperties.URL_CONTEXT);
        basicDataSource.setUsername(ConnectionProperties.USER_DB);
        basicDataSource.setPassword(ConnectionProperties.PASSWORD_DB);
        basicDataSource.setMinIdle(2);
        basicDataSource.setMaxIdle(4);
        basicDataSource.setMaxOpenPreparedStatements(10);
    }

    private ConnectionSource(){}

    public static Connection yieldConnection() throws SQLException {
        return basicDataSource.getConnection();
    }

}

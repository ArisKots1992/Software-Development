package database;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBConnector_POOL {

    private static DBConnector_POOL     DBConnector_POOL;
    private BoneCP connectionPool;

    private DBConnector_POOL() throws IOException, SQLException, PropertyVetoException {
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            // setup the connection pool using BoneCP Configuration
            BoneCPConfig config = new BoneCPConfig();
            // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
            config.setJdbcUrl("jdbc:mysql://localhost/Database");
            config.setUsername("root");
            config.setPassword("aris");
            config.setMinConnectionsPerPartition(1);
            config.setMaxConnectionsPerPartition(100);
            config.setPartitionCount(1);
            config.setPoolAvailabilityThreshold(20);
            config.setReleaseHelperThreads(10);
            // setup the connection pool
            connectionPool = new BoneCP(config);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public static DBConnector_POOL getInstance() throws IOException, SQLException, PropertyVetoException {
        if (DBConnector_POOL == null) {
            DBConnector_POOL = new DBConnector_POOL();
            return DBConnector_POOL;
        } else {
            return DBConnector_POOL;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection();
    }

}

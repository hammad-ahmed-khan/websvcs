// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

import java.sql.SQLException;
import java.sql.Driver;
import java.sql.DriverManager;
import oracle.jdbc.driver.OracleDriver;
import java.sql.Connection;
import com.logicinfo.extra.util.PropertiesReader;
import org.apache.log4j.Logger;

public class ExtraDBConnection
{
    private static Logger LOGGER;
    public String DBURL;
    public String DBUsername;
    public String DBPassword;
    public String dbType;
    
    static {
        ExtraDBConnection.LOGGER = Logger.getLogger((Class)ExtraDBConnection.class);
    }
    
    public void loadProperties(final String dbType) {
        if (dbType.equals("sim")) {
            ExtraDBConnection.LOGGER.info((Object)"start loading sim properties");
            this.DBURL = PropertiesReader.getProperty("sim14.db.jdbc-url");
            this.DBUsername = PropertiesReader.getProperty("sim14.db.user");
            this.DBPassword = PropertiesReader.getProperty("sim14.db.password");
        }
        else if (dbType.equals("wms")) {
            ExtraDBConnection.LOGGER.info((Object)"start loading wms properties");
            this.DBURL = PropertiesReader.getProperty("wms.db.jdbc-url");
            this.DBUsername = PropertiesReader.getProperty("wms.db.user");
            this.DBPassword = PropertiesReader.getProperty("wms.db.password");
        }
    }
    
    public Connection getDBConnection() throws Exception {
        Connection jdbcConnection = null;
        if (this.DBURL != null && this.DBUsername != null && this.DBUsername != null && !this.DBURL.trim().equals("") && !this.DBUsername.trim().equals("") && !this.DBUsername.trim().equals("")) {
            try {
                DriverManager.registerDriver((Driver)new OracleDriver());
                jdbcConnection = DriverManager.getConnection(this.DBURL, this.DBUsername, this.DBPassword);
                ExtraDBConnection.LOGGER.info((Object)(" Connected driver Version:" + jdbcConnection.getMetaData().getDriverVersion()));
                return jdbcConnection;
            }
            catch (SQLException e) {
                ExtraDBConnection.LOGGER.error((Object)("SQLException at getConnection " + e));
                throw new Exception("Exception occured while opening the db connection: " + e.getMessage());
            }
        }
        ExtraDBConnection.LOGGER.error((Object)"Exception occured while reading the db properties");
        throw new Exception("Exception occured while reading the db properties");
    }
    
    public static void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException exception) {
                ExtraDBConnection.LOGGER.error((Object)("Exception while closing the DB Connection: " + exception));
            }
        }
    }
}

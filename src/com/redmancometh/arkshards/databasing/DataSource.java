package com.redmancometh.arkshards.databasing;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource
{
    private HikariDataSource datasource;

    public DataSource(String host, int port, String database, String username, String password) throws IOException, SQLException, PropertyVetoException
    {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(50);
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", host);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", database);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.setPoolName("Arkshards");
        datasource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException
    {
        return datasource.getConnection();
    }
}
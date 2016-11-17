package com.redmancometh.arkshards;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.redmancometh.arkshards.commands.ShardCommandExecutor;
import com.redmancometh.arkshards.commands.WithdrawCommandExecutor;
import com.redmancometh.arkshards.config.ConfigManager;
import com.redmancometh.arkshards.databasing.DataSource;
import com.redmancometh.arkshards.databasing.DatabaseManager;
import com.redmancometh.arkshards.listeners.PlayerListeners;

public class ArkShards extends JavaPlugin
{
    private DataSource dataSource;
    private ShardManager shardManager;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private static ArkShards instance;
    private Executor pool;

    public void onEnable()
    {
        loadConfig();
        instance = this;
        shardManager = new ShardManager();
        pool = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("arkshards-thread-%d").build());
        try
        {
            Bukkit.getLogger().log(Level.INFO, ChatColor.BLUE + "Arkshards has started up! Initializing datasources...");
            dataSource = new DataSource(configManager.getHost(), configManager.getPort(), configManager.getDatabase(), configManager.getUser(), configManager.getPassword());
            try (Connection ignored = dataSource.getConnection())
            {
                Bukkit.getLogger().log(Level.INFO, ChatColor.BLUE + "Arkshards datasource initialized!");
            }
        }
        catch (SQLException | IOException | PropertyVetoException e)
        {
            e.printStackTrace();
            for (int x = 0; x < 5; x++)
            {
                Bukkit.getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "Could not acquire datasource connection with SQL DB! Shutting down to prevent exploitation.");
            }
        }
        databaseManager = new DatabaseManager();
        databaseManager.createTable();
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
        getCommand("arkshards").setExecutor(new ShardCommandExecutor());
        getCommand("iswithdraw").setExecutor(new WithdrawCommandExecutor());
    }

    public Connection getConnection()
    {
        try
        {
            return this.dataSource.getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void loadConfig()
    {
        this.configManager = new ConfigManager(this);
        configManager.init();
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }

    public Executor getPool()
    {
        return pool;
    }

    public static ArkShards getInstance()
    {
        return instance;
    }

    public DatabaseManager getDatabaseManager()
    {
        return databaseManager;
    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }

}

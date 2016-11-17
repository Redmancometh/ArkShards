package com.redmancometh.arkshards.databasing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import com.redmancometh.arkshards.ArkShards;

public class DatabaseManager
{
    public Connection getConnection()
    {
        return ArkShards.getInstance().getConnection();
    }

    public void createTable()
    {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("create table if not exists shards(uuid varchar(64) NOT NULL PRIMARY KEY, count int)"))
        {
            ps.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setShards(UUID uuid, int amount)
    {
        CompletableFuture.runAsync(() ->
        {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO shards(uuid, count) VALUES (?,?) ON DUPLICATE KEY UPDATE count=?"))
            {
                ps.setString(1, uuid.toString());
                ps.setInt(2, amount);
                ps.setInt(3, amount);
                ps.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }, ArkShards.getInstance().getPool());
    }

    public void initPlayer(UUID uuid)
    {
        CompletableFuture.runAsync(() ->
        {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO shards(uuid, count) VALUES (?,0)"))
            {
                ps.setString(1, uuid.toString());
                ps.execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }, ArkShards.getInstance().getPool());
    }

    public CompletableFuture<AtomicInteger> getShards(UUID uuid)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT count from shards where uuid=?"))
            {
                ps.setString(1, uuid.toString());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return new AtomicInteger(rs.getInt("count"));
                    }
                    initPlayer(uuid);
                    return new AtomicInteger(0);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }, ArkShards.getInstance().getPool());
    }
}

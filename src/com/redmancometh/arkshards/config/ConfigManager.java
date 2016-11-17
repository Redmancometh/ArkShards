package com.redmancometh.arkshards.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager
{
    private String host;
    private String database;
    private String user;
    private String password;
    private int port;
    private FileConfiguration config;
    private Material type;
    private String itemName;
    private List<String> lore;
    private ItemStack shardItem;
    private String withdrawSuccessMessage;
    private String withdrawFailMessage;
    private String depositMessage;
    private String prefix;
    private String balanceMessage;
    private String notEnoughShards;
    private String withdrawalUsage;

    public ConfigManager(JavaPlugin pl)
    {
        File configFile = new File(pl.getDataFolder(), "arkshards.yml");
        if (!configFile.exists())
        {
            pl.saveResource("arkshards.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void initDBInfo()
    {
        this.user = config.getString("SQL.username");
        this.password = config.getString("SQL.password");
        this.database = config.getString("SQL.database");
        this.setHost(config.getString("SQL.host"));
        this.port = config.getInt("SQL.port", 3306);

    }

    public void initItemInfo()
    {
        this.itemName = ChatColor.translateAlternateColorCodes('&', config.getString("Item.name"));
        this.lore = new ArrayList(Arrays.asList(ChatColor.translateAlternateColorCodes('&', config.getString("Item.lore"))));
        this.type = Material.getMaterial(config.getString("Item.material"));
        if (config.isSet("Item.datavalue"))
        {
            this.setShardItem(new ItemStack(type, 1, (short) 0, (byte) config.getInt("Item.datavalue")));
            return;
        }
        this.setShardItem(new ItemStack(type));
        ItemMeta meta = this.shardItem.getItemMeta();
        meta.setLore(this.lore);
        meta.setDisplayName(this.itemName);
        this.shardItem.setItemMeta(meta);
    }

    public void init()
    {
        initDBInfo();
        initItemInfo();
        initMessageInfo();
    }

    public void initMessageInfo()
    {
        this.withdrawFailMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.withdraw_fail"));
        this.withdrawSuccessMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.withdraw_succeed"));
        this.depositMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.deposit"));
        this.prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix"));
        this.setBalanceMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.balance_message")));
        this.setNotEnoughShards(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.not_enough_shards")));
        this.setWithdrawalUsage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.withdrawal_usage")));
    }

    public FileConfiguration getBukkitConfig()
    {
        return config;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getDatabase()
    {
        return database;
    }

    public void setDatabase(String database)
    {
        this.database = database;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public Material getType()
    {
        return type;
    }

    public void setType(Material type)
    {
        this.type = type;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String name)
    {
        this.itemName = name;
    }

    public List<String> getLore()
    {
        return lore;
    }

    public void setLore(List<String> lore)
    {
        this.lore = lore;
    }

    public ItemStack getShardItem()
    {
        return shardItem;
    }

    public void setShardItem(ItemStack shardItem)
    {
        this.shardItem = shardItem;
    }

    public String getWithdrawMessage()
    {
        return withdrawSuccessMessage;
    }

    public void setWithdrawMessage(String withdrawMessage)
    {
        this.withdrawSuccessMessage = withdrawMessage;
    }

    public String getDepositMessage()
    {
        return depositMessage;
    }

    public void setDepositMessage(String depositMessage)
    {
        this.depositMessage = depositMessage;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getWithdrawFailMessage()
    {
        return withdrawFailMessage;
    }

    public void setWithdrawFailMessage(String withdrawFailMessage)
    {
        this.withdrawFailMessage = withdrawFailMessage;
    }

    public String getBalanceMessage()
    {
        return balanceMessage;
    }

    public void setBalanceMessage(String balanceMessage)
    {
        this.balanceMessage = balanceMessage;
    }

    public String getNotEnoughShards()
    {
        return notEnoughShards;
    }

    public void setNotEnoughShards(String notEnoughShards)
    {
        this.notEnoughShards = notEnoughShards;
    }

    public String getWithdrawalUsage()
    {
        return withdrawalUsage;
    }

    public void setWithdrawalUsage(String withdrawalUsage)
    {
        this.withdrawalUsage = withdrawalUsage;
    }

}

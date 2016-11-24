package com.redmancometh.arkshards.commands;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.redmancometh.arkshards.ArkShards;
import com.redmancometh.arkshards.util.ItemUtil;

public class ShardCommandExecutor implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        if (!sender.isOp())
        {
            checkBalance((Player) sender);
            return true;
        }
        switch (args[0].toLowerCase())
        {
            case "set":
                setShards(sender, args);
                break;
            case "get":
                checkShards(sender, args);
                break;
            case "check":
                checkShards(sender, args);
                break;
            case "remove":
                removeShards(sender, args);
                break;
            case "add":
                addShards(sender, args);
                break;
            case "giveitem":
                giveShardItem(sender, args);
        }
        return false;
    }

    public void checkBalance(Player p)
    {
        //Just to have another code path for regular players. For future stuff.
        ArkShards.getInstance().getShardManager().getShards(p.getUniqueId()).thenAccept((balance) -> messageBalance(p, balance.get()));
    }

    public void messageBalance(CommandSender sender, int balance)
    {
        sender.sendMessage(ArkShards.getInstance().getConfigManager().getBalanceMessage().replace("%s", balance + ""));
    }

    public void setShards(CommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Not enough arguments! Usage: /arkshards set [username] [amount]");
        }
        String targetPlayer = args[1];
        try
        {
            int amount = Integer.parseInt(args[2]);
            CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(targetPlayer), ArkShards.getInstance().getPool()).thenAccept((player) ->
            {
                if (player == null)
                {
                    sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Player not found!");
                    return;
                }
                UUID uuid = player.getUniqueId();
                ArkShards.getInstance().getShardManager().setShards(uuid, amount);
                sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.GREEN + "Successfully set player's shards!");
            });
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Please put in a valid number!");
        }
    }

    public void checkShards(CommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Not enough arguments! Usage: /arkshards check [username]");
        }
        String targetPlayer = args[1];
        CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(targetPlayer), ArkShards.getInstance().getPool()).thenAccept((player) ->
        {
            if (player == null)
            {
                sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Player not found!");
                return;
            }
            ArkShards.getInstance().getShardManager().getShards(player.getUniqueId()).thenAccept((
                    shards) -> sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.GREEN + player.getName() + "'s shard balance: " + shards));
        });

    }

    public void addShards(CommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Not enough arguments! Usage: /arkshards add [username] [amount]");
        }
        String targetPlayer = args[1];
        try
        {
            int amount = Integer.parseInt(args[2]);
            CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(targetPlayer), ArkShards.getInstance().getPool()).thenAccept((player) ->
            {
                if (player == null)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "Player not found!");
                    return;
                }
                UUID uuid = player.getUniqueId();
                ArkShards.getInstance().getShardManager().addShards(uuid, amount).thenAccept((newBal) -> sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.GREEN + "Player has " + newBal + " shards now!"));
            });
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Please put in a valid number!");
        }
    }

    public void removeShards(CommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Not enough arguments! Usage: /arkshards remove [username] [amount]");
        }
        String targetPlayer = args[1];
        try
        {
            int amount = Integer.parseInt(args[2]);
            CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(targetPlayer), ArkShards.getInstance().getPool()).thenAccept((player) ->
            {
                if (player == null)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "Player not found!");
                    return;
                }
                UUID uuid = player.getUniqueId();
                tryRemoveShards(uuid, amount, sender);
            });
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Please put in a valid number!");
        }
    }

    public void giveShardItem(CommandSender sender, String[] args)
    {
        if (args.length < 3)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Not enough arguments! Usage: /arkshards remove [username] [amount]");
        }
        String targetPlayer = args[1];
        try
        {
            int amount = Integer.parseInt(args[2]);
            CompletableFuture.supplyAsync(() -> Bukkit.getPlayer(targetPlayer), ArkShards.getInstance().getPool()).thenAccept((player) ->
            {
                if (player == null || !player.isOnline())
                {
                    sender.sendMessage(ChatColor.DARK_RED + "Player not found!");
                    return;
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(ArkShards.getInstance(), () -> player.getInventory().addItem(ItemUtil.getShardItem(amount)), 5);
                Bukkit.getLogger().log(Level.INFO, ArkShards.getInstance().getConfigManager().getPrefix() + " Player: " + targetPlayer + " was given " + amount + " shards!");
                sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " Player: " + targetPlayer + " was given " + amount + " shards!");
            });
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "Please put in a valid number!");
        }
    }

    public void tryRemoveShards(UUID uuid, int amount, CommandSender sender)
    {
        ArkShards.getInstance().getShardManager().removeShards(uuid, amount).thenAccept((result) ->
        {
            if (!result.isSuccess())
            {
                sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "User did not have enough shards!");
                return;
            }
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.GREEN + "Successfuly removed shards!");
        });
    }

}

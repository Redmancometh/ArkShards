package com.redmancometh.arkshards.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.redmancometh.arkshards.ArkShards;
import com.redmancometh.arkshards.util.ItemUtil;

import net.md_5.bungee.api.ChatColor;

public class WithdrawCommandExecutor implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getWithdrawalUsage());
            return true;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ChatColor.DARK_RED + "You can't do this from console.");
        }
        Player p = (Player) sender;
        try
        {
            int amount = Integer.parseInt(args[0]);
            ArkShards.getInstance().getShardManager().removeShards(p.getUniqueId(), amount).thenAccept((result) ->
            {
                if (!result.isSuccess())
                {
                    sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ArkShards.getInstance().getConfigManager().getWithdrawFailMessage().replace("%s", result.getAmountLeft() + "").replace("%as", amount + ""));
                    return;
                }
                sender.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ArkShards.getInstance().getConfigManager().getWithdrawMessage().replace("%s", result.getAmountLeft() + "").replace("%as", amount + ""));
                Bukkit.getScheduler().scheduleSyncDelayedTask(ArkShards.getInstance(), () -> ((Player) sender).getInventory().addItem(ItemUtil.getShardItem(amount)), 5);
            });
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ArkShards.getInstance().getConfigManager().getWithdrawalUsage());
        }
        return true;
    }

}

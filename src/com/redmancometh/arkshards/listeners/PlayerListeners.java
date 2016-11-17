package com.redmancometh.arkshards.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.redmancometh.arkshards.ArkShards;
import com.redmancometh.arkshards.util.ItemUtil;

public class PlayerListeners implements Listener
{

    @EventHandler
    public void onLogin(PlayerJoinEvent e)
    {
        //Load the player into the cache
        ArkShards.getInstance().getShardManager().getShards(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e)
    {
        ArkShards.getInstance().getShardManager().purgeAndSave(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void depositShards(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();
        String targetName = ArkShards.getInstance().getConfigManager().getItemName();
        List<String> targetLore = ArkShards.getInstance().getConfigManager().getLore();
        if (item != null && item.getType() == ArkShards.getInstance().getConfigManager().getType())
        {
            checkForShard(item, targetLore, targetName, p);
        }
    }

    public void checkForShard(ItemStack item, List<String> targetLore, String targetName, Player p)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(targetName))
        {
            if (targetLore.get(0).equals(meta.getLore().get(0)))
            {
                //Hackish, but fast, and it works
                int amount = Integer.parseInt(ChatColor.stripColor(meta.getLore().get(1)).replaceAll("[^0-9]", "")); //anyone who wants to complain, find an edge case and I'll change thisS
                ItemUtil.takeOne(item, p);
                ArkShards.getInstance().getShardManager().removeShards(p.getUniqueId(), amount).thenAccept((
                        result) -> p.sendMessage(ArkShards.getInstance().getConfigManager().getPrefix() + " " + ArkShards.getInstance().getConfigManager().getDepositMessage().replace("%s", result.getAmountLeft() + "").replace("%as", amount + "")));
            }
        }
    }
}

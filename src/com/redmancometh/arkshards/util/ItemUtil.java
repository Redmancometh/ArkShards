package com.redmancometh.arkshards.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.redmancometh.arkshards.ArkShards;

public class ItemUtil
{
    private static String amountString = ChatColor.translateAlternateColorCodes('&', "&7Amount: &f&l%a"); //lmao

    public static void takeOne(ItemStack i, Player p)
    {
        for (ItemStack item : p.getInventory())
        {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
            {
                String name = item.getItemMeta().getDisplayName();
                if (name.equals(i.getItemMeta().getDisplayName()) && item.getItemMeta().getLore().equals(i.getItemMeta().getLore()))
                {
                    if (item.getAmount() > 1)
                    {
                        item.setAmount(item.getAmount() - 1);
                        return;
                    }
                    p.getInventory().removeItem(i);
                    return;
                }
            }
        }
    }

    /**
     * 
     * @param amount how many shards the itemstack should be worth
     * @return
     */
    public static ItemStack getShardItem(int amount)
    {
        ItemStack shardItemBase = new ItemStack(ArkShards.getInstance().getConfigManager().getShardItem());
        System.out.println(shardItemBase);
        ItemMeta meta = shardItemBase.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(amountString.replace("%a", amount + ""));
        meta.setLore(lore); //For some reason getting and adding to the list breaks things??
        shardItemBase.setItemMeta(meta);
        System.out.println(shardItemBase);
        return shardItemBase;
    }
}

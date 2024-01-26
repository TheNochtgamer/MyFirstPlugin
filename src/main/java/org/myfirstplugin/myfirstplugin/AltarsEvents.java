package org.myfirstplugin.myfirstplugin;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.myfirstplugin.myfirstplugin.extra.Enums;

public class AltarsEvents implements Listener {
    private MyFirstPlugin main;

    public AltarsEvents(MyFirstPlugin plugin) {
        this.main = plugin;
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() != Material.DIAMOND ||
                item.lore() == null ||
                item.lore().toString().contains(Enums.UniqueEnchants.Sacrifice.Name())) return;


    }
}

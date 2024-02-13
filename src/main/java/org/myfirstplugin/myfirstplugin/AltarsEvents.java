package org.myfirstplugin.myfirstplugin;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.myfirstplugin.myfirstplugin.controllers.altars.AltarsController;
import org.myfirstplugin.myfirstplugin.extra.Enums;

import java.util.Arrays;
import java.util.Optional;

public class AltarsEvents implements Listener {
    private final AltarsController controller;
    private final MyFirstPlugin main;

    public AltarsEvents(MyFirstPlugin plugin) {

        this.main = plugin;
        this.controller = new AltarsController(plugin);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

//        if (item.getType() != Material.DIAMOND ||
//                item.lore() == null ||
//                !item.lore().toString().contains(Enums.UniqueEnchants.Sacrifice.Name())) return;

        Optional<Enums.Sacrifices> itemType = Arrays.stream(Enums.Sacrifices.values()).filter(obj ->
                obj.getItem() == item.getType()
                        && item.lore() != null
                        && item.lore().toString().contains(obj.getLore())).findFirst();

//        main.getLogger().warning(String.valueOf(itemType.isPresent()));
//        main.getLogger().warning(item.lore() == null ? "The item has no lore" : item.lore().toString());

        if (!itemType.isPresent() ||
                event.getPlayer().getWorld().getEnvironment() != World.Environment.NORMAL) return;

        controller.trackItem(event, itemType.get());
    }
}

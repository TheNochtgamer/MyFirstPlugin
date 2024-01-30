package org.myfirstplugin.myfirstplugin;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.myfirstplugin.myfirstplugin.controllers.AnvilEnchantHit;
import org.myfirstplugin.myfirstplugin.controllers.AnvilEnchantShot;
import org.myfirstplugin.myfirstplugin.controllers.ResurrectCharge;
import org.myfirstplugin.myfirstplugin.controllers.ResurrectShot;
import org.myfirstplugin.myfirstplugin.extra.Enums;

public class BowEvents implements Listener {
    private final MyFirstPlugin main;

    public BowEvents(MyFirstPlugin main) {
        this.main = main;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBow(EntityShootBowEvent event) {
//        if (event.getEntity().getType() != EntityType.PLAYER) return;
        if (event.getBow() == null || event.getBow().lore() == null) return;

        if (event.getBow().lore().toString().contains(Enums.UniqueEnchants.ANVIL.Name()))
            new AnvilEnchantShot(main, event).run();
        if (event.getEntity() instanceof Player && event.getBow().lore().toString().contains(Enums.UniqueEnchants.RESURRECT.Name()))
            new ResurrectShot(main, event).run();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {

        if (event.getEntity().getType() == EntityType.ARROW)
            new AnvilEnchantHit(main, event).run();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerReadyArrow(PlayerReadyArrowEvent event) {
        if (event.getBow().lore() == null) return;


//        event.getPlayer().sendMessage("BowTick");

        if (event.getBow().lore().toString().contains(Enums.UniqueEnchants.RESURRECT.Name()))
            new ResurrectCharge(main, event).run();

    }

//    @EventHandler
//    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
//        if (event.getItem().lore() == null) return;
//
//        main.getLogger().warning(event.getItem().toString());
//        if (event.getItem().lore().toString().contains(BowEnchants.Resurrect.Name()))
//            bowController.resurrectStopCharge(event);
//    }
}

package org.myfirstplugin.myfirstplugin;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.myfirstplugin.myfirstplugin.controllers.enchants.*;
import org.myfirstplugin.myfirstplugin.extra.Enums;

public class BowEvents implements Listener {
    private final MyFirstPlugin main;

    public BowEvents(MyFirstPlugin main) {
        this.main = main;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBow(EntityShootBowEvent event) {
//        if (event.getEntity().getType() != EntityType.PLAYER) return;
        ItemStack bow = event.getBow();
        if (bow == null || event.getBow().lore() == null) return;

        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.ANVIL.Name()))
            new AnvilFallShot(main, event).run();
        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.INSTANTANEOUS.Name()))
            new InstantaneousEnchant(main, event).run();

        if (!(event.getEntity() instanceof Player)) return;

        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.RESURRECT.Name()))
            new ResurrectShot(main, event).run();
        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.SNIPER.Name()))
            new SniperShot(main, event).run();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {

        if (event.getEntity().getType() == EntityType.ARROW)
            new AnvilFallHit(main, event).run();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerReadyArrow(PlayerReadyArrowEvent event) {
        ItemStack bow = event.getBow();
        if (event.getBow().lore() == null) return;
//        assert event.getBow().lore() != null;

//        event.getPlayer().sendMessage("BowTick");
        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.RESURRECT.Name()))
            new ResurrectCharge(main, event).run();
        if (MyUtils.hasLore(bow, Enums.UniqueEnchants.SNIPER.Name()))
            new SniperCharge(main, event).run();
    }
}

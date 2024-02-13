package org.myfirstplugin.myfirstplugin.controllers.enchants;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.MyUtils;
import org.myfirstplugin.myfirstplugin.interfaces.AnvilEnchant;

public class AnvilFallHit extends AnvilEnchant {

    private ProjectileHitEvent event;

    public AnvilFallHit(MyFirstPlugin main, ProjectileHitEvent event) {
        super(main);
        this.event = event;
    }

    public void run() {
        Entity arrow = event.getEntity();
        Location location = event.getHitEntity() == null ? event.getHitBlock().getLocation() : event.getHitEntity().getLocation();

        if (findMyArrowAndDel(arrow) == null) return;

        event.setCancelled(true);
        arrow.remove();

//        this.main.getLogger().info(String.format("Special Arrow HIT (%.2f, %.2f, %.2f)", location.x(), location.y(), location.z()));

        if (!MyUtils.hasAirUp(location.clone().add(new Vector(0, 1, 0)), 19)) return;

        Location fallingLocation = location.clone();

        fallingLocation.set(((int) location.x()) + 0.5, ((int) location.y()) + 20, ((int) location.z()) + 0.5);

        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(fallingLocation, Material.DAMAGED_ANVIL.createBlockData());
        fallingBlock.setMaxDamage(40);
        fallingBlock.setDamagePerBlock(3);
        fallingBlock.setFallDistance(0);
        fallingBlock.setCancelDrop(true);
        fallingBlock.setHurtEntities(true);

        fallingBlock.setVelocity(new Vector(0, -2, 0));

        BukkitScheduler scheduler = main.getServer().getScheduler();
        scheduler.runTaskLater(main, () -> {
            Location anvilLocation = fallingLocation.clone().subtract(new Vector(0, 19, 0));

            for (int i = 20; i > 0; i--) {
                Location actualLocation = anvilLocation.clone().subtract(0, i, 0);

                if (actualLocation.getWorld().getBlockAt(actualLocation).getBlockData().getMaterial() != Material.DAMAGED_ANVIL)
                    continue;
                actualLocation.getBlock().setType(Material.AIR);
            }

        }, 50);
    }

}

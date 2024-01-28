package org.myfirstplugin.myfirstplugin.controllers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.MyUtils;
import org.myfirstplugin.myfirstplugin.extra.Enums;

import java.util.ArrayList;

public class AltarsController {
    private final MyFirstPlugin main;
    private ArrayList<Block> usedBlocks = new ArrayList<>();

    public AltarsController(MyFirstPlugin main) {
        this.main = main;

    }

    public void trackItem(PlayerDropItemEvent event, Enums.Sacrifices sacrifice) {
        @NotNull BukkitScheduler scheduler = main.getServer().getScheduler();

        TrackerLoop trackerLoop = new TrackerLoop(event.getItemDrop(), sacrifice);
        BukkitTask task = scheduler.runTaskTimer(main, trackerLoop, 10, 4);
        trackerLoop.setTask(task);

        scheduler.runTaskLater(main, () -> {
            if (!task.isCancelled()) scheduler.cancelTask(task.getTaskId());
        }, 30 * 20);
    }

    private void diamondRain(Item entity, Block blockBelow) {
        BukkitScheduler scheduler = main.getServer().getScheduler();
        Location startLocation = blockBelow.getLocation().clone();
        World world = blockBelow.getWorld();

        entity.remove();
        startLocation.setY(256);

        world.playSound(blockBelow.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1f, 2f);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, blockBelow.getLocation().toCenterLocation(), 40, 1, 1, 1, 1.5);

        scheduler.runTaskLater(main, () -> world.setStorm(true), 40);
        BukkitTask task = scheduler.runTaskTimer(main, new DiamondsSpawnLoop(startLocation), 4 * 20, 2);

        scheduler.runTaskLater(main, () -> {
            task.cancel();
            scheduler.runTaskLater(main, () -> world.setStorm(false), 4 * 20);
        }, 30 * 20);
    }

    private class TrackerLoop implements Runnable {

        private final BukkitScheduler scheduler;
        private final Item entity;
        private final Enums.Sacrifices sacrifice;
        private BukkitTask task;

        public TrackerLoop(@NotNull Item entity, Enums.Sacrifices sacrifice) {
            this.entity = entity;
            this.sacrifice = sacrifice;
            this.scheduler = main.getServer().getScheduler();
        }

        public void setTask(BukkitTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            Location location = entity.getLocation();
            Block blockBelow = location.getWorld().getBlockAt(location.subtract(new Vector(0, 1, 0)));

            if (blockBelow.getBlockData().getMaterial() != sacrifice.getBlockToFall() ||
                    usedBlocks.contains(blockBelow)) return;

            scheduler.cancelTask(task.getTaskId());

            switch (sacrifice) {
                case DiamondRain:
                    usedBlocks.add(blockBelow);
                    diamondRain(entity, blockBelow);
                    break;
            }

            scheduler.runTaskLater(main, () -> {
                usedBlocks.remove(blockBelow);
            }, sacrifice.getTimeout() * 20L);

        }
    }

    private class DiamondsSpawnLoop implements Runnable {
        private final Location startLocation;

        public DiamondsSpawnLoop(Location startLocation) {
            this.startLocation = startLocation;
        }

        @Override
        public void run() {
            final int radius = 150;
            Vector spawnOffset = new Vector(
                    MyUtils.getRandomNumber(1, radius) - (radius / 2d),
                    MyUtils.getRandomNumber(1, 8) + 1,
                    MyUtils.getRandomNumber(1, radius) - (radius / 2d));
            Location spawnThis = startLocation.clone().add(spawnOffset);

            Item item = startLocation.getWorld().dropItem(spawnThis, new ItemStack(Material.DIAMOND));
            item.setPickupDelay(1);
        }
    }
}

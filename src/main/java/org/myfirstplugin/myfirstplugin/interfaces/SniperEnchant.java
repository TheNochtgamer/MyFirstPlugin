package org.myfirstplugin.myfirstplugin.interfaces;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.extra.Enums;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class SniperEnchant extends MyEnchant {
    private static final ArrayList<SniperLoop> sniperLoops = new ArrayList<>();

    public SniperEnchant(MyFirstPlugin main) {
        super(main);
    }

    @Nullable
    protected SniperLoop findMyLoop(@NotNull Player player) {
        for (SniperLoop myLoop : sniperLoops) {
            if (myLoop.player.equals(player)) return myLoop;
        }
        return null;
    }

    protected void addLoop(@NotNull SniperLoop loop) {
        sniperLoops.add(loop);
    }

    protected class SniperLoop implements Runnable {
        private final int loopsPerLevel = 29;
        //
        private int totalLoops = -1;
        private boolean isClosed = false;
        private boolean gotShooted = false;
        private BukkitTask task;
        private final PlayerReadyArrowEvent event;
        private BukkitScheduler scheduler = main.getServer().getScheduler();
        public final Player player;


        public SniperLoop(PlayerReadyArrowEvent event) {
            this.event = event;
            this.player = event.getPlayer();
        }

        public void setTask(BukkitTask task) {
            this.task = task;
        }

        public void shoot(EntityShootBowEvent event) {
            Arrow projectile = (Arrow) event.getProjectile();

            this.gotShooted = true;

            if (Level() >= 1) {
                projectile.setDamage(projectile.getDamage() + 2f);
            }
            if (Level() >= 2) {
                projectile.setGravity(false);
            }
            if (Level() >= 3) {
                projectile.setDamage(projectile.getDamage() / 8);
                projectile.setVelocity(projectile.getVelocity().multiply(10));
            }

            stopMe();
        }

        double Level() {
            return (double) totalLoops / loopsPerLevel;
        }

        public boolean checkIfStillDrawing() {
            PlayerInventory inv = event.getPlayer().getInventory();

            ItemStack holding = (inv.getItemInMainHand().getType() == Material.BOW || inv.getItemInMainHand().getType() == Material.CROSSBOW) ? inv.getItemInMainHand() : inv.getItemInOffHand();
            if (
                    (holding.getType() != Material.BOW && holding.getType() != Material.CROSSBOW) ||
                            holding.lore() == null ||
                            !holding.lore().toString().contains(Enums.UniqueEnchants.SNIPER.Name())
            )
                return false;
            return true;
        }

        @Override
        public void run() {
            if (event.isCancelled() || !checkIfStillDrawing() || isClosed) stopMe();
            totalLoops++;

            Location playerViewLocation = player.getEyeLocation();

            if (Level() > 1) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6, 7, true, false));
            }
            if (Level() > 2 && totalLoops % 5 == 0) {
                player.spawnParticle(Particle.ENCHANTMENT_TABLE, playerViewLocation, 7, 0.2, 0.2, 0.2, 1.9);
            }
            if (Level() > 3 && totalLoops % 13 == 0) {
                player.playSound(playerViewLocation, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 0.8f, 2f);
            }

            if (Level() == 1) {
                player.playSound(playerViewLocation, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1f, 0f);
                player.sendActionBar(Component.text("+ Extra damage", NamedTextColor.GREEN));
            } else if (Level() == 2) {
                player.playSound(playerViewLocation, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1f, 1f);
                player.sendActionBar(Component.text("+ Zero gravity", NamedTextColor.GREEN));
            } else if (Level() == 3) {
                player.playSound(playerViewLocation, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1f, 2f);
                // java.lang.IllegalArgumentException: Cannot get data for not block REDSTONE
//                player.spawnParticle(Particle.REDSTONE, playerViewLocation, 30, 0, 0, 0, 3, Material.REDSTONE.createBlockData());
                player.sendActionBar(Component.text("+ Instant", NamedTextColor.RED));
            }
        }

        private void stopMe() {
            isClosed = true;

            scheduler.cancelTask(task.getTaskId());
            sniperLoops.remove(this);

            if (this.gotShooted) return;

            if (Level() > 1)
                event.getPlayer().playSound(player.getEyeLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0f, 1.9f);
        }
    }
}

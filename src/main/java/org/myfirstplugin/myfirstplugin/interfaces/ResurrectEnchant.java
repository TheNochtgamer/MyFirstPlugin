package org.myfirstplugin.myfirstplugin.interfaces;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.extra.Enums;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public abstract class ResurrectEnchant extends MyEnchant {
    protected static final ArrayList<ResurrectLoop> resurrectLoops = new ArrayList<>();

    public ResurrectEnchant(MyFirstPlugin main) {
        super(main);
    }

    @Nullable
    protected ResurrectLoop findMyLoop(Player player) {
        for (ResurrectLoop myLoop : resurrectLoops) {
            if (myLoop.checkIfTheSamePlayer(player)) return myLoop;
        }
        return null;
    }

    protected class ResurrectLoop implements Runnable {
        private final int totalLoopsForNewArrow = 5;
        //
        private int loopsForNewArrow = 0;
        private boolean isClosed = false;
        private boolean gotShooted = false;
        private BukkitTask task;
        private PlayerReadyArrowEvent event;
        private BukkitScheduler scheduler = main.getServer().getScheduler();
        private ArrayList<Arrow> shootingArrows = new ArrayList<>();


        public ResurrectLoop(PlayerReadyArrowEvent event) {
            this.event = event;
        }

        public void setTask(BukkitTask task) {
            this.task = task;
        }

        public void shoot(EntityShootBowEvent event) {
//            event.getEntity().sendMessage("Shoot");
            Projectile projectile = (Projectile) event.getProjectile();

            this.gotShooted = true;
            stopMe();

            for (Arrow arrow : shootingArrows) {
                arrow.setShooter(projectile.getShooter());
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

                arrow.setGravity(true);
                arrow.setRotation(projectile.getYaw(), projectile.getPitch());
                arrow.setVelocity(projectile.getVelocity());
            }
        }

        public boolean checkIfTheSamePlayer(Player player) {
            if (!this.event.getPlayer().equals(player)) return false;

            this.isClosed = true;
            return true;
        }

        public boolean checkIfStillDrawing() {
            PlayerInventory inv = event.getPlayer().getInventory();

            ItemStack holding = (inv.getItemInMainHand().getType() == Material.BOW || inv.getItemInMainHand().getType() == Material.CROSSBOW) ? inv.getItemInMainHand() : inv.getItemInOffHand();
            if (
                    (holding.getType() != Material.BOW && holding.getType() != Material.CROSSBOW) ||
                            holding.lore() == null ||
                            !holding.lore().toString().contains(Enums.UniqueEnchants.RESURRECT.Name())
            )
                return false;
            return true;
        }

        @Override
        public void run() {
            if (event.isCancelled() || !this.checkIfStillDrawing() || this.isClosed) stopMe();
            this.loopsForNewArrow++;

            Player player = event.getPlayer();
            Location playerEye = player.getEyeLocation();
            Vector playerEyeDirection = playerEye.getDirection();
            final Collection<Arrow> nearbyArrowsRaw = playerEye.getWorld().getNearbyEntitiesByType(Arrow.class, playerEye, 6.5f);
            ArrayList<Arrow> nearbyArrows = new ArrayList<>();
            Arrow newArrow = null;

//            player.sendMessage("Tick " + MyUtils.vectorIntoString(playerEyeDirection));

            for (Arrow nearbyArrow : nearbyArrowsRaw) {
                boolean found = false;
                for (Arrow shootingArrow : this.shootingArrows) {
                    if (nearbyArrow.equals(shootingArrow)) {
                        found = true;
                        break;
                    }
                }
                if (found) continue;
                nearbyArrows.add(nearbyArrow);
            }

            if (!nearbyArrows.isEmpty() && shootingArrows.size() < 4 && totalLoopsForNewArrow <= loopsForNewArrow) {
                loopsForNewArrow = 0;

//                newArrow = (Arrow) spawnArrow(player, new Vector(0, -2, 0));
                newArrow = nearbyArrows.stream().findAny().get();

//                newArrow.getLocation().setDirection(playerEyeDirection);
                newArrow.setGravity(false);
                newArrow.setDamage(5.0f);
                shootingArrows.add(newArrow);

                player.getWorld().playSound(playerEye, Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.9f);
            }

            int arrowCount = 0;
            for (Arrow arrow : this.shootingArrows) {
                Location newLocation = null;
                Location frontLocation = playerEye.clone().add(new Vector(playerEyeDirection.getX(), playerEyeDirection.getY(), playerEyeDirection.getZ()).normalize());
                Vector leftVector = new Vector(-playerEyeDirection.getZ(), 0 /*playerEyeDirection.getY()*/, playerEyeDirection.getX()).normalize();

//                player.sendMessage("Tick " + MyUtils.vectorIntoString(leftVector));

                switch (arrowCount) {
                    case 0:
                        newLocation = frontLocation.clone().subtract(leftVector);
                        break;
                    case 1:
                        newLocation = frontLocation.clone().add(new Vector(0, 1, 0));
                        break;
                    case 2:
                        newLocation = frontLocation.clone().add(leftVector);
                        break;
                    case 3:
                        newLocation = frontLocation.clone().subtract(new Vector(0, 1, 0));
                        break;
                    default:
                        newLocation = playerEye.clone().add(playerEyeDirection);
                        break;

                }

                arrow.teleport(newLocation);
                arrow.setRotation(playerEye.getYaw(), playerEye.getPitch());
//                arrow.setVelocity(playerEyeDirection.divide(new Vector(1000, 1000, 1000)));

                arrowCount += 1;

            }
        }

//        private Projectile spawnArrow(Player player, Vector offset) {
//            Location location = player.getEyeLocation();
//            Vector direction = location.getDirection();
//
//            return player.getWorld().spawnArrow(
//                    location.add(offset),
//                    direction,
//                    0,
//                    3.0f
//            );
//        }

        private void stopMe() {
            Player player = this.event.getPlayer();
            this.isClosed = true;
            scheduler.cancelTask(task.getTaskId());
            resurrectLoops.remove(this);

            if (!this.gotShooted && !this.shootingArrows.isEmpty()) {
                player.getWorld().playSound(player.getEyeLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0f, 1.9f);

                for (Entity arrow : this.shootingArrows) {
                    arrow.setGravity(true);
                }
            }


        }

    }
}
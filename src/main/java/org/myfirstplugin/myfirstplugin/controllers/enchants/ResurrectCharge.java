package org.myfirstplugin.myfirstplugin.controllers.enchants;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.ResurrectEnchant;

public class ResurrectCharge extends ResurrectEnchant {

    private PlayerReadyArrowEvent event;

    public ResurrectCharge(MyFirstPlugin main, PlayerReadyArrowEvent event) {
        super(main);
        this.event = event;
    }

    public void run() {
        if (this.findMyLoop(event.getPlayer()) != null) return;

        BukkitScheduler scheduler = main.getServer().getScheduler();

        ResurrectLoop resurrectLoop = new ResurrectLoop(event);
        resurrectLoops.add(resurrectLoop);
        BukkitTask task = scheduler.runTaskTimer(main, resurrectLoop, 10, 4);
        resurrectLoop.setTask(task);
    }
}

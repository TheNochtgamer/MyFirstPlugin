package org.myfirstplugin.myfirstplugin.controllers.enchants;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.SniperEnchant;

public class SniperCharge extends SniperEnchant {

    private PlayerReadyArrowEvent event;

    public SniperCharge(MyFirstPlugin main, PlayerReadyArrowEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void run() {
        if (this.findMyLoop(event.getPlayer()) != null) return;

        BukkitScheduler scheduler = main.getServer().getScheduler();

        SniperLoop myLoop = new SniperLoop(event);
        addLoop(myLoop);

        BukkitTask task = scheduler.runTaskTimer(main, myLoop, 0, 2);
        myLoop.setTask(task);
    }
}

package org.myfirstplugin.myfirstplugin.controllers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.SniperEnchant;

public class SniperShot extends SniperEnchant {
    private final EntityShootBowEvent event;

    public SniperShot(MyFirstPlugin main, EntityShootBowEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void run() {
        SniperLoop myLoop = findMyLoop((Player) event.getEntity());
        if (myLoop == null) return;

        myLoop.shoot(event);
    }
}

package org.myfirstplugin.myfirstplugin.controllers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.ResurrectEnchant;

public class ResurrectShot extends ResurrectEnchant {
    private final EntityShootBowEvent event;

    public ResurrectShot(MyFirstPlugin main, EntityShootBowEvent event) {
        super(main);
        this.event = event;
    }

    public void run() {
        ResurrectLoop myLoop = findMyLoop((Player) event.getEntity());
        if (myLoop == null) return;
        
        myLoop.shoot(event);
    }
}

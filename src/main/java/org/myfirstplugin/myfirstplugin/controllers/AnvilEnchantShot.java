package org.myfirstplugin.myfirstplugin.controllers;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.AnvilEnchant;

public class AnvilEnchantShot extends AnvilEnchant {
    private final EntityShootBowEvent event;

    public AnvilEnchantShot(MyFirstPlugin main, EntityShootBowEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void run() {
        if (event.getProjectile().getType() != EntityType.ARROW) return;

        addArrow(event.getProjectile());
    }
}

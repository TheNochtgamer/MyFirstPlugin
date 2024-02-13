package org.myfirstplugin.myfirstplugin.controllers.enchants;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;
import org.myfirstplugin.myfirstplugin.interfaces.AnvilEnchant;

public class AnvilFallShot extends AnvilEnchant {
    private final EntityShootBowEvent event;

    public AnvilFallShot(MyFirstPlugin main, EntityShootBowEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void run() {
        if (event.getProjectile().getType() != EntityType.ARROW) return;

        addArrow(event.getProjectile());
    }
}

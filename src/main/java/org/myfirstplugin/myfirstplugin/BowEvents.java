package org.myfirstplugin.myfirstplugin;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.myfirstplugin.myfirstplugin.controllers.BowController;
import org.myfirstplugin.myfirstplugin.extra.Enums;

import java.util.ArrayList;

public class BowEvents implements Listener {
    private final ArrayList<FlechaEspecial> flechasEspeciales;
    private final BowController controller;

    public BowEvents(MyFirstPlugin main) {
        this.controller = new BowController(main);
        this.flechasEspeciales = new ArrayList<>();
    }

    public <E extends EntityEvent> void manageEspecialArrows(E event) {
        Entity entity = event.getEntity();

//        this.flechasEspeciales.stream().forEach(fE -> {
//            if (!entity.equals(fE.getEntityInvolved())) return;
//            flechasEspeciales.remove(fE);
//
//            switch (fE.getType()) {
//                case ANVIL:
//                    bowController.anvilArrow((ProjectileHitEvent) event);
//                    break;
//            }
//        });

        for (FlechaEspecial fE : this.flechasEspeciales) {
            if (!entity.equals(fE.getEntityInvolved())) continue;
            flechasEspeciales.remove(fE);

            switch (fE.getType()) {
                case ANVIL:
                    controller.anvilArrow((ProjectileHitEvent) event);
                    break;
            }

            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityShootBow(EntityShootBowEvent event) {
//        if (event.getEntity().getType() != EntityType.PLAYER) return;
        if (event.getBow() == null || event.getBow().lore() == null) return;

        if (event.getBow().lore().toString().contains(Enums.UniqueEnchants.Anvil.Name()))
            this.flechasEspeciales.add(new FlechaEspecial(event.getProjectile(), Enums.SpecialItems.ANVIL));
        if (event.getEntity() instanceof Player && event.getBow().lore().toString().contains(Enums.UniqueEnchants.Resurrect.Name()))
            controller.resurrectShot(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        manageEspecialArrows(event);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerReadyArrow(PlayerReadyArrowEvent event) {
        if (event.getBow().lore() == null) return;


//        event.getPlayer().sendMessage("BowTick");

        if (event.getBow().lore().toString().contains(Enums.UniqueEnchants.Resurrect.Name()))
            controller.resurrectCharge(event);

    }

//    @EventHandler
//    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
//        if (event.getItem().lore() == null) return;
//
//        main.getLogger().warning(event.getItem().toString());
//        if (event.getItem().lore().toString().contains(BowEnchants.Resurrect.Name()))
//            bowController.resurrectStopCharge(event);
//    }
}

class FlechaEspecial {
    private final Entity entityInvolved;
    private final Enums.SpecialItems type;


    public FlechaEspecial(Entity entityInvolved, Enums.SpecialItems type) {
        this.entityInvolved = entityInvolved;
        this.type = type;
    }

    public Entity getEntityInvolved() {
        return entityInvolved;
    }

    public Enums.SpecialItems getType() {
        return type;
    }
}

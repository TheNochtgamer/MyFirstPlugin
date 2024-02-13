package org.myfirstplugin.myfirstplugin.interfaces;

import org.bukkit.entity.Entity;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;

import java.util.ArrayList;

public abstract class AnvilEnchant extends MyEnchant {

    private static final ArrayList<Entity> arrows = new ArrayList<>();

    public AnvilEnchant(MyFirstPlugin main) {
        super(main);
    }

    public void addArrow(Entity arrow) {
        arrows.add(arrow);
    }

    protected Entity findMyArrowAndDel(Entity arrowToFind) {
        for (Entity arrow : arrows) {
            if (!arrow.equals(arrowToFind)) continue;
            arrows.remove(arrow);
            return arrow;
        }
        return null;
    }
}

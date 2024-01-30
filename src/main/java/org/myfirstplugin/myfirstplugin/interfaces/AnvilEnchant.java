package org.myfirstplugin.myfirstplugin.interfaces;

import org.bukkit.entity.Arrow;
import org.myfirstplugin.myfirstplugin.MyFirstPlugin;

import java.util.ArrayList;

public abstract class AnvilEnchant extends MyEnchant {

    protected final ArrayList<Arrow> arrows = new ArrayList<>();

    public AnvilEnchant(MyFirstPlugin main) {
        super(main);
    }

    public Arrow findMyArrow(Arrow arrowToFind) {
        for (Arrow arrow : this.arrows) {
            if (!arrow.equals(arrowToFind)) continue;
            this.arrows.remove(arrow);
            return arrow;
        }
        return null;
    }
}

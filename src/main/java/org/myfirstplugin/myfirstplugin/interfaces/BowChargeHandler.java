package org.myfirstplugin.myfirstplugin.interfaces;

import java.util.ArrayList;

public abstract class BowChargeHandler {
    private final ArrayList<BowShotEvent> bowShotListeners = new ArrayList<>();
    private final ArrayList bowChargeListeners = new ArrayList();
    private final ArrayList bowCancelListeners = new ArrayList();
    private final ArrayList bowDrawingTicklListeners = new ArrayList();


    public interface BowShotEvent {
        public void onBowShot();
    }
}

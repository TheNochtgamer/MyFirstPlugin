package org.myfirstplugin.myfirstplugin.interfaces;

import org.myfirstplugin.myfirstplugin.MyFirstPlugin;

public abstract class MyEnchant {
    protected final MyFirstPlugin main;

    public MyEnchant(MyFirstPlugin main) {
        this.main = main;
    }

    public abstract void run();
}

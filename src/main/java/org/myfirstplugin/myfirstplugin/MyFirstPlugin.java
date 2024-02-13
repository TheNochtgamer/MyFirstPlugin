package org.myfirstplugin.myfirstplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class MyFirstPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new BowEvents(this), this);
        getServer().getPluginManager().registerEvents(new AltarsEvents(this), this);

        getLogger().info("Funcionando correctamente");

    }

    public void onDisable() {

//        getLogger().info("Se desactivo el plugin");

    }

}

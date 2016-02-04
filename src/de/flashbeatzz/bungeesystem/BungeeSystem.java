package de.flashbeatzz.bungeesystem;

import de.flashbeatzz.bungeesystem.banmanager.BanManager;
import de.flashbeatzz.bungeesystem.banmanager.PreLoginListener;
import de.flashbeatzz.bungeesystem.uuid_lib.UUIDLibrary;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeSystem extends Plugin {

    private static BungeeSystem instance;

    public static BungeeSystem getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Starting CommunicationSystem-Server ...");
        getProxy().getScheduler().runAsync(this, new SocketReadThread());
        System.out.println("Successfully started.");

        new BanManager();
        getProxy().getPluginManager().registerListener(this, new PreLoginListener());
        getProxy().getPluginManager().registerListener(this, new UUIDLibrary());
    }

}

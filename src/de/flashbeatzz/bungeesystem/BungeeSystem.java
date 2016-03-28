package de.flashbeatzz.bungeesystem;

import de.flashbeatzz.bungeesystem.whitelist.Whitelist;
import de.flashbeatzz.bungeesystem.whitelist.WhitelistListener;
import de.flashbeatzz.bungeesystem.whitelist.cmdWhitelist;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BungeeSystem extends Plugin {

    private static BungeeSystem instance;

    public Socket socket;

    public static BungeeSystem getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        Data.mySQL.closeConnection();

        sendMessage(SocketTarget.BUNGEECORD, "DISCONNECT", "", false);
    }

    @Override
    public void onEnable() {
        instance = this;
        Data.mysqlCfg = new Config("mysql", getDataFolder().getName());
        Data.mysqlCfg.addDefault("MySQL.Host", "localhost");
        Data.mysqlCfg.addDefault("MySQL.User", "root");
        Data.mysqlCfg.addDefault("MySQL.Password", "pass");
        Data.mysqlCfg.addDefault("MySQL.Database", "db");
        Data.mysqlCfg.addDefault("MySQL.Port", 3306);
        Data.mysqlCfg.copyAndSave(true);

        Data.mySQL = new MySQL();
        if(!Data.mySQL.openConnection()) {
            BungeeCord.getInstance().stop("MySQL-Connection failed!");
        }

        createTables();
        getProxy().getPluginManager().registerCommand(this, new cmdWhitelist());
        new Whitelist();
        getProxy().getPluginManager().registerListener(this, new WhitelistListener());

        try {
            socket = new Socket("localhost", 19888);
            printWriter = new PrintWriter(socket.getOutputStream());
            getProxy().getScheduler().runAsync(this, new SocketReadThread());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage(SocketTarget.BUNGEECORD, "CONNECT", "BungeeCord", false);

        getProxy().getPluginManager().registerListener(this, new UUIDLibrary());
    }

    public void createTables() {
        MySQL.createTable("levelsystem", null,
                "`uuid` text NOT NULL," +
                "`level` int(11) NOT NULL," +
                "`exp` int(11) NOT NULL");
        MySQL.createTable("guildes", "id",
                "`id` int(11) NOT NULL AUTO_INCREMENT," +
                "`name` text NOT NULL," +
                "`tag` text NOT NULL," +
                "`founder_uuid` text NOT NULL," +
                "`money` double NOT NULL");
        MySQL.createTable("guilde_members", null,
                "`uuid` text NOT NULL," +
                "`guilde_id` int(11) NOT NULL");
        MySQL.createTable("uuid_library", null,
                "`uuid` text NOT NULL," +
                "`name` text NOT NULL");
    }

    public static void sendMessage(String target, String header, String message, boolean sendSelf) {
        String fString = target + "/§§/" + header + "/§§/" + message + "/§§/" + (sendSelf ? "TRUE" : "FALSE");
        printWriter.println(fString);
        printWriter.flush();
    }

    private static PrintWriter printWriter;

    public static void sendMessage(SocketTarget target, String header, String message, boolean sendSelf) {
        String fString = target.get() + "/§§/" + header + "/§§/" + message + "/§§/" + (sendSelf ? "TRUE" : "FALSE");
        printWriter.println(fString);
        printWriter.flush();
    }

}

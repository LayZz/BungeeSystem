package de.flashbeatzz.bungeesystem;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

        try {
            socket = new Socket("localhost", 19888);
            printWriter = new PrintWriter(socket.getOutputStream());
            getProxy().getScheduler().runAsync(this, new SocketReadThread());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage(SocketTarget.BUNGEECORD, "CONNECT", "BUNGEECORD", false);

        new GuildeSystem();
        new LevelSystem();
        getProxy().getPluginManager().registerListener(this, new UUIDLibrary());
    }

    public void createTables() {
        MySQL.createTable("levelsystem", "" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`uuid` VARCHAR(100) NOT NULL," +
                "`level` INT NOT NULL," +
                "`exp` INT NOT NULL," +
                "PRIMARY KEY ('id')");
        MySQL.createTable("guildes", "" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`name` VARCHAR(100) NOT NULL," +
                "`tag` VARCHAR(4) NOT NULL," +
                "`founder_uuid` VARCHAR(100) NOT NULL," +
                "`money` DOUBLE NOT NULL," +
                "PRIMARY KEY ('id')");
        MySQL.createTable("guilde_members", "" +
                "`uuid` VARCHAR(100) NOT NULL," +
                "`guilde_id` INT NOT NULL)");
        MySQL.createTable("uuid_library", "`id` int(11) NOT NULL AUTO_INCREMENT",
                "  `uuid` varchar(100) NOT NULL",
                "  `name` varchar(50) NOT NULL",
                "  PRIMARY KEY (`id`)");
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

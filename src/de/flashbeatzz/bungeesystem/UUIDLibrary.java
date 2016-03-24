package de.flashbeatzz.bungeesystem;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDLibrary implements Listener {

    @EventHandler
    public void onPreLogin(PostLoginEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        String name = e.getPlayer().getName();
        ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `uuid`='" + uuid.toString() + "';");
        try {
            if (rs != null && rs.next()) {
                if(!name.equals(rs.getString("name"))) {
                    MySQL.update("UPDATE `uuid_library` SET `name`='" + name + "' WHERE `uuid`='" + uuid.toString() + "';");
                }
            } else {
                MySQL.update("INSERT INTO `uuid_library` (`uuid`, `name`) VALUES ('" + uuid.toString() + "', '" + name + "');");
                MySQL.update("INSERT INTO `levelsystem` (`uuid`, `level`, `exp`) VALUES ('" + uuid.toString() + "', '1', '0');");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static UUID getUUIDtoName(String name) {
        if(isRegistred(name)) {
            try {
                ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `name`='" + name + "';");
                if(rs != null && rs.next()) {
                    return UUID.fromString(rs.getString("uuid"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getNameToUUID(UUID uuid) {
        if(isRegistred(uuid)) {
            try {
                ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `uuid`='" + uuid + "';");
                if(rs != null && rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Boolean isRegistred(UUID uuid) {
        ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `uuid`='" + uuid.toString() + "';");
        try {
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isRegistred(String name) {
        ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `name`='" + name + "';");
        try {
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static UUID getConsoleUUID() {
        return new UUID(0L, 0L);
    }
}

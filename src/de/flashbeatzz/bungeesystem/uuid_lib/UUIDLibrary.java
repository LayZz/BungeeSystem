package de.flashbeatzz.bungeesystem.uuid_lib;

import de.flashbeatzz.bungeesystem.MySQL;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDLibrary implements Listener {

    @EventHandler
    public void onPreLogin(PreLoginEvent e) {
        UUID uuid = e.getConnection().getUniqueId();
        String name = e.getConnection().getName();
        ResultSet rs = MySQL.query("SELECT * FROM `uuid_library` WHERE `uuid`='" + uuid.toString() + "';");
        try {
            if (rs != null && rs.next()) {
                if(!name.equals(rs.getString("name"))) {
                    MySQL.update("UPDATE `uuid_library` SET `name`='" + name + "' WHERE `uuid`='" + uuid.toString() + "';");
                }
            } else {
                MySQL.update("INSERT INTO `uuid_library` (`uuid`, `name`) VALUES ('" + uuid.toString() + "', '" + name + "');");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public UUIDLibrary() {
        MySQL.createTable("uuid_library", "`id` int(11) NOT NULL AUTO_INCREMENT",
                "  `uuid` varchar(100) NOT NULL",
                "  `name` varchar(50) NOT NULL",
                "  PRIMARY KEY (`id`)");
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
}

package de.flashbeatzz.bungeesystem.whitelist;

import de.flashbeatzz.bungeesystem.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Whitelist {

    public static Boolean enable() {
        if(!status()) {
            Data.whitelist.getYamlConfiguration().set("enabled", true);
            return true;
        }
        return false;
    }

    public static Boolean disable() {
        if(status()) {
            Data.whitelist.getYamlConfiguration().set("enabled", false);
            return true;
        }
        return false;
    }

    public static Boolean addPlayer(UUID uuid) {
        List<String> list = Data.whitelist.getYamlConfiguration().getStringList("players");
        if(!list.contains(uuid.toString())) {
            list.add(uuid.toString());
            Data.whitelist.getYamlConfiguration().set("players", list);
            Data.whitelist.copyAndSave(true);
            return true;
        }
        return false;
    }

    public static Boolean removePlayer(UUID uuid) {
        List<String> list = Data.whitelist.getYamlConfiguration().getStringList("players");
        if(list.contains(uuid.toString())) {
            list.remove(uuid.toString());
            Data.whitelist.getYamlConfiguration().set("players", list);
            Data.whitelist.copyAndSave(true);
            return true;
        }
        return false;
    }

    public static List<UUID> getWhitelisted() {
        List<UUID> list = new ArrayList<>();
        for(String str : Data.whitelist.getYamlConfiguration().getStringList("players")) {
            list.add(UUID.fromString(str));
        }
        return list;
    }

    public static String getDisconnectMessage() {
        return Data.whitelist.getYamlConfiguration().getString("message");
    }

    public static void setDisconnectMessage(String msg) {
        Data.whitelist.getYamlConfiguration().set("message", msg);
    }

    public static Boolean status() {
        return Data.whitelist.getYamlConfiguration().getBoolean("enabled");
    }

}

package de.flashbeatzz.bungeesystem.withelist;

import com.google.common.collect.Lists;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Withelist {
    private static Configuration c;
    private static File f;

    public Withelist() {
        try {
            f = new File("plugins/BungeeSystem", "withelist.yml");
            if(!f.exists()) f.createNewFile();
            c = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
            if(c.get("enabled") == null) {
                c.set("enabled", false);
            }
            if(c.get("messsage") == null) {
                c.set("message", "You are not withelisted.");
            }
            if(c.get("players") == null) {
                c.set("players", Lists.newArrayList());
            }
            saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean enable() {
        if(!status()) {
            c.set("enabled", true);
            return true;
        }
        return false;
    }

    public static Boolean disable() {
        if(status()) {
            c.set("enabled", false);
            return true;
        }
        return false;
    }

    public static Boolean addPlayer(UUID uuid) {
        List<String> list = c.getStringList("players");
        if(!list.contains(uuid.toString())) {
            list.add(uuid.toString());
            c.set("players", list);
            saveFile();
            return true;
        }
        return false;
    }

    public static Boolean removePlayer(UUID uuid) {
        List<String> list = c.getStringList("players");
        if(list.contains(uuid.toString())) {
            list.remove(uuid.toString());
            c.set("players", list);
            saveFile();
            return true;
        }
        return false;
    }

    public static List<UUID> getWithelisted() {
        List<UUID> list = new ArrayList<>();
        for(String str : c.getStringList("players")) {
            list.add(UUID.fromString(str));
        }
        return list;
    }

    public static String getDisconnectMessage() {
        return c.getString("message");
    }

    public static void setDisconnectMessage(String msg) {
        c.set("message", msg);
    }

    public static Boolean status() {
        return c.getBoolean("enabled");
    }

    public static void saveFile() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

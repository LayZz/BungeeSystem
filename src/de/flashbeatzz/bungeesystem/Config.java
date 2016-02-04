package de.flashbeatzz.bungeesystem;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private File file;
    private Configuration yaml;

    public Config(String fileName, String folderName) {
        File folder = new File("plugins/" + folderName);
        this.file = new File(folder + "/" + fileName + ".yml");
        if(!folder.exists()) {
            folder.mkdir();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BungeeSystem.getInstance().getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }

    public String getString(String path) {
        return yaml.getString(path);
    }

    public Boolean getBoolean(String path) {
        return yaml.getBoolean(path);
    }

    public Integer getInt(String path) {
        return yaml.getInt(path);
    }

    public List<String> getStringList(String path) {
        return yaml.getStringList(path);
    }

    public Double getDouble(String path) {
        return yaml.getDouble(path);
    }

    public Float getFloat(String path) {
        return (float) yaml.getInt(path);
    }

    public Object get(String path) {
        return yaml.get(path);
    }

    public void addDefault(String path, Object value) {
        if(yaml.get(path) == null) {
            yaml.set(path, value);
        }
    }

    public Configuration getYamlConfiguration() {
        return this.yaml;
    }

    public File getFile() {
        return this.file;
    }

    public void copyAndSave(Boolean b) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(yaml, new File(BungeeSystem.getInstance().getDataFolder(), this.file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

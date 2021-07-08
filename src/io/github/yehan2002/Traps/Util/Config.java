package io.github.yehan2002.Traps.Util;

import io.github.yehan2002.Traps.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings({"ResultOfMethodCallIgnored", "WeakerAccess", "unused"})
public class Config {
    private File f;
    public YamlConfiguration yml;

    public Config(String name) {

        if (!Main.get().getDataFolder().exists()) {
            Main.get().getDataFolder().mkdirs();
        }

        f = Paths.get(Main.get().getDataFolder().toString(), name).toFile();
        yml = YamlConfiguration.loadConfiguration(f);

        this.saveDefault();
    }

    public void saveDefault(){
        if (!f.exists()) {
            Main.get().getLogger().info(f.getName() + " not found, creating!");
            Main.get().saveResource(f.getName(), false);
            f = f.toPath().toFile();
            yml = YamlConfiguration.loadConfiguration(f);
        }

    }

    public void saveDefault(boolean force){
        if (force){
            f.delete();
        }
        if (force || !f.exists()) {
            Main.get().getLogger().info(f.getName() + " not found, creating!");
            Main.get().saveResource(f.getName(), false);
            f = f.toPath().toFile();
            yml = YamlConfiguration.loadConfiguration(f);
        }

    }

    public void Create(){
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        yml = YamlConfiguration.loadConfiguration(f);


    }

    public void reload(){
        this.save();
        f = f.toPath().toFile();
        yml = YamlConfiguration.loadConfiguration(f);
    }

    public Object get(String path){
        return yml.get(path);
    }

    public void save(){
        try {
            yml.options().copyDefaults(true);
            yml.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value){
        yml.set(path, value);
    }

    public boolean getBoolean(String p){
        return yml.getBoolean(p);
    }

    public List<Boolean> getBooleanList(String p){
        return yml.getBooleanList(p);
    }

    public String getString(String path){
        return yml.getString(path);
    }

    public List<String> getStringList(String p){
        return yml.getStringList(p);
    }


    public int getInt(String path){
        return yml.getInt(path);
    }

    public boolean getBool(String path){
        return yml.getBoolean(path);
    }

}

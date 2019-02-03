package io.github.yehan2002.Traps;

import io.github.yehan2002.Traps.EventListeners.TrapListener;
import io.github.yehan2002.Traps.Util.Config;
import io.github.yehan2002.Traps.Util.Vault;
import io.github.yehan2002.Traps.api.TrapManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Vault vault;
    private static Main instance;
    public Config config;
    private Config trapConfig;
    public Shop shop;
    private EventListener listener;


    @Override
    public void onEnable() {
        instance = this;
        config = new Config("config.yml");
        trapConfig = new Config("traps.yml");
        config.saveDefault(false);
        trapConfig.saveDefault(false);
        vault = new Vault(this);
        System.out.println(TrapManager.TNT);

        shop = new Shop();
        listener = new EventListener();
        this.getServer().getPluginManager().registerEvents(new TrapListener(), this);
        this.getServer().getPluginManager().registerEvents(listener,this);
        this.getServer().getPluginCommand("trap").setExecutor(new Command());
    }


    public static Main get() {
        return instance;
    }

    @Override
    public void onDisable() {
        listener.save();
        super.onDisable();
    }
}

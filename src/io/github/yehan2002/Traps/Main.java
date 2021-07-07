package io.github.yehan2002.Traps;

import io.github.yehan2002.Traps.EventListeners.TrapListener;
import io.github.yehan2002.Traps.Util.Config;
import io.github.yehan2002.Traps.Util.Recipe;
import io.github.yehan2002.Traps.Util.Vault;
import io.github.yehan2002.Traps.api.TrapManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    Vault vault;
    private static Main instance;
    public Config config;
    public Shop shop;
    private EventListener listener;
    static ArrayList<UUID> exemptedPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        config = new Config("config.yml");
        config.saveDefault(false);

        Config trapConfig = new Config("traps.yml");
        trapConfig.saveDefault(false);

        try {
            vault = new Vault(this);
        } catch (RuntimeException e){
            e.printStackTrace();
            return;
        }

        shop = new Shop();
        listener = new EventListener();


        this.getServer().getPluginManager().registerEvents(new TrapListener(), this);
        this.getServer().getPluginManager().registerEvents(listener,this);
        Objects.requireNonNull(this.getServer().getPluginCommand("trap")).setExecutor(new Command());
    }


    public static Main get() {
        return instance;
    }

    @Override
    public void onDisable() {
        if (listener != null) {
            listener.save();
        }
        super.onDisable();
    }


}

package io.github.yehan2002.Traps;

import io.github.yehan2002.Traps.EventListeners.TrapListener;
import io.github.yehan2002.Traps.Util.Config;
import io.github.yehan2002.Traps.Util.Recipe;
import io.github.yehan2002.Traps.Util.Vault;
import io.github.yehan2002.Traps.api.TrapManager;
import org.bukkit.Material;
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

        vault = new Vault(this);
        System.out.println(TrapManager.TNT);

        shop = new Shop();
        listener = new EventListener();

        try {
            new Recipe(new ItemStack(Material.DEAD_BUSH), "spring_stick").addRecipe(" I ", " S ", " S ").setIngredient('I', Material.IRON_INGOT, 1).setIngredient('S', Material.STICK, 1);
        } catch (NoClassDefFoundError  e){
            getLogger().log(Level.SEVERE, e.getMessage());
        }
        this.getServer().getPluginManager().registerEvents(new TrapListener(), this);
        this.getServer().getPluginManager().registerEvents(listener,this);
        Objects.requireNonNull(this.getServer().getPluginCommand("trap")).setExecutor(new Command());
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

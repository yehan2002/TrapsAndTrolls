package io.github.yehan2002.Traps.Util;

import io.github.yehan2002.Traps.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Vault {
    private Economy economy;
    private String buyFail;
    private String buyOk;

    public Vault(JavaPlugin plugin){
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getServer().getLogger().severe("Vault was not found. Disabling "+ plugin.getName());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null || rsp.getProvider() == null){
            plugin.getServer().getLogger().severe("Vault was not found. Disabling "+ plugin.getName());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        economy = rsp.getProvider();
        buyFail = ChatColor.translateAlternateColorCodes('&', Main.get().config.getString("messages.notEnoughMoney"));
        buyOk = ChatColor.translateAlternateColorCodes('&', Main.get().config.getString("messages.buySuccess"));

    }

    public boolean buyItem(Player p, int amount){
        if (economy.has(p, amount)) {
            economy.withdrawPlayer(p, amount);
            p.sendMessage(buyOk);
            return true;
        }
        p.sendMessage(buyFail);
        return false;

    }
}

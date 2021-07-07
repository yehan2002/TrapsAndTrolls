package io.github.yehan2002.Traps.Util;

import io.github.yehan2002.Traps.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Vault {
    private Economy economy;
    private String buyFail;
    private String buyOk;

    public Vault(JavaPlugin plugin){
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getServer().getLogger().severe("Vault was not found. Disabling "+ plugin.getName());
            Objects.requireNonNull(plugin.getServer().getPluginCommand("trap")).setExecutor(
                (sender, command, label, args) -> {
                    TextComponent message =  new TextComponent(ChatColor.RED + "Vault is not installed. Click here to install.");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"https://www.spigotmc.org/resources/vault.34315/"));
                    sender.spigot().sendMessage(message);
                    return true;
                }
            );
            throw new RuntimeException("Vault was not installed.");
        }

           RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null || rsp.getProvider() == null){
            plugin.getServer().getLogger().severe("Economy provider was not found. Disabling "+ plugin.getName());
            Objects.requireNonNull(plugin.getServer().getPluginCommand("trap")).setExecutor(
                (sender, command, label, args) -> {
                    TextComponent message =  new TextComponent(ChatColor.RED + "Please install a supported economy plugin. Click here for a list of supported plugins.");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"https://www.spigotmc.org/resources/vault.34315/"));
                    sender.spigot().sendMessage(message);
                    return true;
                }
        );
            throw new RuntimeException("No economy plugin.");
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

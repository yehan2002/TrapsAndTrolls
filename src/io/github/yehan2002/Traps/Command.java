package io.github.yehan2002.Traps;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {


    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 | (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
                p.sendMessage(ChatColor.DARK_AQUA + "-----------                -----------");
                p.sendMessage(ChatColor.GOLD + "                Traps & Trolls            ");
                p.sendMessage(ChatColor.DARK_AQUA + "-----------                -----------");
                p.sendMessage("");

                p.sendMessage(ChatColor.DARK_GREEN + "Created by " + ChatColor.GREEN + "yj22k (http://bit.do/eySHt)");
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "Commands:");
                p.sendMessage(ChatColor.GOLD + "/trap shop -" + ChatColor.GREEN + " Open player shop.");
                p.sendMessage(ChatColor.GOLD + "/trap help -" + ChatColor.GREEN + " Display this message.");
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("shop")) {
                Main.get().shop.openShop(p);

                return true;
            }
        }
            return true;
    }
}
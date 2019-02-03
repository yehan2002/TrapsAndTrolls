package io.github.yehan2002.Traps;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {


    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        System.out.println("caller");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.get().shop.openShop(p);
            /*
            if (args.length == 0 | (args.length == 1 && args[0].equalsIgnoreCase("help"))){
                p.sendMessage(ChatColor.DARK_AQUA + "-----------                -----------");
                p.sendMessage(ChatColor.GOLD + "                Traps & Trolls            ");
                p.sendMessage(ChatColor.DARK_AQUA + "-----------                -----------");
                p.sendMessage("");

                p.sendMessage(ChatColor.DARK_GREEN + "Created by " + ChatColor.GREEN + "yj22k (http://bit.do/eySHt)");
                p.sendMessage("");
                p.sendMessage(ChatColor.GREEN + "Commands:");
                p.sendMessage(ChatColor.GOLD + "/trap shop -"+ ChatColor.GREEN + " Open player shop.");
                p.sendMessage(ChatColor.GOLD + "/trap god -"+ ChatColor.GREEN + " Excludes the player from traps.");
                p.sendMessage(ChatColor.GOLD + "/trap help -" + ChatColor.GREEN + " Display this message.");
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("shop")){
                //trapShop.openInventory(p);

            } else if (args.length == 1 && args[0].equalsIgnoreCase("god")){
                if (p.hasPermission("trapAandTrolls.exempt")) {
                    excluded.add(p);
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this");
                }
            }
            return true;
        }
        return false;
    }
}
*/
        }
        return true;
    }
}
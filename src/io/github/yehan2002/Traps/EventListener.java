package io.github.yehan2002.Traps;

import io.github.yehan2002.Traps.EventListeners.TrapTriggeredEvent;
import io.github.yehan2002.Traps.Util.Serialization.serialization;
import io.github.yehan2002.Traps.api.Trap;
import io.github.yehan2002.Traps.api.TrapManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class EventListener implements Listener{
    private HashMap<Location, Trap> traps;
    private boolean debug = true;

    EventListener(){
        traps = serialization.Deserialize();
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void PlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        Location l = p.getLocation().getBlock().getLocation();
        if(!p.getLocation().getBlock().isEmpty() && p.getLocation().getBlock().getType() == Material.TRIPWIRE){
            if (traps.containsKey(l)){
                TrapManager trap = traps.get(l).trap;


                //if (p.hasPermission("trapsAndTrolls.exempt")){
                  //  return;
                //}

                TrapTriggeredEvent event = new TrapTriggeredEvent(p, trap, null);

                Bukkit.getServer().getPluginManager().callEvent(event);

                if (! event.isCancelled()){

                    Logger.getGlobal().severe("[Trap] Unhandled Trap " + trap + "@" + p.getLocation().getBlock().getLocation());

                } else {
                    if (event.isRemove()) {
                        p.getLocation().getBlock().setType(Material.AIR);
                        traps.remove(l);
                    }
                    if (debug) {
                        Logger.getGlobal().info("[Trap] Triggered  " + trap + "@" + p.getLocation().getBlock().getLocation() + ".");
                        System.out.println(traps);
                    }

                }

            }

        }
    }


    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void trapPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getType() == Material.STRING && e.getItemInHand().getItemMeta().getDisplayName() != null) {
            TrapManager trapManager = TrapManager.getTrapType(e.getItemInHand());

            if (trapManager != null) {

                traps.put(e.getBlock().getLocation(), new Trap(trapManager,""));

                if (debug) {
                    Logger.getGlobal().info("[Trap] Placed " + trapManager + " trap @ " + e.getBlock().getLocation() + ".");
                }
            }
        }
    }

    @SuppressWarnings({"unused", "deprecation"})
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void blockBreak(BlockBreakEvent e){
        Location l = e.getBlock().getLocation();
        if(!e.getBlock().isEmpty() && e.getBlock().getType() == Material.TRIPWIRE){
            if (traps.containsKey(l)) {
                Map enc;
                try {
                    enc = e.getPlayer().getInventory().getItemInMainHand().getEnchantments();
                }catch (NoSuchMethodError methodError){
                    enc = e.getPlayer().getInventory().getItemInHand().getEnchantments();
                }
                Player p = e.getPlayer();
                if (enc.containsKey(Enchantment.SILK_TOUCH) | enc.containsKey(Enchantment.LOOT_BONUS_BLOCKS) | enc.containsKey(Enchantment.LOOT_BONUS_MOBS)) {
                    p.getWorld().dropItemNaturally(l, traps.get(l).trap.get(true));


                } else {
                    e.setExpToDrop(50);
                }
            }
        }
    }

    public void save(){
        serialization.Serialize(traps);
    }
}

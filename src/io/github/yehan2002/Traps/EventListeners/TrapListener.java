package io.github.yehan2002.Traps.EventListeners;

import io.github.yehan2002.Traps.Main;
import io.github.yehan2002.Traps.Util.Constants;
import io.github.yehan2002.Traps.api.TrapManager;
import io.github.yehan2002.Traps.api.TrapTriggeredEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TrapListener implements Listener {

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTrap(TrapTriggeredEvent e) {
        switch (e.getTrap()) {
            case TNT:
                e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(), EntityType.PRIMED_TNT);
                break;
            case Fire:
                e.getPlayer().setFireTicks(100);
                break;
            case Glow:
                try {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 1));
                } catch (NoSuchFieldError ignored) {
                }
                break;
            case Launch:
                try {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10), false);
                } catch (NoSuchFieldError error) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getPlayer().teleport(e.getPlayer().getLocation().add(0, 10, 0));
                        }
                    }.runTaskLater(Main.get(), 5);

                }
                break;
            case Poison:
                try {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                } catch (NoSuchFieldError ignored) {
                }
                break;
            case Lightning:
                e.getPlayer().getWorld().strikeLightning(e.getPlayer().getLocation());
                break;
            default:
                return;
        }
        e.setCancelled(true);


    }

    @EventHandler(ignoreCancelled = true)
    private void HerobrineTroll(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Herobrine) return;
        Location l = e.getPlayer().getLocation();
        e.getPlayer().playSound(e.getPlayer().getLocation(), Constants.JUMPSCARE, 1, 5);
        e.setCancelled(true);
        l.add(2, 0, 0);
        Creeper c = (Creeper) e.getPlayer().getWorld().spawnEntity(l, EntityType.CREEPER);
        try {
            c.setExplosionRadius(0);
        } catch (NoSuchMethodError methodError) {
            c.remove();
            return;
        }
        c.setPowered(true);
        c.setTarget(e.getPlayer());
        c.setMaxFuseTicks(40);
        c.setVelocity(e.getPlayer().getVelocity());

        l.subtract(4, 0, 0);
        c = (Creeper) e.getPlayer().getWorld().spawnEntity(l, EntityType.CREEPER);
        c.setExplosionRadius(0);
        c.setPowered(true);
        c.setTarget(e.getPlayer());
        c.setMaxFuseTicks(40);
        c.setVelocity(e.getPlayer().getVelocity());

        l.add(2, 0, 2);
        c = (Creeper) e.getPlayer().getWorld().spawnEntity(l, EntityType.CREEPER);
        c.setExplosionRadius(0);
        c.setPowered(true);
        c.setTarget(e.getPlayer());
        c.setMaxFuseTicks(40);
        c.setVelocity(e.getPlayer().getVelocity());

        l.subtract(0, 0, 4);
        c = (Creeper) e.getPlayer().getWorld().spawnEntity(l, EntityType.CREEPER);
        c.setExplosionRadius(0);
        c.setPowered(true);
        c.setTarget(e.getPlayer());
        c.setMaxFuseTicks(40);
        c.setVelocity(e.getPlayer().getVelocity());

        
    }

    @EventHandler(ignoreCancelled = true)
    private void ThiefTrap(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Thief) return;
        e.setRemove(false);
        e.setCancelled(true);
        Player p = e.getPlayer();
        ItemStack handItem;
        try {
            handItem = p.getInventory().getItemInMainHand();
        } catch (NoSuchMethodError methodError) {
            handItem = p.getInventory().getItemInHand();
        }
        if (handItem.getType() == Constants.AIR | handItem.getType() == Constants.WRITTEN_BOOK) {
            return;
        }
        Location l = p.getLocation();
        l.setY(l.getBlockY() - 2);
        Block block = l.getBlock();
        block.setType(Constants.CHEST);

        Chest c = (Chest) block.getState();

        c.getBlockInventory().addItem(handItem);

        ItemStack writtenBook = new ItemStack(Constants.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
        bookMeta.setTitle("Stolen Item");
        bookMeta.setAuthor("Unknown");
        bookMeta.setPages("X: " + l.getBlockX() + "\nY: " + l.getBlockY() + "\nZ: " + l.getBlockZ());
        writtenBook.setItemMeta(bookMeta);

        try {
            p.getInventory().setItemInMainHand(writtenBook);
        } catch (NoSuchMethodError methodError) {
            p.getInventory().setItemInHand(writtenBook);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void LavaTrap(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Lava) return;

        Player p = e.getPlayer();
        Location l = p.getLocation();
        Location lava;
        HashMap<Location, Material> resetList = new HashMap<>();
        l.setY(l.getY() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.AIR);
        l.setY(l.getY() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.WEB);
        l.setY(l.getY() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.WEB);
        l.setY(l.getY() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.LAVA);
        lava = l.clone();
        Runnable noDeath = new BukkitRunnable() {
            @Override
            public void run() {
                if (p.getHealth() < 5) {
                    lava.getBlock().setType(Constants.MAGMA);
                    p.setFireTicks(0);
                }
            }
        };
        BukkitTask noDeathRunner = Bukkit.getServer().getScheduler().runTaskTimer(Main.get(), noDeath, 200, 20);


        Bukkit.getServer().getScheduler().runTaskLater(Main.get(), () -> {
            if (l.getChunk().isLoaded()) {
                for (Location loc : resetList.keySet()) {
                    loc.getBlock().setType(resetList.get(loc));

                }
                noDeathRunner.cancel();
            }
        }, 1200);

        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void CageTrap(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Cage) return;
        Player p = e.getPlayer();
        Location l = p.getLocation();
        HashMap<Location, Material> resetList = new HashMap<>();

        l.setY(l.getY() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setY(l.getY() + 3);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setX(l.getX() - 1);
        l.setY(l.getY() - 2);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() - 2);
        l.setX(l.getX() + 2);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() - 2);
        l.setX(l.getX() - 1);
        l.setZ(l.getZ() - 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() - 2);
        l.setZ(l.getZ() + 2);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.OBSIDIAN);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() + 1);
        resetList.put(l.clone(), l.getBlock().getType());
        l.getBlock().setType(Constants.IRON_FENCE);
        l.setY(l.getY() - 2);

        Bukkit.getServer().getScheduler().runTaskLater(Main.get(), () -> {
            if (l.getChunk().isLoaded()) {
                for (Location loc : resetList.keySet()) {
                    loc.getBlock().setType(resetList.get(loc));
                }
            }
        }, 300);


        e.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true)
    private void DiamondTroll(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Diamond) return;
        ArrayList<Item> drops = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            //
            ItemStack stack = new ItemStack(Constants.DIAMOND);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN+"fake diamond");
            stack.setItemMeta(meta);
            Item Drop = e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation().add(0, 1, 0), stack);

            Drop.setPickupDelay(1000);

            try {
                Drop.setInvulnerable(true);
            } catch (NoSuchMethodError methodError) {
                // spigot 1.8
            }

            drops.add(Drop);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item Drop : drops) {
                    Drop.remove();
                }
            }
        }.runTaskLater(Main.get(), 500);
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void CreativeTroll(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Creative) return;
        e.getPlayer().sendMessage(ChatColor.GOLD + "Set game mode " + ChatColor.RED + "creative" + ChatColor.GOLD + " for " + ChatColor.DARK_RED + e.getPlayer().getDisplayName());
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    private void OpTroll(TrapTriggeredEvent e) {
        if (e.getTrap() != TrapManager.Op) return;
        e.getPlayer().sendMessage(ChatColor.GRAY + "[Server: Opped " + e.getPlayer().getDisplayName() + "]");
        e.setCancelled(true);
    }

}


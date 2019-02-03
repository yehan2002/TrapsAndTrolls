package io.github.yehan2002.Traps;

import io.github.yehan2002.Traps.api.TrapManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Shop implements Listener {
    private ItemStack[] optionIcons;
    private int[] prices;
    private Inventory inventory;
    private int size;
    private String name = ChatColor.GREEN + "Trap Shop";


    public Shop() {
        size = TrapManager.values().length;
        size = size % 9 == 0 ? size : (size - (size % 9)) + 9;

        this.inventory = Bukkit.createInventory(null, size, name);
        this.optionIcons = new ItemStack[size];
        this.prices = new int[size];
        this.addItems();
        Main.get().getServer().getPluginManager().registerEvents(this, Main.get());
    }

    private void addItems() {
        TrapManager[] traps = TrapManager.values();
        for (int i = 0; i < traps.length; i++) {
            if (traps[i] == TrapManager.Custom || !traps[i].enabled) continue;
            optionIcons[i] = traps[i].get(true);
            prices[i] = traps[i].price;
            inventory.setItem(i, this.addPrice(traps[i].get(false), traps[i].price));
        }
    }
    private ItemStack addPrice(ItemStack i, int price){
        i = i.clone();
        ItemMeta meta = i.getItemMeta();
        List<String> lore= meta.getLore();
        lore.add(ChatColor.GREEN + "Price: $"+ price);
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (this.isValid(slot,event.getClickedInventory().getItem(slot))){
                Player p = (Player) event.getWhoClicked();
                if (Main.get().vault.buyItem(p, prices[slot])) p.getInventory().addItem(optionIcons[slot]);
            }
        }
    }

    private boolean isValid(int slot, ItemStack itemStack){
        if (!(slot >= 0) || slot > size || optionIcons[slot] == null) return false;

        ItemStack item = optionIcons[slot];
        if (item.getType() != itemStack.getType()) return false;

        ItemMeta meta1 = itemStack.getItemMeta();
        ItemMeta meta2 = item.getItemMeta();

        if (meta1.hasDisplayName() != meta2.hasDisplayName() || !meta1.getDisplayName().equals(meta2.getDisplayName())) return false;
        if (meta1.hasLore() != meta2.hasLore()) return false;
        if (!meta1.hasLore()) return true;

        for (int i = 0; i < meta2.getLore().size(); i++) {
            if (!meta1.getLore().get(i).equals(meta2.getLore().get(i))) return false;
        }
        return true;
    }

    void openShop(Player p){
        p.openInventory(inventory);
    }

}

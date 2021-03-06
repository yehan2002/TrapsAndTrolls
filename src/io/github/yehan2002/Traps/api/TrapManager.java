package io.github.yehan2002.Traps.api;

import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.yehan2002.Traps.Main;
import io.github.yehan2002.Traps.Shop;
import io.github.yehan2002.Traps.Util.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public enum  TrapManager {
    Fire(0),
    Diamond(1),
    Poison(2),
    Herobrine(3),
    Creative(4),
    Op(5),
    Glow(6),
    Launch(7),
    TNT(8),
    Lightning(9),
    Thief(10),
    Lava(11),
    Cage(12),
    Custom(-1);

    private final Config trapConfig ;

    {
        trapConfig = new Config("traps.yml");
        trapConfig.saveDefault();
    }

    public final int type;
    public final int price;
    public final String description;
    public final String name;
    public final boolean enabled;
    public final String perm;

    TrapManager(int i) {
        type = i;

        if (i == -1){
            price = 0;
            description = "";
            name = "";
            perm = "";
            enabled = false;
            return;
        }

        price = trapConfig.getInt(this + ".price");
        description = trapConfig.getString(this + ".description");
        name = trapConfig.getString(this + ".name");
        perm = trapConfig.getString(this + ".permission");
        enabled = Main.get().config.getBoolean( "traps."+ this );

        if (Main.get().shop != null) Main.get().shop = new Shop();

    }

    public ItemStack get(boolean addNBT){
        ItemStack itemStack = new ItemStack(Material.STRING);
        ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(ChatColor.translateAlternateColorCodes('&', String.format("&a%s&r", this.name)));
        meta.setLore(new ArrayList<>(Arrays.asList(description.split("\n"))));
        itemStack.setItemMeta(meta);
        if (addNBT){
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setString("trapName", this.toString());
            itemStack = nbtItem.getItem();
        }
        return itemStack;
    }

    public static TrapManager getTrapType(ItemStack itemStack){
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("trapName")){
            return TrapManager.valueOf(nbtItem.getString("trapName"));
        }
        return null;
    }

    public static void addCustom(String name, boolean addToShop, int price) throws Exception {
        throw new Exception("Not Implemented Yet...");
    }
}

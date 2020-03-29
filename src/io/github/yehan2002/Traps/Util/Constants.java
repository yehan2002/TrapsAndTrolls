package io.github.yehan2002.Traps.Util;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class Constants {
    public static final Material OBSIDIAN = Material.OBSIDIAN;
    public static final Material WRITTEN_BOOK = Material.WRITTEN_BOOK;
    public static final Material CHEST = Material.CHEST;
    public static final Material IRON_FENCE;
    public static final Material AIR =Material.AIR ;
    public static final Material WEB;
    public static final Material DIAMOND = Material.DIAMOND;
    public static final Material MAGMA;
    public static final Material LAVA = Material.LAVA;
    public static final Sound JUMPSCARE;

    static {
        Material tmp;
        try {
            tmp = Material.valueOf("IRON_FENCE");
        }catch (IllegalArgumentException ignored){
            tmp = Material.valueOf("IRON_BARS");
        }
        IRON_FENCE = tmp;

        try {
            tmp = Material.valueOf("WEB");
        }catch (IllegalArgumentException ignored){
            tmp = Material.valueOf("COBWEB");
        }
        WEB = tmp;

        try {
            tmp = Material.valueOf("MAGMA");
        }catch (IllegalArgumentException ignored){
            tmp = Material.valueOf("MAGMA_BLOCK");
        }
       MAGMA = tmp;

        Sound tmpSound;
        try{
            tmpSound = Sound.valueOf("ENTITY_ENDERDRAGON_AMBIENT");
        }catch (IllegalArgumentException ignored) {
            try{
                tmpSound = Sound.valueOf("Sound.ENTITY_ENDER_DRAGON_AMBIENT");
            }catch (IllegalArgumentException ignored2) {
                tmpSound = null;
            }
        }
        JUMPSCARE = tmpSound;
    }
}

package io.github.yehan2002.Traps.Util;

import io.github.yehan2002.Traps.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

@SuppressWarnings("unused")
public class Recipe {

    private ShapedRecipe recipe;

    public Recipe(ItemStack i, String n){
        NamespacedKey nn = new NamespacedKey(Main.get(), n.toLowerCase().replace(" ", "_"));
        recipe = new ShapedRecipe(nn, i);
    }

    @Deprecated
    public Recipe(ItemStack i){
        recipe = new ShapedRecipe(i);
    }

    public Recipe addRecipe(String s1, String s2, String s3){
        recipe.shape(s1,s2,s3);
        return this;
    }

    public Recipe setIngredient(char s, Material m, int amount, int damage){
        ItemStack ii = new ItemStack(m, amount);
        ii.setDurability((short) damage);
        recipe.setIngredient(s, ii.getData());
        return this;
    }

    public Recipe setIngredient(char s, Material m, int amount){
        ItemStack ii = new ItemStack(m, amount);
        recipe.setIngredient(s, ii.getData());
        return this;
    }

    public Recipe setIngredient(char s, Material m){
        ItemStack ii = new ItemStack(m);
        recipe.setIngredient(s, ii.getData());
        return this;
    }

    public void add(){
        Bukkit.getServer().addRecipe(recipe);
    }

}

package io.github.yehan2002.Traps.Util.Serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

class location implements Serializable {
    private static final long serialVersionUID = -967672296935701779L;
    private int x,y,z;
    private String world;

    location(Location l){
        x = l.getBlockX();
        y = l.getBlockY();
        z = l.getBlockZ();
        world = l.getWorld().getName();

    }

    org.bukkit.Location getLocation() throws IllegalStateException{
        World world1 = Bukkit.getWorld(world);
        if (world1 == null){
            throw new IllegalStateException(world);
        }
        return new Location(world1,x,y,z);
    }
}

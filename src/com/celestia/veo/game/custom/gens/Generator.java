package com.celestia.veo.game.custom.gens;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Generator {

    public String name;
    Material m;
    Location l;
    int time = 0, length;
    ArmorStand s;

    public Generator(String name, Material m, Location l, int length) {

        this.name = name;
        this.m = m;
        this.l = l;
        this.length = length;
        s = (ArmorStand) l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.ARMOR_STAND);
        s.setInvisible(true);
        s.setMarker(true);
        s.setGravity(false);
        s.setHelmet(new ItemStack(m));
        s.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                + (length - time) + " seconds...");
        s.setCustomNameVisible(true);

    }

    public void run() {

        /*Location tl = s.getLocation();
        tl.setYaw(tl.getYaw() + 1);
        s.teleport(tl);*/

        s.getLocation().setYaw(s.getLocation().getYaw() + 5);

        if (time == length) {

            time = 0;
            l.getWorld().dropItemNaturally(l.add(0, 2, 0), new ItemStack(m));

        } else time++;
        s.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                + (length - time) + " seconds...");

    }

    public void remove() {

        s.remove();
        GenManager.gens.remove(this);

    }

}

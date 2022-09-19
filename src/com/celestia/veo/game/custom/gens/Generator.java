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
    ArmorStand n;

    public Generator(String name, Material m, Location l, int length) {

        this.name = name;
        this.m = m;
        this.l = l;
        this.length = length;
        s = (ArmorStand) l.getWorld().spawnEntity(
                new Location(l.getWorld(), l.getX(), l.getY() + 2, l.getZ()), EntityType.ARMOR_STAND);
        s.setInvisible(true);
        s.setMarker(true);
        s.setGravity(false);
        s.setHelmet(new ItemStack(m));
        s.addScoreboardTag("removable");

        n = (ArmorStand) l.getWorld().spawnEntity(
                new Location(l.getWorld(), l.getX(), l.getY() + 2.2, l.getZ()), EntityType.ARMOR_STAND);
        n.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                + (length - time) + " seconds...");
        n.setCustomNameVisible(true);
        n.setInvisible(true);
        n.setMarker(true);
        n.setGravity(false);
        n.addScoreboardTag("removable");

    }

    public void run() {

        Location tl = s.getLocation();
        tl.setYaw(tl.getYaw() + 5);
        s.teleport(tl);

        if (s.getNearbyEntities(3, 3, 3).size() < 1) {

            s.setCustomName(ChatColor.GREEN + "Stand here!");
            return;

        }

        if (time == length) {

            time = 0;
            l.getWorld().dropItemNaturally(new Location(l.getWorld(), l.getX(), l.getY() + 2, l.getZ()),
                    new ItemStack(m));

        } else time++;

        if (time % 20 == 0) n.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                + (length - time) / 20 + " seconds...");

    }

    public void remove() {

        s.remove();
        n.remove();
        GenManager.gens.remove(this);
        GenManager.genFile.removeLine(name + "@" + m.toString() + "@" + l.getWorld() + "@"
                + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + time);

    }

}

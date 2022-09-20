package veo.game.gens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
                new Location(l.getWorld(), l.getX(), l.getY() + 4.2, l.getZ()), EntityType.ARMOR_STAND);
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

        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getLocation().distance(s.getLocation()) >= 4) {

                n.setCustomName(ChatColor.GREEN + "Stand here!");
                time = 0;
                return;

            }

        if (time == length) {

            time = 0;
            Item i = l.getWorld().dropItemNaturally(new Location(l.getWorld(),
                            l.getX(), l.getY() + 2, l.getZ()),
                    new ItemStack(m));
            i.teleport(new Location(l.getWorld(), s.getLocation().getX(),
                    s.getLocation().getY() + 1.8, s.getLocation().getZ()));
            i.setVelocity(new Vector(0, 0, 0));


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

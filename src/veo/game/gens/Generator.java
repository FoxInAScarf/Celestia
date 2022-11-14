package veo.game.gens;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import veo.game.gens.flag.Flag;
import veo.game.gens.flag.FlagManager;
import veo.game.items.ZItem;
import veo.game.items.ZItemManager;

import java.util.Objects;

public class Generator {

    public String name, drop;
    ItemStack m;
    Material h;
    Location l;
    int time = 0, length;
    ArmorStand s, n;

    public Generator(String name, String m, Location l, int length, Material h) {

        this.name = name;
        this.drop = m;
        this.m = ZItemManager.getItem(m);
        if (this.m == null) this.m = new ItemStack(Material.getMaterial(m.toUpperCase()));
        this.h = h;
        //this.l = new Location(l.getWorld(), Math.floor(l.getX()) + 0.5, l.getY(), Math.floor(l.getZ()) + 0.5);
        this.l = l;
        this.length = length;

        for (Entity e : this.l.getWorld().getEntities())
            if (e.getScoreboardTags().contains(name + "GenItem")
                ||
                e.getScoreboardTags().contains(name + "GenName")) e.remove();


        //s = (ArmorStand) l.getWorld().spawnEntity(
        //        new Location(this.l.getWorld(), this.l.getX(), this.l.getY() + 2, this.l.getZ()), EntityType.ARMOR_STAND);
        s = (ArmorStand) l.getWorld().spawnEntity(new Location(l.getWorld(), l.getX(), l.getY() + 2, l.getZ()), EntityType.ARMOR_STAND);
        s.setInvisible(true);
        s.setMarker(true);
        s.setGravity(false);
        s.setHelmet(new ItemStack(h));
        s.addScoreboardTag("removable");
        s.addScoreboardTag(name + "GenItem");

        n = (ArmorStand) l.getWorld().spawnEntity(
                new Location(l.getWorld(), l.getX(), l.getY() + 4.2, l.getZ()), EntityType.ARMOR_STAND);
        n.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                + (length - time) / 20 + " seconds...");
        n.setCustomNameVisible(true);
        n.setInvisible(true);
        n.setMarker(true);
        n.setGravity(false);
        n.addScoreboardTag("removable");
        n.addScoreboardTag(name + "GenName");

    }

    public void run() {

        boolean sendItem = false;
        if (time == length) {

            sendItem = true;
            time = 0;

        } else time++;

        Flag f = FlagManager.getFlag(name);
        if (sendItem && f != null) if (f.owner != null) {

            ItemStack i = m.clone();
            i.setAmount(2);
            f.owner.getPlayer().getInventory().addItem(i);

        }

        Location tl = s.getLocation();
        tl.setYaw(tl.getYaw() + 5);
        s.teleport(tl);

        int nearbyPlayers = 0;
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getLocation().distance(s.getLocation()) <= 4) nearbyPlayers++;

        if (nearbyPlayers > 0) {

            if (sendItem) {

                Item i = l.getWorld().dropItemNaturally(new Location(l.getWorld(),
                                l.getX(), l.getY() + 2, l.getZ()), m);
                i.teleport(new Location(l.getWorld(), s.getLocation().getX(),
                        s.getLocation().getY() + 1.8, s.getLocation().getZ()));
                i.setVelocity(new Vector(0, 0, 0));

            }

            if (time % 20 == 0) n.setCustomName(ChatColor.GREEN + "Next item is in " + ChatColor.GOLD
                    + (length - time) / 20 + " seconds" + ChatColor.GREEN + "...");
            return;

        }
        if (time % 20 == 0) n.setCustomName(ChatColor.GREEN + "Stand here! (" + ChatColor.GOLD
                + (length - time) / 20 + "s" + ChatColor.GREEN + ")");

    }

    public void remove() {

        s.remove();
        n.remove();
        GenManager.genFile.removeLine(name + "@" + m.getType().name() + "@" + l.getWorld().getName() + "@"
                + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + length + "@" + h.toString());
        //System.out.println(name + "@" + m.getType().name() + "@" + l.getWorld().getName() + "@"
        //        + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + length + "@" + h.toString());
        GenManager.gens.remove(this);

    }

    public void edit(String name, String drop, int length, Material item) {

        GenManager.genFile.removeLine(name + "@" + m.getType().name() + "@" + l.getWorld().getName() + "@"
                + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + length + "@" + h.toString());
        //System.out.println(name + "@" + m.getType().name() + "@" + l.getWorld().getName() + "@"
        //        + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + length + "@" + h.toString());
        this.name = name;
        this.drop = drop;
        this.m = ZItemManager.getItem(drop);
        if (this.m == null) this.m = new ItemStack(Material.getMaterial(drop.toUpperCase()));
        this.length = length;
        this.h = item;
        s.setHelmet(new ItemStack(h));

        GenManager.genFile.addLine(name + "@" + drop + "@" + l.getWorld().getName() + "@"
                + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + length + "@" + h);

    }

}

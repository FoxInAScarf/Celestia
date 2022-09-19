package com.celestia.veo.game.custom.gens;

import com.celestia.veo.Main;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Flag {

    public String name;
    public OfflinePlayer owner;
    public Location head, pole;
    private ArmorStand a, b, c;

    public Flag(String name, Location head, Location pole) {

        // generate blocks

        head.getBlock().setType(Material.RED_WOOL);
        for (int i = 0; i <= 9; i++)
            pole.add(0, i, 0).getBlock().setType(Material.OAK_FENCE);
        pole.add(0, 10, 0).getBlock().setType(Material.DARK_OAK_SLAB);

        this.head = head;
        this.pole = pole;
        this.name = name;

        unclaim();

    }

    public void claim(Player p) {

        owner = p;
        Main.sendMessage(p, ChatColor.GREEN + "You claimed the island of "
                + name, false);
        /*
        *
        *
        * ADD CLAIM STATUE
        *
        *
        * */
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 3);

        a.remove();
        a = (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX(), head.getY() + 2, head.getZ()),
                EntityType.ARMOR_STAND);
        a.setMarker(true);
        a.setInvisible(true);
        a.setGravity(false);
        a.setCustomName(ChatColor.RED + p.getName() + " has claimed this island!");
        a.setCustomNameVisible(true);
        a.addScoreboardTag("removable");

        b = (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX(), head.getY() + 2.2, head.getZ()),
                EntityType.ARMOR_STAND);
        b.setMarker(true);
        b.setInvisible(true);
        b.setGravity(false);
        b.setCustomName(ChatColor.GREEN + "Crouch here to tear the flag down.");
        b.setCustomNameVisible(false);
        b.addScoreboardTag("removable");

        ((CraftPlayer) p).getHandle().playerConnection.
                sendPacket(new PacketPlayOutEntityDestroy(a.getEntityId()));
        ((CraftPlayer) p).getHandle().playerConnection.
                sendPacket(new PacketPlayOutEntityDestroy(b.getEntityId()));

        c = (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX(), head.getY() + 2, head.getZ()),
                EntityType.ARMOR_STAND);
        b.setMarker(true);
        b.setInvisible(true);
        b.setGravity(false);
        b.setCustomName(ChatColor.GREEN + "You've claimed this island!");
        b.setCustomNameVisible(false);
        c.addScoreboardTag("removable");

        for (Player pl : Bukkit.getOnlinePlayers()) {

            if (pl.equals(p)) continue;
            ((CraftPlayer) pl).getHandle().playerConnection.
                    sendPacket(new PacketPlayOutEntityDestroy(c.getEntityId()));

        }

    }

    public void unclaim() {

        owner = null;
        a.remove();
        b.remove();
        c.remove();
        /*for (Player p : Bukkit.getOnlinePlayers())
            Main.sendMessage(p, ChatColor.GREEN + "The island of " + name
                    + " has been unclaimed!", false);*/
        a = (ArmorStand) head.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX(), head.getY() + 0.3, head.getZ()),
                EntityType.ARMOR_STAND);
        a.setMarker(true);
        a.setInvisible(true);
        a.setGravity(false);
        a.setCustomName(ChatColor.GREEN + "Click here to claimed this island!");
        a.setCustomNameVisible(true);
        a.addScoreboardTag("removable");

    }

    public void remove() {

        unclaim();
        GenManager.flags.remove(this);
        GenManager.flagFile.removeLine(name + "@" + head.getWorld() + "@" + head.getX() +
                "@" + head.getY() + "@" + head.getZ() + "@"
                + pole.getX() + "@" + pole.getY() + "@" + pole.getZ() + "@");

    }

}

package veo.game.gens;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import veo.Main;

public class Flag {

    public String name;
    public OfflinePlayer owner;
    public Location head, pole;
    private ArmorStand a, b, c;

    public Flag(String name, Location head, Location pole) {

        this.head = new Location(head.getWorld(), Math.floor(head.getX()), head.getY(),
                Math.floor(head.getZ()));
        this.pole = pole;
        this.name = name;

        // generate blocks

        head.getBlock().setType(Material.RED_WOOL);
        for (int i = 0; i <= 9; i++)
            new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + i, this.pole.getZ())
                    .getBlock().setType(Material.OAK_FENCE);
        new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + 10, this.pole.getZ())
                .getBlock().setType(Material.DARK_OAK_SLAB);

        a = (ArmorStand) head.getWorld().spawnEntity(
                new Location(head.getWorld(), this.head.getX() + 0.5, this.head.getY() + 0.3, this.head.getZ() + 0.5),
                EntityType.ARMOR_STAND);
        a.setMarker(true);
        a.setInvisible(true);
        a.setGravity(false);
        a.setCustomName(ChatColor.GREEN + "Click here to claimed this island!");
        a.setCustomNameVisible(true);
        a.addScoreboardTag("removable");

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
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2.2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND);
        a.setMarker(true);
        a.setInvisible(true);
        a.setGravity(false);
        a.setCustomName(ChatColor.RED + p.getName() + " has claimed this island!");
        a.setCustomNameVisible(true);
        a.addScoreboardTag("removable");

        b = (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND);
        b.setMarker(true);
        b.setInvisible(true);
        b.setGravity(false);
        b.setCustomName(ChatColor.GREEN + "Crouch here to tear the flag down.");
        b.setCustomNameVisible(false);
        b.addScoreboardTag("removable");

        ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityDestroy(a.getEntityId()));
        ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityDestroy(b.getEntityId()));

        c = (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND);
        c.setMarker(true);
        c.setInvisible(true);
        c.setGravity(false);
        c.setCustomName(ChatColor.GREEN + "You've claimed this island!");
        c.setCustomNameVisible(false);
        c.addScoreboardTag("removable");

        for (Player pl : Bukkit.getOnlinePlayers()) {

            if (pl.equals(p)) continue;
            ((CraftPlayer) pl).getHandle().b.a(new PacketPlayOutEntityDestroy(c.getEntityId()));

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
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 0.3, head.getZ() + 0.5),
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

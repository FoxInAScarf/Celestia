package veo.game.gens;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import veo.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Flag {

    public String name;
    public OfflinePlayer owner;
    public Location head, pole;

    private HashMap<String, ArmorStand> stands = new HashMap<>();

    public Flag(String name, Location head, Location pole) {

        this.head = new Location(head.getWorld(), Math.floor(head.getX()), head.getY(),
                Math.floor(head.getZ()));
        this.pole = pole;
        this.name = name;

        // generate blocks

        for (int i = 0; i <= 9; i++)
            new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + i, this.pole.getZ())
                    .getBlock().setType(Material.OAK_FENCE);
        new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + 10, this.pole.getZ())
                .getBlock().setType(Material.DARK_OAK_SLAB);

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

        stands.get("clickHere").remove();
        stands.remove("clickHere", stands.get("clickHere"));

        stands.put("hasClaimed", (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2.2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND));
        stands.get("hasClaimed").setMarker(true);
        stands.get("hasClaimed").setInvisible(true);
        stands.get("hasClaimed").setGravity(false);
        stands.get("hasClaimed").setCustomName(ChatColor.RED + p.getName() + " has claimed this island!");
        stands.get("hasClaimed").setCustomNameVisible(true);
        stands.get("hasClaimed").addScoreboardTag("removable");

        stands.put("crouchHere", (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND));
        stands.get("crouchHere").setMarker(true);
        stands.get("crouchHere").setInvisible(true);
        stands.get("crouchHere").setGravity(false);
        stands.get("crouchHere").setCustomName(ChatColor.GREEN + "Crouch here to tear the flag down.");
        stands.get("crouchHere").setCustomNameVisible(true);
        stands.get("crouchHere").addScoreboardTag("removable");

        ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityDestroy(stands.get("hasClaimed").getEntityId()));
        ((CraftPlayer) p).getHandle().b.a(new PacketPlayOutEntityDestroy(stands.get("crouchHere").getEntityId()));

        stands.put("youClaimed", (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 2, head.getZ() + 0.5),
                EntityType.ARMOR_STAND));
        stands.get("youClaimed").setMarker(true);
        stands.get("youClaimed").setInvisible(true);
        stands.get("youClaimed").setGravity(false);
        stands.get("youClaimed").setCustomName(ChatColor.GREEN + "You've claimed this island!");
        stands.get("youClaimed").setCustomNameVisible(true);
        stands.get("youClaimed").addScoreboardTag("removable");

        for (Player pl : Bukkit.getOnlinePlayers()) {

            if (pl.equals(p)) continue;
            ((CraftPlayer) pl).getHandle().b.a(new PacketPlayOutEntityDestroy(stands.get("youClaimed").getEntityId()));

        }

    }

    public void unclaim() {

        owner = null;

        // commit ethnic cleansing
        Iterator<Map.Entry<String, ArmorStand>> i = stands.entrySet().iterator();
        while (i.hasNext()) {

            Map.Entry<String, ArmorStand> s = i.next();
            s.getValue().remove();
            i.remove();

        }

        stands.put("clickHere", (ArmorStand) head.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY() + 0.3, head.getZ() + 0.5),
                EntityType.ARMOR_STAND));
        stands.get("clickHere").setMarker(true);
        stands.get("clickHere").setInvisible(true);
        stands.get("clickHere").setGravity(false);
        stands.get("clickHere").setCustomName(ChatColor.GREEN + "Click here to claimed this island!");
        stands.get("clickHere").setCustomNameVisible(true);
        stands.get("clickHere").addScoreboardTag("removable");

        head.getBlock().setType(Material.RED_WOOL);

    }

    HashMap<Player, Integer> crouching = new HashMap<>();

    public void run() {

        if (owner == null) return;

        for (Entity e : stands.get("crouchHere").getNearbyEntities(4, 4, 4))
            if (e instanceof Player && ((Player) e).isSneaking()) {

                if ((e.equals(owner))) continue;

                int sneakTime = 0;
                if (crouching.containsKey((Player) e))
                    sneakTime = crouching.get((Player) e) + 1;
                crouching.put((Player) e, sneakTime);

            }

        Iterator<Map.Entry<Player, Integer>> i = crouching.entrySet().iterator();
        while (i.hasNext()) {

            Map.Entry<Player, Integer> e = i.next();
            if (!e.getKey().isSneaking()) {

                e.getKey().sendMessage("Stopped crouching.");
                i.remove();
                continue;

            }

            if (e.getValue() >= 200) {

                e.getKey().sendMessage("DONE!");
                crouching.clear();
                unclaim();
                return;

            }

            e.getKey().sendTitle(getBar(((double) e.getValue()) / 200), "", 0, 2, 0);

        }

    }

    public void remove() {

        Iterator<Map.Entry<String, ArmorStand>> i = stands.entrySet().iterator();
        while (i.hasNext()) {

            Map.Entry<String, ArmorStand> s = i.next();
            s.getValue().remove();
            i.remove();

        }

        GenManager.flags.remove(this);
        GenManager.flagFile.removeLine(name + "@" + head.getWorld().getName() + "@" + head.getX() +
                "@" + head.getY() + "@" + head.getZ() + "@"
                + pole.getX() + "@" + pole.getY() + "@" + pole.getZ());

    }

    static String getBar(double v) {

        // v should be a number between 0 and 1

        int bars = 20;
        v = Math.round(bars * v);
        String s = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[";
        for (int i = 1; i <= v; i++) s += ChatColor.RESET + "" + ChatColor.GREEN + "|";
        for (int i = 1; i <= bars - v; i++) s += ChatColor.RESET + "" + ChatColor.GRAY + "|";
        s += ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "]";

        return s;

    }

}

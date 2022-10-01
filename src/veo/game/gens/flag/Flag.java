package veo.game.gens.flag;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import veo.Main;
import veo.game.gens.GenManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Flag {

    public String name;
    public OfflinePlayer owner;
    public Location head, pole;

    public HashMap<String, ArmorStand> stands = new HashMap<>();
    private int alert = 0;
    private FlagStructure flag;

    public Flag(String name, Location head, Location pole) {

        this.head = new Location(head.getWorld(), Math.floor(head.getX()), head.getY(),
                Math.floor(head.getZ()));
        this.pole = pole;
        this.name = name;
        this.flag = new FlagStructure(pole);

        // generate blocks
        flag.createStruct();

        unclaim();

    }

    public void claim(Player p) {

        owner = p;
        Main.sendMessage(p, ChatColor.GREEN + "You claimed the island of "
                + name, false);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 3);

        head.getBlock().setType(Material.AIR);
        stands.get("clickHere").remove();
        stands.remove("clickHere", stands.get("clickHere"));

        stands.put("statue", (ArmorStand) p.getWorld().spawnEntity(
                new Location(head.getWorld(), head.getX() + 0.5, head.getY(), head.getZ() + 0.5),
                EntityType.ARMOR_STAND));
        stands.get("statue").setMarker(true);
        stands.get("statue").setBasePlate(false);
        stands.get("statue").setGravity(false);
        stands.get("statue").addScoreboardTag("removable");
        // give style to the lil' guy
        {

            stands.get("statue").setArms(true);
            stands.get("statue").setHeadPose(new EulerAngle(15, 0, 0));
            stands.get("statue").setRightArmPose(new EulerAngle(115, 335, 0));
            stands.get("statue").setLeftArmPose(new EulerAngle(0, 0, 10));
            stands.get("statue").setLeftLegPose(new EulerAngle(340, 0, 0));
            ItemStack h = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta hm = (SkullMeta) h.getItemMeta();
            hm.setOwningPlayer(owner);
            h.setItemMeta(hm);
            stands.get("statue").setHelmet(h);
            ItemStack c = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta cm = (LeatherArmorMeta) c.getItemMeta();
            cm.setColor(Color.BLACK);
            c.setItemMeta(cm);
            stands.get("statue").setChestplate(c);
            ItemStack l = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta lm = (LeatherArmorMeta) l.getItemMeta();
            lm.setColor(Color.BLACK);
            l.setItemMeta(lm);
            stands.get("statue").setLeggings(l);
            ItemStack b = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta bm = (LeatherArmorMeta) b.getItemMeta();
            bm.setColor(Color.BLACK);
            b.setItemMeta(bm);
            stands.get("statue").setBoots(b);

        }
        // give heading to the lil' guy
        /*{

            Location tl = stands.get("statue").getLocation();
            double dx = pole.getX() - head.getX(),
                    dz = pole.getZ() - head.getZ();
            tl.setYaw((float) Math.toDegrees(Math.atan(dx / dz)) - 90);
            stands.get("statue").teleport(tl);

        }*/

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

        flag.raise(8, owner.getPlayer());

    }

    public void unclaim() {

        if (owner != null)
            FlagManager.cooldown.putIfAbsent(owner, 0);
        owner = null;

        // become serbian
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
        stands.get("clickHere").setCustomName(ChatColor.GREEN + "Click here to claim this island!");
        stands.get("clickHere").setCustomNameVisible(true);
        stands.get("clickHere").addScoreboardTag("removable");

        // shittiest line(s) of code I've ever written in my entire life
        long t = 0L;
        if (Bukkit.getOnlinePlayers().size() == 0) t = 100L;
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "setblock " + ((int) head.getX()) + " " + ((int) head.getY()) + " " + ((int) head.getZ()) +
                    " minecraft:player_head[rotation=0]{SkullOwner:{" +
                    "Id:[I;-1217896545,-1015529000,-1506369421,548850094],Properties:{textures:[{Value:" +
                    "\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJkZDExZGEwNDI1MmY3NmI2OTM0YmMyNjYxMmY1NGYyNjRmMzBlZWQ3NGRmODk5NDEyMDllMTkxYmViYzBhMiJ9fX0=\"}]}}}");

        }, t);

        flag.tear();

    }

    private final HashMap<Player, Integer> crouching = new HashMap<>();
    String load;

    public void run() {

        if (owner == null) return;

        for (Entity e : stands.get("crouchHere").getNearbyEntities(2, 2, 2))
            if (e instanceof Player && ((Player) e).isSneaking()) {

                if ((e.equals(owner))) continue;

                int sneakTime = 0;
                if (crouching.containsKey((Player) e))
                    sneakTime = crouching.get((Player) e) + 1;
                crouching.put((Player) e, sneakTime);

            }

        if (alert != 200 && alert != 0) alert++;
        else alert = 0;

        if (!crouching.isEmpty() && alert == 0) {

            owner.getPlayer().playSound(owner.getPlayer().getLocation(), Sound.ENTITY_CAT_AMBIENT, 1, 1);
            Main.sendMessage(owner.getPlayer(), ChatColor.DARK_RED + "" + ChatColor.BOLD
                    + "Warning! One of your islands is under attack!", false);
            alert = 1;

        }

        Iterator<Map.Entry<Player, Integer>> i = crouching.entrySet().iterator();
        while (i.hasNext()) {

            Map.Entry<Player, Integer> e = i.next();
            if (!e.getKey().isSneaking()) {

                //e.getKey().sendMessage("Stopped crouching.");
                i.remove();
                continue;

            }

            if (e.getValue() >= 160) {

                Main.sendMessage(e.getKey(), ChatColor.GREEN + "GG!", false);
                Main.sendMessage(owner.getPlayer(), ChatColor.DARK_RED + "" + ChatColor.BOLD +
                        e.getKey().getName() + " has unclaimed one of your islands!", false);
                owner.getPlayer().playSound(owner.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                crouching.clear();
                unclaim();
                return;

            }

            /*String dots = ChatColor.GRAY + ".";
            if (alert % 5 == 0) dots += ".";
            if (alert % 10 == 0) dots += ".";
            if (alert % 15 == 0) dots += ".";*/

            if (alert % 10 == 0) load = ChatColor.GRAY + "|";
            if (alert % 20 == 0) load = ChatColor.GRAY + "/";
            if (alert % 30 == 0) load = ChatColor.GRAY + "--";
            if (alert % 40 == 0) load = ChatColor.GRAY + "\\";
            e.getKey().sendTitle(getBar(((double) e.getValue()) / 160), load, 0, 2, 0);

        }

    }

    public void remove() {

        Iterator<Map.Entry<String, ArmorStand>> i = stands.entrySet().iterator();
        while (i.hasNext()) {

            Map.Entry<String, ArmorStand> s = i.next();
            s.getValue().remove();
            i.remove();

        }

        FlagManager.flags.remove(this);
        FlagManager.flagFile.removeLine(name + "@" + head.getWorld().getName() + "@" + head.getX() +
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

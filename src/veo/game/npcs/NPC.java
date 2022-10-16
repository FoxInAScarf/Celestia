package veo.game.npcs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_19_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import veo.Main;
import veo.essentials.zfm.ZFile;
import veo.game.items.ZItem;
import veo.game.items.ZItemManager;
import veo.game.shop.Shop;
import veo.game.shop.ShopManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NPC extends ZFile {

    /*
    *
    * name=&2Farmer
    * subname=I farm lol
    * location=world@20@20@20@50@50
    * action=shop
    * value=farmshop
    * skinValue=<base64 value>
    *
    * */
    String name, displayName, displaySubname;
    Location l;
    int actionType = 0;
    Object value;
    ArmorStand as;
    EntityPlayer npc;
    String skinValue = "", skinSignature = "";
    Slime hitbox1, hitbox2;
    boolean loadedHitbox = false;

    public NPC(File f) {

        super(f.getAbsolutePath());

        List<String> nLines = new ArrayList<>();
        for (String s : lines) nLines.add(s.replaceAll("&", "§"));
        lines.clear();
        lines.addAll(nLines);
        name = f.getName().replaceAll(".zra", "");

        HashMap<String, String> data = getData();
        if (data.containsKey("name")) displayName = data.get("name");
        if (data.containsKey("subname")) displaySubname = data.get("subname");
        if (data.containsKey("location")) {

            String[] ss = data.get("location").split("@");
            l = new Location(Bukkit.getWorld(ss[0]),
                    Double.parseDouble(ss[1]),
                    Double.parseDouble(ss[2]),
                    Double.parseDouble(ss[3]),
                    (float) Double.parseDouble(ss[4]),
                    (float) Double.parseDouble(ss[5]));

        }
        if (data.containsKey("actionType") && data.containsKey("value")) switch (data.get("actionType")) {

            case "shop" -> {

                actionType = 0;
                Shop s = ShopManager.getShop(data.get("value"));
                if (s == null) {

                    System.out.println("[NPCReader-ERROR]: Invalid shop name in '" + name + ".zra'!");
                    return;

                }
                value = s;

            }
            case "item" -> {

                actionType = 1;
                ItemStack i;
                Material m = Material.getMaterial(data.get("value").toUpperCase());
                if (m == null) i = ZItemManager.getItem(data.get("value"));
                else i = new ItemStack(m);
                if (i == null) {

                    System.out.println("[NPCReader-ERROR]: Invalid item name in '" + name + ".zra'!");
                    return;

                }
                value = i;

            }
            case "dialogue" -> {

                actionType = 2;
                /*
                *
                * do this later Zraphy
                *
                * */

            }
            case "teleport" -> {

                actionType = 3;
                String[] ss = data.get("value").split("@");
                value = new Location(Bukkit.getWorld(ss[0]),
                        Double.parseDouble(ss[1]),
                        Double.parseDouble(ss[2]),
                        Double.parseDouble(ss[3]),
                        (float) Double.parseDouble(ss[4]),
                        (float) Double.parseDouble(ss[5]));

            }
            case "command" -> {

                actionType = 4;
                value = data.get("value");

            }
            case "text" -> {

                actionType = 5;
                value = data.get("value");

            }
            default -> {

                System.out.println("[NPCReader-ERROR]: Invalid action type in '" + name + ".zra'!");
                return;

            }

        }
        if (data.containsKey("skinValue")) skinValue = data.get("skinValue");
        if (data.containsKey("skinSignature")) skinSignature = data.get("skinSignature");

        if (l.getChunk().isLoaded()) makeHitboxes();


        System.out.println("[NPCReader]: '" + name + "' loaded up successfully!");

    }

    private HashMap<String, String> getData() {

        HashMap<String, String> data = new HashMap<>();
        for (String s : lines) if (s.contains(":")) {

            String[] ss = s.split(":");
            data.put(ss[0], ss[1]);

        }
        return data;

    }

    public void makeHitboxes() {

        as = (ArmorStand) l.getWorld().spawnEntity(l.clone().add(0, 2.05, 0), EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setMarker(true);
        as.setInvisible(true);
        as.setCustomName(displayName);
        as.setCustomNameVisible(true);
        as.addScoreboardTag("removable");

        hitbox1 = (Slime) l.getWorld().spawnEntity(l, EntityType.SLIME);
        hitbox1.setInvisible(true);
        hitbox1.setAI(false);
        hitbox1.setSilent(true);
        hitbox1.addScoreboardTag("removable");
        hitbox1.setSize(2);

        hitbox2 = (Slime) l.getWorld().spawnEntity(l.clone().add(0, 1, 0), EntityType.SLIME);
        hitbox2.setInvisible(true);
        hitbox2.setAI(false);
        hitbox2.setSilent(true);
        hitbox2.addScoreboardTag("removable");
        hitbox2.setSize(2);
        loadedHitbox = true;

    }

    public void createTo(Player p) {

        if (!loadedHitbox) makeHitboxes();


        // Thank you, Stephen (stephen#2067) for helping with this <3 you're an absolute hero

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displaySubname);
        gameProfile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) l.getWorld()).getHandle();
        npc = new EntityPlayer(server, world, gameProfile, null);
        npc.a(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());

        EntityPlayer connection = ((CraftPlayer) p).getHandle();
        connection.b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
        connection.b.a(new PacketPlayOutNamedEntitySpawn(npc));
        connection.b.a(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.getBukkitYaw() * 256 / 360)));
        DataWatcher watcher = npc.ai();
        watcher.b(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);
        connection.b.a(new PacketPlayOutEntityMetadata(npc.ae(), watcher, true));

        // https://www.youtube.com/watch?v=Avwg6ZCQX1o
        // ^^ cool person

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

            connection.b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc));

        }, 5L);

    }

}

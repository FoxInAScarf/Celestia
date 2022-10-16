package veo.game.npcs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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
    * skin=<base64 value>
    *
    * */
    String name, displayName, displaySubname;
    Location l;
    int actionType = 0;
    Object value;

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
            default -> {

                System.out.println("[NPCReader-ERROR]: Invalid action type in '" + name + ".zra'!");
                return;

            }

        }

    }

    private HashMap<String, String> getData() {

        HashMap<String, String> data = new HashMap<>();
        for (String s : lines) if (s.contains("=")) {

            String[] ss = s.split("=");
            data.put(ss[0], ss[1]);

        }
        return data;

    }

    public void createTo(Player p) {

        // Thank you, Stephen (stephen#2067) for helping with this <3 you're an absolute hero

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        //gameProfile.getProperties().put("textures", new Property("textures", , ));
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) l.getWorld()).getHandle();
        EntityPlayer fakePlayer = new EntityPlayer(server, world, gameProfile, null);
        fakePlayer.a(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());

        EntityPlayer connection = ((CraftPlayer) p).getHandle();
        connection.b.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, fakePlayer));
        connection.b.a(new PacketPlayOutNamedEntitySpawn(fakePlayer));
        connection.b.a(new PacketPlayOutEntityHeadRotation(fakePlayer, (byte) (fakePlayer.getBukkitYaw() * 256 / 360)));
        DataWatcher watcher = fakePlayer.ai();
        watcher.b(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);
        connection.b.a(new PacketPlayOutEntityMetadata(fakePlayer.ae(), watcher, true));

    }

}

package veo.game.npcs;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import veo.Main;
import veo.game.shop.Shop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NPCManager {

    static String folder;
    static List<NPC> npcs = new ArrayList<>();

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/NPCs";
        if (!new File(folder).exists()) new File(folder).mkdir();

        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
        load();
        Main.getInstance().getCommand("npc").setExecutor(new NPCCommand());

    }

    public static void load() {

        // clear
        for (NPC n : npcs) {

            n.as1.remove();
            n.as2.remove();
            /*for (Entity e : n.l.getWorld().getEntities())
                if (e.getScoreboardTags().contains("removable-" + Main.removableTag) && e.getType().equals(EntityType.ARMOR_STAND))
                    e.remove();*/

            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(n.npc.ae());
            for (Player p : Bukkit.getOnlinePlayers()) ((CraftPlayer) p).getHandle().b.a(packet);

        }
        npcs.clear();

        // do stuff
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) npcs.add(new NPC(f));
        for (NPC n : npcs) for (Player p : Bukkit.getOnlinePlayers()) {

            n.createTo(p);
            if (n.l.getChunk().isLoaded()) n.makeHitboxes();

        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            for (NPC n : npcs) if (n.l.getChunk().isLoaded()) n.makeHitboxes();

        }, 10L, 10L);

    }

    public static NPC getNPC(String name) {

        for (NPC n : npcs) if (n.name.equals(name)) return n;
        return null;

    }

}

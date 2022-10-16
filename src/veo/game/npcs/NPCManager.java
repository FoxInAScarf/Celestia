package veo.game.npcs;

import org.bukkit.Bukkit;
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

        npcs.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) npcs.add(new NPC(f));
        for (NPC n : npcs) for (Player p : Bukkit.getOnlinePlayers()) n.createTo(p);

    }

    public static NPC getNPC(String name) {

        for (NPC n : npcs) if (n.name.equals(name)) return n;
        return null;

    }

}

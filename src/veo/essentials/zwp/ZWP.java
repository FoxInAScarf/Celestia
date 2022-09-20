package veo.essentials.zwp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZWP {

    public static boolean running = true;
    static String folder;
    static ZFile ac;
    public static List<Block> allowedContainers = new ArrayList<>();

    public static void init(JavaPlugin main) {

        folder = Main.mainFolder.getAbsolutePath() + "/ZWP";
        if (!new File(folder).exists()) new File(folder).mkdir();

        ac = new ZFile(folder + "/allowedContainers.zra");
        for (String s : ac.lines) {

            String[] ss = s.split(" ");
            World w = Bukkit.getWorld(ss[0]);
            allowedContainers.add(w.getBlockAt(Integer.parseInt(ss[1]),
                    Integer.parseInt(ss[2]), Integer.parseInt(ss[3])));

        }

        Bukkit.getPluginManager().registerEvents(new ZWPListeners(), main);
        main.getCommand("zwp").setExecutor(new ZWPCommand());

    }

}

package veo.essentials.zwp;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;
import veo.essentials.zwp.areas.AreaManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZWP {

    public static boolean running = true;
    public static String folder;
    static ZFile ac, wd;
    static Location respawn, pvpProtectionCentre;
    static int pvpProtectionRadius;
    public static List<Block> allowedContainers = new ArrayList<>();

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/ZWP";
        if (!new File(folder).exists()) new File(folder).mkdir();

        ac = new ZFile(folder + "/allowedContainers.zra");
        for (String s : ac.lines) {

            if (s.equals("")) continue;

            String[] ss = s.split(" ");
            World w = Bukkit.getWorld(ss[0]);
            allowedContainers.add(w.getBlockAt(Integer.parseInt(ss[1]),
                    Integer.parseInt(ss[2]), Integer.parseInt(ss[3])));

        }

        wd = new ZFile(folder + "/worldData.zra");
        for (String s : wd.lines) {

            if (!s.contains("=")) continue;
            String[] ss = s.split("=");
            if (ss[0].equals("spawn")) {

                String[] ssa = ss[1].split("@");
                respawn = new Location(Bukkit.getWorld(ssa[0]),
                        Double.parseDouble(ssa[1]),
                        Double.parseDouble(ssa[2]),
                        Double.parseDouble(ssa[3]),
                        (float) Double.parseDouble(ssa[4]),
                        (float) Double.parseDouble(ssa[5]));

            }
            /*if (ss[0].equals("pvpProtection")) {

                String[] ssa = ss[1].split("@");
                pvpProtectionCentre = new Location(Bukkit.getWorld(ssa[0]),
                        Integer.parseInt(ssa[1]),
                        Integer.parseInt(ssa[2]),
                        Integer.parseInt(ssa[3]));
                pvpProtectionRadius = Integer.parseInt(ssa[4]);

            }*/

        }
        AreaManager.init();

        for (World w : Bukkit.getWorlds()) {

            w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            w.setGameRule(GameRule.KEEP_INVENTORY, true);
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            w.setGameRule(GameRule.MOB_GRIEFING, false);
            w.setGameRule(GameRule.DO_FIRE_TICK, false);
            w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);

        }

        Bukkit.getPluginManager().registerEvents(new ZWPListeners(), Main.getInstance());
        Main.getInstance().getCommand("zwp").setExecutor(new ZWPCommand());
        Main.getInstance().getCommand("spawn").setExecutor(new SpawnCommand());

    }

}

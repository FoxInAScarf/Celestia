package veo.game.gens;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;
import veo.game.gens.flag.FlagManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenManager {
    public static List<Generator> gens = new ArrayList<>();
    static ZFile genFile;

    public static boolean running = true;


    public static void init() {

        String folder = Main.mainFolder.getAbsolutePath() + "/Gens";
        if (!new File(folder).exists()) new File(folder).mkdir();

        genFile = new ZFile(folder + "/gens.zra");
        for (String l : genFile.lines) {

            String[] ls = l.split("@");
            Location location = new Location(Bukkit.getWorld(ls[2]),
                    Double.parseDouble(ls[3]),
                    Double.parseDouble(ls[4]),
                    Double.parseDouble(ls[5]));
            gens.add(new Generator(ls[0], ls[1],
                    location, Integer.parseInt(ls[6]), Material.getMaterial(ls[7])));

        }

        FlagManager.init(new File(folder));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (running)
                for (Generator g : gens) g.run();

        }, 0L, 1L);

        Bukkit.getPluginManager().registerEvents(new GenListeners(), Main.getInstance());
        Main.getInstance().getCommand("gens").setExecutor(new GenCommand());

        /*Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            for (Generator g : GenManager.gens) if (g.l.getWorld() != null) {

                    int genItems = 0, genNames = 0;
                    for (Entity c : g.l.getWorld().getEntities()) {

                        if (c.getScoreboardTags().contains(g.name + "GenItem")) genItems++;
                        if (genItems > 1) {

                            c.remove();
                            genItems--;
                            continue;

                        }
                        if (c.getScoreboardTags().contains(g.name + "GenName")) genNames++;
                        if (genNames > 1) {

                            c.remove();
                            genNames++;

                        }

                    }

                }

        }, 20L, 20L);*/

    }

    public static Generator getGen(String name) {

        for (Generator g : gens) if (g.name.equals(name))
            return g;
        return null;

    }

}

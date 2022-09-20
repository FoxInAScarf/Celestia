package veo.game.gens;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenManager {

    static List<Generator> gens = new ArrayList<>();
    static List<Flag> flags = new ArrayList<>();
    static ZFile genFile, flagFile;

    static boolean running = true;

    public static void init(JavaPlugin main) {

        String folder = Main.mainFolder.getAbsolutePath() + "/Gens";
        if (!new File(folder).exists()) new File(folder).mkdir();

        genFile = new ZFile(folder + "/gens.zra");
        for (String l : genFile.lines) {

            String[] ls = l.split("@");
            Location location = new Location(Bukkit.getWorld(ls[2]),
                    Double.parseDouble(ls[3]),
                    Double.parseDouble(ls[4]),
                    Double.parseDouble(ls[5]));
            gens.add(new Generator(ls[0], Material.getMaterial(ls[1]),
                    location, Integer.parseInt(ls[6])));

        }

        flagFile = new ZFile(folder + "/flags.zra");
        for (String l : flagFile.lines) {

            String[] ls = l.split("@");
            World w = Bukkit.getWorld(ls[1]);
            Location head = new Location(w, Double.parseDouble(ls[2]),
                    Double.parseDouble(ls[3]),
                    Double.parseDouble(ls[4]));
            Location pole = new Location(w, Double.parseDouble(ls[5]),
                    Double.parseDouble(ls[6]),
                    Double.parseDouble(ls[7]));
            flags.add(new Flag(ls[0], head, pole));

        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

            if (running)
                for (Generator g : gens) g.run();

        }, 0L, 1L);

        Bukkit.getPluginManager().registerEvents(new GenListeners(), main);
        main.getCommand("gens").setExecutor(new GenCommand());

    }

    public static Generator getGen(String name) {

        for (Generator g : gens) if (g.name.equals(name))
            return g;
        return null;

    }

    public static Flag getFlag(String name) {

        for (Flag f : flags) if (f.name.equals(name))
            return f;
        return null;

    }

}

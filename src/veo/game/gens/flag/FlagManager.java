package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.*;

public class FlagManager {

    static List<FlagData> fs = new ArrayList<>();
    private static File folder;
    public static HashMap<Player, Integer> cooldown = new HashMap<>();
    public static List<Flag> flags = new ArrayList<>();

    public static void init(File folder) {

        FlagManager.folder = folder;
        /*
        *
        * READ FLAGS
        *
        * */
        for (File f : folder.listFiles())
            fs.add(new FlagData(f.getName().replaceAll(".zra", ""), f.getAbsolutePath()));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            Iterator<Map.Entry<Player, Integer>> i = cooldown.entrySet().iterator();
            if (i.next().getValue() >= (20 * 60 * 10)) i.remove();

        }, 0L, 1L);

        Main.getInstance().getCommand("flag").setExecutor(new FlagCommand());

    }

    public static FlagData getFlag(Player p) {

        for (FlagData fd : fs) if (fd.p.getPlayer().equals(p))
            return fd;

        return null;

    }

    public static FlagData addFlag(Player p, Material[][] m) {

        String UUID = p.getUniqueId().toString();
        File f = new File(folder.getAbsolutePath() + "/" + UUID + ".zra");
        if (Arrays.stream(folder.listFiles()).toList().contains(f))
            return null;

        FlagData d = new FlagData(UUID, f.getAbsolutePath());
        d.setData(m);
        d.saveL();
        fs.add(d);

        System.out.println("created: " + f.getAbsolutePath());

        return d;

    }

    public static Flag getFlag(String name) {

        for (Flag f : flags) if (f.name.equals(name))
            return f;
        return null;

    }

}

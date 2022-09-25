package veo.game.gens.flag;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FlagManager {

    static List<FlagData> fs = new ArrayList<>();
    private static File folder;

    public static void init(File folder) {

        FlagManager.folder = folder;
        /*
        *
        * READ FLAGS
        *
        * */
        for (File f : folder.listFiles())
            fs.add(new FlagData(f.getName().replaceAll(".zra", ""), f.getAbsolutePath()));

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

}

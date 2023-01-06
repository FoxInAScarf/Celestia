package veo.game.custom.holograms;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import veo.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class HologramManager {

    static String folder;
    static List<Hologram> holograms = new ArrayList<>();

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/Holograms";
        if (!new File(folder).exists()) new File(folder).mkdir();

        load();
        Main.getInstance().getCommand("hologram").setExecutor(new HologramCommand());

    }

    public static void load() {

        if (!holograms.isEmpty()) for (Hologram h : holograms) for (ArmorStand s : h.armorStands) s.remove();
        holograms.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) holograms.add(new Hologram(f));

    }

}

package veo.game.gens.flag;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import veo.Main;
import veo.essentials.zfm.ZFile;
import veo.game.gens.GenManager;
import veo.game.gens.Generator;

import java.io.File;
import java.util.*;

public class FlagManager {

    // CUSTOM FLAG SYSTEM
    //static List<FlagData> fs = new ArrayList<>();
    private static File folder;
    public static List<FlagCooldown> cooldown = new ArrayList<>();
    public static List<Flag> flags = new ArrayList<>();
    public static ZFile flagFile;

    public static void init(File folder) {

        FlagManager.folder = folder;

        File flagFolder = new File(folder.getAbsolutePath() + "/Flags");
        if (!flagFolder.exists()) flagFolder.mkdir();
        // CUSTOM FLAG SYSTEM
        /*for (File f : Objects.requireNonNull(flagFolder.listFiles()))
            fs.add(new FlagData(f.getName().replaceAll(".zra", ""), f.getAbsolutePath()));*/

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

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (!GenManager.running) return;
            for (Flag f : flags) f.run();
            for (int i = 0; i <= cooldown.size() - 1; i++) cooldown.get(i).update();
            // ^ operates as an iterator

        }, 0L, 1L);

        Main.getInstance().getCommand("flag").setExecutor(new FlagCommand());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            for (Player p : Bukkit.getOnlinePlayers())
                for (Generator g : GenManager.gens) {

                    Flag f = FlagManager.getFlag(g.name);

                    if (f == null) continue;
                    if (f.owner != null) {

                        f.flag.raise(8, f.owner.getPlayer());
                        if (f.owner.getUniqueId().equals(p.getUniqueId())) {

                            ((CraftPlayer) p).getHandle().b.a(
                                    new PacketPlayOutEntityDestroy(f.stands.get("hasClaimed").getEntityId()));
                            ((CraftPlayer) p).getHandle().b.a(
                                    new PacketPlayOutEntityDestroy(f.stands.get("crouchHere").getEntityId()));

                        } else ((CraftPlayer) p).getHandle().b.a(
                                new PacketPlayOutEntityDestroy(f.stands.get("youClaimed").getEntityId()));

                    }


                }

        }, 10L, 10L);

    }

    // CUSTOM FLAG SYSTEM
    /*public static FlagData getFlag(Player p) {

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

    }*/

    public static Flag getFlag(String name) {

        for (Flag f : flags) if (f.name.equals(name))
            return f;
        return null;

    }

    public static FlagCooldown getCooldown(OfflinePlayer p) {

        for (FlagCooldown fc : cooldown) if (fc.p.getUniqueId().equals(p.getUniqueId()))
            return fc;
        return null;

    }

}

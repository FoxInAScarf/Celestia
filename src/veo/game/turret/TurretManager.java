package veo.game.turret;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TurretManager {


    /*
    *
    *
    * THIS IS NOT NEEDED
    *
    *
    * */


    ZFile turretfile;
    List<Turret> turrets = new ArrayList<>();

    public void init(JavaPlugin main) {

        String folder = Main.mainFolder.getAbsolutePath() + "/Custom";
        if (!new File(folder).exists()) new File(folder).mkdir();

        turretfile = new ZFile(folder + "/turrets.zra");
        for (String l : turretfile.lines) {

            String[] ls = l.split("_");
            turrets.add(new Turret(Bukkit.getWorld(ls[0]), Double.parseDouble(ls[1]),
                    Double.parseDouble(ls[2]),
                    Double.parseDouble(ls[3]),
                    UUID.fromString(ls[4])));

        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

            for (Turret t : turrets) t.update();

        }, 0L, 1L);

    }

}

package veo.essentials.zwp.areas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import veo.essentials.zfm.ZFile;
import veo.essentials.zwp.ZWP;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    static List<Area> areas = new ArrayList<>();

    public static void init() {

        ZFile file = new ZFile(ZWP.folder + "/areas.zra");
        for (String s : file.lines) {

            // type@world@x1@z1@x1@z1
            String[] ss = s.split("@");
            areas.add(

                    new Area(Integer.parseInt(ss[0]),
                    new Location(Bukkit.getWorld(ss[1]), Double.parseDouble(ss[2]), -64, Double.parseDouble(ss[3])),
                    new Location(Bukkit.getWorld(ss[1]), Double.parseDouble(ss[4]), 255, Double.parseDouble(ss[5])))

            );

        }

    }

    public static boolean isPlayerInArea(int type, Player p) {

        for (Area ar : areas) {

            if (ar.type != type) continue;
            boolean x, z;

            if (ar.a.getX() >= 0) x = ar.a.getX() < p.getLocation().getX() && p.getLocation().getX() < ar.b.getX();
            else x = ar.a.getX() > p.getLocation().getX() && p.getLocation().getX() > ar.b.getX();

            if (ar.a.getZ() >= 0) z = ar.a.getZ() < p.getLocation().getZ() && p.getLocation().getZ() < ar.b.getZ();
            else z = ar.a.getZ() > p.getLocation().getZ() && p.getLocation().getZ() > ar.b.getZ();

            return x && z;

        }
        return false;

    }

}

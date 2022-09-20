package veo.game.turret;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class Turret {


    /*
    *
    *
    *
    * THIS IS NOT NEEDED
    *
    *
    *
    * */

    Location l;
    OfflinePlayer p;
    ArmorStand b;

    public Turret(World w, double x, double y, double z, UUID p) {

        l = new Location(w, x, y, z);
        this.p = Bukkit.getOfflinePlayer(p);

    }

    public void update() {

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (!p.getWorld().equals(l.getWorld())) continue;
            double dx = p.getLocation().getX() - l.getX(),
                    dy = p.getLocation().getY() - l.getY(),
                    dz = p.getLocation().getZ() - l.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance <= 10) {



            }

        }

    }

}

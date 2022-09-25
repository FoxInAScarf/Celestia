package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import veo.Main;

public class FlagStructure {

    private int raiseLoop = 0, raiseIteration = 0;
    private Location pole;

    public FlagStructure(Location pole) {

        this.pole = pole;

    }

    /*public void raise(FlagData data) {

        raiseLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (raiseIteration == 6) {

                Bukkit.getScheduler().cancelTask(raiseLoop);

            }



        }, 0L, 10L);

    }*/

    public void raise(int h, FlagData data) {

        h = (int) pole.getY() + h;
        for (int y = 0; y <= data.height - 1; y++)
            for (int x = 0; x <= data.width - 1; x++) {

                Material m = data.m[y][x];
                Location l = new Location(pole.getWorld(), pole.getX() + x + 1, h - y, pole.getZ());

                l.getBlock().setType(m);

            }

    }

    public void tear() {



    }

}

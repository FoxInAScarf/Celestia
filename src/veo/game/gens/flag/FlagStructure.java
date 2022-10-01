package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import veo.Main;

public class FlagStructure {

    //private int raiseLoop = 0, raiseIteration = 0;
    private final Location pole;

    public FlagStructure(Location pole) {

        this.pole = pole;

    }

    // CUSTOM FLAG SYSTEM
    /*public void raise(FlagData data) {

        raiseLoop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (raiseIteration == 6) {

                Bukkit.getScheduler().cancelTask(raiseLoop);

            }



        }, 0L, 10L);

    }*/

    /*public void raise(int h, FlagData data) {

        h = (int) pole.getY() + h;
        for (int y = 0; y <= data.height - 1; y++)
            for (int x = 0; x <= data.width - 1; x++) {

                Material m = data.m[y][x];
                Location l = new Location(pole.getWorld(), pole.getX() + x + 1, h - y, pole.getZ());

                l.getBlock().setType(m);

            }

    }

    public void tear() {



    }*/

    public void raise(int h, Player owner) {

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (p.equals(owner)) {

                p.sendBlockChange(pole, Material.GREEN_WOOL.createBlockData());
                continue;

            }
            p.sendBlockChange(pole, Material.RED_WOOL.createBlockData());

        }

        h = (int) pole.getY() + h;
        for (int y = 0; y <= 3 - 1; y++)
            for (int x = 0; x <= 4 - 1; x++) {

                Location l = new Location(pole.getWorld(), x + 1, h - y, pole.getZ());
                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (p.equals(owner)) {

                        p.sendBlockChange(l, Material.GREEN_WOOL.createBlockData());
                        continue;

                    }
                    p.sendBlockChange(l, Material.RED_WOOL.createBlockData());

                }

            }

    }

    public void tear() {



    }

    public void createStruct() {

        for (int i = 0; i <= 9; i++)
            new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + i, this.pole.getZ())
                    .getBlock().setType(Material.OAK_FENCE);
        new Location(this.pole.getWorld(), this.pole.getX(), this.pole.getY() + 10, this.pole.getZ())
                .getBlock().setType(Material.DARK_OAK_SLAB);

    }

}

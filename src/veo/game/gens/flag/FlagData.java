package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import veo.essentials.zfm.ZFile;

import java.util.UUID;

public class FlagData extends ZFile {

    public final int width = 4, height = 3;
    OfflinePlayer p;
    Material[][] m;

    public FlagData(String name, String path) {

        super(path);
        this.p = Bukkit.getOfflinePlayer(UUID.fromString(name));

        m = new Material[height][];
        for (int row = 0; row <= height - 1; row++) {

            String[] sl = lines.get(row).split(";");
            Material[] mRow = new Material[width];
            for (int column = 0; column <= width - 1; column++)
                mRow[column] = Material.getMaterial(sl[column]);

            m[row] = mRow;

        }

    }

}

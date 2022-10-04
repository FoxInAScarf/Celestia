package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import veo.Main;

public class FlagCooldown {

    public int time = 0, duration;
    private int loop = 0;
    public OfflinePlayer p;

    public FlagCooldown(OfflinePlayer p, int duration) {

        this.duration = duration;
        this.p = p;

        loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (time >= duration) {

                FlagManager.cooldown.remove(this);
                Bukkit.getScheduler().cancelTask(loop);

            }
            time++;

        }, 0L, 20 * 60 * 10);

    }

}

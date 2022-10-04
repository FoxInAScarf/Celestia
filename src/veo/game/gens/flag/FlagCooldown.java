package veo.game.gens.flag;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import veo.Main;

public class FlagCooldown {

    public int time = 0;
    public long duration;
    public OfflinePlayer p;

    public FlagCooldown(OfflinePlayer p, int duration) {

        this.duration = duration;
        this.p = p;

    }

    public void update() {

        if (time >= duration)
            FlagManager.cooldown.remove(this);
        time++;

    }

}

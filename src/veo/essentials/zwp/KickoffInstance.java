package veo.essentials.zwp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import veo.Main;

public class KickoffInstance {

    public Player victim, damager;
    private int time = 10, loop;

    public KickoffInstance(Player victim, Player damager) {

        this.victim = victim;
        this.damager = damager;

    }

    public KickoffInstance start() {

        loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (time <= 0) {

                Bukkit.getScheduler().cancelTask(loop);
                ZWPListeners.kickoffInstanceList.remove(this);

            }

            time--;

        }, 20L, 20L);
        return this;

    }

    public static Player getKiller(Player victim) {

        for (KickoffInstance i : ZWPListeners.kickoffInstanceList) if (i.victim.getUniqueId().equals(victim.getUniqueId())) return i.damager;
        return null;

    }

}

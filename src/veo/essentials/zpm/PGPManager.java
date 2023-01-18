package veo.essentials.zpm;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zpm.profiles.PlayerGameProfile;

public class PGPManager {

    public static void initTime() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            for (Player p : Bukkit.getOnlinePlayers()) {

                PlayerGameProfile pgp = ZPM.getPGP(p);
                if (pgp == null) {

                    System.out.println("ERROR: WHAT THE FUCK ZRAPHY???? (nonexistent player profile despite player joining, did you reload while players were on?)");
                    return;

                }
                pgp.timePlayed++;
                pgp.saveF();
                if (pgp.timePlayed == 30) Main.sendMessage(p, ChatColor.GREEN + "Your grace period is over! From now on you're vulnerable.", false);

            }

        }, 20L, 20L * 60L);

    }

}

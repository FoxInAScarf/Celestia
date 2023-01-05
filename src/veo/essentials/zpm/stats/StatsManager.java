package veo.essentials.zpm.stats;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import veo.Main;

public class StatsManager {

    public static void init() {

        Main.getInstance().getCommand("stats").setExecutor(new StatsCommand());
        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());

    }

    public static String getFormattedStats(int kills, int killStreak, int deaths, int flagsClaimed, int timePlayed) {

        int hours = (int) Math.floor((double) timePlayed / 60.0), mins = timePlayed % 60;
        return "\n" + ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Kills: " + ChatColor.of("#73dfff") + kills +
                "\n" + ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Killstreak: " + ChatColor.of("#73dfff") + killStreak +
                "\n" + ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Deaths: " + ChatColor.of("#73dfff") + deaths +
                "\n" + ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Flags claimed: " + ChatColor.of("#73dfff") + flagsClaimed +
                "\n" + ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Time played: " + ChatColor.of("#73dfff") + hours + ChatColor.of("#aceafc") + ":" + ChatColor.of("#73dfff") + mins + "\n";

    }

}

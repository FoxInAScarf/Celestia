package veo.essentials.zpm.stats;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;
import veo.essentials.zpm.ZPM;
import veo.essentials.zpm.profiles.PlayerGameProfile;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player p)) {

            System.out.println("Only a player can run this command!");
            return false;

        }

        if (args.length >= 1) {

            PlayerGameProfile pgp = null;
            for (PlayerGameProfile pgpc : ZPM.pgp) if (pgpc.p.getName().equals(args[0])) pgp = pgpc;
            if (pgp == null) {

                System.out.println("Zraphy you fucked up again!");
                Main.sendMessage(p, ChatColor.RED + "This player hasn't played on this server yet.", true);
                return false;

            }
            Main.sendMessage(p, "Statistics of " + ChatColor.of("#73dfff") + args[0] + ChatColor.RESET + ": ", false);
            p.sendMessage(StatsManager.getFormattedStats(pgp.kills, pgp.killStreak, pgp.deaths, pgp.flagsClaimed, pgp.timePlayed));

            return false;

        }

        PlayerGameProfile pgp = ZPM.getPGP(p);
        if (pgp == null) {

            System.out.println("Zraphy you fucked up again!");
            return false;

        }
        Main.sendMessage(p, ChatColor.GRAY + "" + ChatColor.ITALIC + "Why do you run this command if you can see your statistics on the scoreboard?", false);
        Main.sendMessage(p, "Your statistics: ", false);
        p.sendMessage(StatsManager.getFormattedStats(pgp.kills, pgp.killStreak, pgp.deaths, pgp.flagsClaimed, pgp.timePlayed));

        return false;

    }

}

package veo.essentials.zpm.stats;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import veo.essentials.zpm.ZPM;
import veo.essentials.zpm.profiles.PlayerGameProfile;

import java.util.Objects;

public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        addScoreboardTo(e.getPlayer());

    }

    public static void onUpdate(PlayerGameProfile pgp) {

        String[] stats = StatsManager.getFormattedStats(0, pgp.killStreak, 0/*deaths is never displayed on sidebar*/, pgp.flagsClaimed, pgp.timePlayed).split("\n");

        if (pgp.p.getPlayer() != null && pgp.p.getPlayer().getScoreboard().getTeam("playtime") == null) addScoreboardTo(pgp.p.getPlayer());
        //for (Player p : Bukkit.getOnlinePlayers()) addScoreboardTo(p);

        pgp.p.getPlayer().getScoreboard().getTeam("playtime").setPrefix(stats[5]);
        pgp.p.getPlayer().getScoreboard().getTeam("flagsClaimed").setPrefix(stats[4]);
        pgp.p.getPlayer().getScoreboard().getTeam("killStreak").setPrefix(stats[2]);
        pgp.p.getPlayer().getScoreboard().getTeam("kd").setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "K/D: " + ChatColor.of("#73dfff") + pgp.kills + ChatColor.of("#aceafc") + "/" + ChatColor.of("#73dfff") + pgp.deaths);

    }

    public static void addScoreboardTo(Player p) {

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("sidebar", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.of("#aceafc") + "☄ " + ChatColor.of("#aceafc").toString() + ChatColor.BOLD + "Celestia" + ChatColor.RESET + ChatColor.of("#aceafc") + " ☄");

        /*Score ip = obj.getScore(ChatColor.of("#73dfff") + "" + ChatColor.STRIKETHROUGH + "                                  ");
        ip.setScore(1);*/
        Team underline = board.registerNewTeam("underline");
        underline.addEntry(ChatColor.AQUA.toString());
        underline.setPrefix(ChatColor.of("#aceafc") + "" + ChatColor.STRIKETHROUGH + "                                        ");
        obj.getScore(ChatColor.AQUA.toString()).setScore(1);

        /*Score ip = obj.getScore("• " + ChatColor.of("#73dfff") + "/link for free perks!" + ChatColor.of("#ffdcf7") + " ❤");
        ip.setScore(2);*/
        Team link = board.registerNewTeam("link");
        link.addEntry(ChatColor.BOLD.toString());
        link.setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "/link for free perks!" + ChatColor.of("#ffdcf7") + " ❤");
        obj.getScore(ChatColor.BOLD.toString()).setScore(2);

        /*Score ip = obj.getScore("• " + ChatColor.of("#73dfff") + "IP: " + ChatColor.RESET + "CelestiaGens.minehut.gg");
        ip.setScore(3);*/
        Team ip = board.registerNewTeam("ip");
        ip.addEntry(ChatColor.BLACK.toString());
        ip.setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "IP: " + ChatColor.of("#73dfff") + "CelestiaGens.minehut.gg");
        obj.getScore(ChatColor.BLACK.toString()).setScore(3);

        /*Score server = obj.getScore(ChatColor.of("#73dfff") + "sᴇʀᴠᴇʀ");
        server.setScore(4);*/
        Team server = board.registerNewTeam("server");
        server.addEntry(ChatColor.BLUE.toString());
        server.setPrefix(ChatColor.of("#73dfff") + "sᴇʀᴠᴇʀ");
        obj.getScore(ChatColor.BLUE.toString()).setScore(4);

        Score space1 = obj.getScore(" ");
        space1.setScore(5);

        PlayerGameProfile pgp = ZPM.getPGP(p);
        if (pgp == null) {

            System.out.println("Zraphy... what the fuck did you do again?");
            return;

        }
        String[] stats = StatsManager.getFormattedStats(0, pgp.killStreak, 0/*deaths is never displayed on sidebar*/, pgp.flagsClaimed, pgp.timePlayed).split("\n");

        Team playtime = board.registerNewTeam("playtime");
        playtime.addEntry(ChatColor.DARK_GREEN.toString());
        playtime.setPrefix(stats[5]);
        obj.getScore(ChatColor.DARK_GREEN.toString()).setScore(6);

        Team flagsClaimed = board.registerNewTeam("flagsClaimed");
        flagsClaimed.addEntry(ChatColor.DARK_AQUA.toString());
        flagsClaimed.setPrefix(stats[4]);
        obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(7);

        Team killStreak = board.registerNewTeam("killStreak");
        killStreak.addEntry(ChatColor.DARK_BLUE.toString());
        killStreak.setPrefix(stats[2]);
        obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(8);

        Team kd = board.registerNewTeam("kd");
        kd.addEntry(ChatColor.DARK_GRAY.toString());
        kd.setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "K/D: " + ChatColor.of("#73dfff") + pgp.kills + ChatColor.of("#aceafc") + "/" + ChatColor.of("#73dfff") + pgp.deaths);
        obj.getScore(ChatColor.DARK_GRAY.toString()).setScore(9);

        /*Score statsAE = obj.getScore(ChatColor.of("#73dfff") + "sᴛᴀᴛs");
        statsAE.setScore(10);*/
        Team statsAE = board.registerNewTeam("stats");
        statsAE.addEntry(ChatColor.GRAY.toString());
        statsAE.setPrefix(ChatColor.of("#73dfff") + "sᴛᴀᴛs");
        obj.getScore(ChatColor.GRAY.toString()).setScore(10);

        Score space2 = obj.getScore("  ");
        space2.setScore(11);

        /*Score rank = obj.getScore("• " + ChatColor.of("#73dfff") + "Rank: " + ChatColor.RESET + ZPM.getPRP(e.getPlayer()).rankName);
        rank.setScore(12);*/
        Team rank = board.registerNewTeam("rank");
        rank.addEntry(ChatColor.GREEN.toString());
        rank.setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Rank: " + ChatColor.of("#73dfff") + ZPM.getPRP(p).rankName);
        obj.getScore(ChatColor.GREEN.toString()).setScore(12);

        /*Score name = obj.getScore("• " + ChatColor.of("#73dfff") + "Name: " + ChatColor.RESET + e.getPlayer().getName());
        name.setScore(13);*/
        Team name = board.registerNewTeam("name");
        name.addEntry(ChatColor.GOLD.toString());
        name.setPrefix(ChatColor.of("#73dfff") + "• " + ChatColor.of("#aceafc") + "Name: " + ChatColor.of("#73dfff") + p.getName());
        obj.getScore(ChatColor.GOLD.toString()).setScore(13);

        /*Score player = obj.getScore(ChatColor.of("#73dfff") + "ᴘʟᴀʏᴇʀ");
        player.setScore(14);*/
        Team player = board.registerNewTeam("player");
        player.addEntry(ChatColor.ITALIC.toString());
        player.setPrefix(ChatColor.of("#73dfff") + "ᴘʟᴀʏᴇʀ");
        obj.getScore(ChatColor.ITALIC.toString()).setScore(14);

        Score space3 = obj.getScore("   ");
        space3.setScore(15);

        p.setScoreboard(board);

    }

}

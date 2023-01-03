package veo.essentials.zpm.stats;

import veo.Main;

public class StatsManager {

    public static void init() {

        Main.getInstance().getCommand("stats").setExecutor(new StatsCommand());

    }

    public static String getFormattedStats(int kills, int killStreak, int deaths, int flagsClaimed, int timePlayed) {

        int hours = (int) Math.floor((double) timePlayed / 60.0), mins = timePlayed % 60;
        return "\tKills: " + kills + "\n\tKill streak: " + killStreak + "\n\tDeaths: " + deaths + "\n\tFlags claimed: " + flagsClaimed + "\n\tTime played: " + hours + ":" + mins + "\n";

    }

}

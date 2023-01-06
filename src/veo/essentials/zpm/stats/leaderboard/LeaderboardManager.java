package veo.essentials.zpm.stats.leaderboard;

import veo.Main;
import veo.essentials.zfm.ZFile;
import veo.essentials.zpm.stats.leaderboard.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {

    List<Leaderboard> leaderboards = new ArrayList<>();

    public static void init() {

        ZFile lbsFiles = new ZFile(Main.mainFolder.getAbsolutePath() + "/leaderboards.zra");
        for (String l : lbsFiles.lines) {

            // x@y@z@numberOfPositions@isStatic@optionalStaticPosition
            String[] sl = l.split("@");
            new Leaderboard(Double.parseDouble(sl[0]), Double.parseDouble(sl[1]), Double.parseDouble(sl[2]), Integer.parseInt(sl[3]), Boolean.parseBoolean(sl[4]), sl[5]);

        }

    }

}

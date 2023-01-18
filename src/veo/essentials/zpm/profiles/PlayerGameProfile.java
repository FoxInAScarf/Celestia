package veo.essentials.zpm.profiles;

import org.bukkit.entity.Player;
import veo.essentials.zpm.Profile;
import veo.essentials.zpm.stats.Listeners;

import java.io.File;

public class PlayerGameProfile extends Profile {

    public int kills = 0, deaths = 0, killStreak = 0, timePlayed = 0, flagsClaimed = 0;

    public PlayerGameProfile(File f) {

        super(f);
        for (String l : this.lines) {

            String key = l.split("=")[0];
            int value = Integer.parseInt(l.split("=")[1]);

            switch (key) {

                case "kills" -> kills = value;
                case "deaths" -> deaths = value;
                case "killStreak" -> killStreak = value;
                case "timePlayed" -> timePlayed = value;
                case "flagsClaimed" -> flagsClaimed = value;

            }

        }

    }

    public PlayerGameProfile reset() {

        //Listeners.onUpdate(this);
        lines.clear();

        clear();
        save();
        addLine("kills=0");
        addLine("deaths=0");
        addLine("killStreak=0");
        addLine("timePlayed=0");
        addLine("flagsClaimed=0");
        save();

        return this;

    }

    public void saveF() {


        Listeners.onUpdate(this);
        lines.clear();

        clear();
        save();
        addLine("kills=" + kills);
        addLine("deaths=" + deaths);
        addLine("killStreak=" + killStreak);
        addLine("timePlayed=" + timePlayed);
        addLine("flagsClaimed=" + flagsClaimed);
        save();

    }

    public void displayScoreboard() {

        Player player = p.getPlayer();

    }

}

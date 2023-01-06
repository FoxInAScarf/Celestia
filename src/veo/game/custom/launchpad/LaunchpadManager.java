package veo.game.custom.launchpad;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.util.ArrayList;
import java.util.List;

public class LaunchpadManager implements Listener {

    static List<Launchpad> launchpads = new ArrayList<>();
    static boolean enabled = true;
    static ZFile file;

    public static void init() {

        file = new ZFile(Main.mainFolder.getAbsolutePath() + "/launchpads.zra");
        for (String s : file.lines) {

            String[] ss = s.split("@");
            launchpads.add(new Launchpad(

                    ss[0],
                    new Location(Bukkit.getWorld(ss[1]), Double.parseDouble(ss[2]), Double.parseDouble(ss[3]), Double.parseDouble(ss[4])),
                    new Vector(Double.parseDouble(ss[5]), Double.parseDouble(ss[6]), Double.parseDouble(ss[7]))

            ).build());

        }

        Bukkit.getPluginManager().registerEvents(new OnMove(), Main.getInstance());
        Main.getInstance().getCommand("launchpad").setExecutor(new LaunchpadCommand());

    }

    public static class OnMove implements Listener {

        @EventHandler
        public void onMove(PlayerMoveEvent e) { if (enabled) for (Launchpad pad : launchpads) pad.yeet(e.getPlayer()); }

    }

}
package veo.essentials.zpm;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zpm.profiles.PlayerChatProfile;
import veo.essentials.zpm.profiles.PlayerGameProfile;
import veo.essentials.zpm.profiles.PlayerRankProfile;
import veo.essentials.zpm.profiles.PlayerSocialProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZPM implements Listener {

    public static String pcpFolder, prpFolder, pspFolder, pgpFolder;

    public static List<PlayerChatProfile> pcp = new ArrayList<>();
    public static List<PlayerRankProfile> prp = new ArrayList<>();
    public static List<PlayerSocialProfile> psp = new ArrayList<>();
    public static List<PlayerGameProfile> pgp = new ArrayList<>();

    private static String folder;

    public static void init(JavaPlugin main) {

        folder = Main.mainFolder.getAbsolutePath() + "/ZPM";
        if (!new File(folder).exists()) new File(folder).mkdir();

        initChatProfile();
        initRankProfile();
        initSocialProfile();
        initGameProfile();

        //Bukkit.getPluginManager().registerEvents(this, main);

    }

    private static void initChatProfile() {

        pcpFolder = new File(folder + "/playerChatProfiles").getAbsolutePath();
        File ff = new File(pcpFolder);
        if (!new File(pcpFolder).exists()) new File(pcpFolder).mkdir();

        for (File f : Objects.requireNonNull(ff.listFiles()))
            pcp.add(new PlayerChatProfile(f));

    }

    private static void initRankProfile() {

        prpFolder = new File(folder + "/playerRankProfiles").getAbsolutePath();
        File ff = new File(prpFolder);
        if (!new File(prpFolder).exists()) new File(prpFolder).mkdir();

        for (File f : Objects.requireNonNull(ff.listFiles()))
            prp.add(new PlayerRankProfile(f));

    }

    private static void initSocialProfile() {

        pspFolder = new File(folder + "/playerSocialProfiles").getAbsolutePath();
        File ff = new File(pspFolder);
        if (!new File(pspFolder).exists()) new File(pspFolder).mkdir();

        for (File f : Objects.requireNonNull(ff.listFiles()))
            psp.add(new PlayerSocialProfile(f));

    }

    private static void initGameProfile() {

        pgpFolder = new File(folder + "/playerGameProfiles").getAbsolutePath();
        File ff = new File(pgpFolder);
        if (!new File(pgpFolder).exists()) new File(pgpFolder).mkdir();

        for (File f : Objects.requireNonNull(ff.listFiles()))
            pgp.add(new PlayerGameProfile(f));

    }

    /*@EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (!getProfiledPlayers().contains(p)) {

            File f = new File(pcpFolder + "/" + p.getUniqueId());
            prp.add(new PlayerRankProfile(f).reset());

        }

    }*/

    public static List<Player> getProfiledPlayers() {

        List<Player> profiledPlayers = new ArrayList<>();
        {

            for (PlayerChatProfile pr : pcp)
                if (!profiledPlayers.contains(pr.getPlayer()))
                    profiledPlayers.add(pr.getPlayer());
            for (PlayerRankProfile pr : prp)
                if (!profiledPlayers.contains(pr.getPlayer()))
                    profiledPlayers.add(pr.getPlayer());
            for (PlayerSocialProfile pr : psp)
                if (!profiledPlayers.contains(pr.getPlayer()))
                    profiledPlayers.add(pr.getPlayer());
            for (PlayerGameProfile pr : pgp)
                if (!profiledPlayers.contains(pr.getPlayer()))
                    profiledPlayers.add(pr.getPlayer());

        }

        return profiledPlayers;

    }

    public static PlayerChatProfile getPCP(Player p) {

        for (PlayerChatProfile pr : pcp)
            if (pr.getPlayer().equals(p)) return pr;
        return null;

    }

    public static PlayerRankProfile getPRP(Player p) {

        for (PlayerRankProfile pr : prp)
            if (pr.getPlayer().equals(p)) return pr;
        return null;

    }

    public static PlayerSocialProfile getPSP(Player p) {

        for (PlayerSocialProfile pr : psp)
            if (pr.getPlayer().equals(p)) return pr;
        return null;

    }

    public static PlayerGameProfile getPGP(Player p) {

        for (PlayerGameProfile pr : pgp)
            if (pr.getPlayer().equals(p)) return pr;
        return null;

    }

}
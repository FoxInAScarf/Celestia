package veo.essentials.zpm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zpm.profiles.*;
import veo.essentials.zpm.stats.StatsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ZPM {

    public static String pcpFolder, prpFolder, pspFolder, pgpFolder;

    public static List<PlayerChatProfile> pcp = new ArrayList<>();
    public static List<PlayerRankProfile> prp = new ArrayList<>();
    public static List<PlayerSocialProfile> psp = new ArrayList<>();
    public static List<PlayerGameProfile> pgp = new ArrayList<>();

    private static String folder;

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/ZPM";
        if (!new File(folder).exists()) new File(folder).mkdir();

        initChatProfile();
        initRankProfile();
        initSocialProfile();
        initGameProfile();

        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
        Main.getInstance().getCommand("troubleshoot").setExecutor(new TroubleShootCommand());

        StatsManager.init();

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

        PGPManager.initTime();

    }

    /*@EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (!getProfiledPlayers().contains(p)) {

            File f = new File(pcpFolder + "/" + p.getUniqueId());
            prp.add(new PlayerRankProfile(f).reset());

        }

    }*/

    public static List<String> getProfiledPlayers() {

        List<String> profiledPlayers = new ArrayList<>();
        {

            for (PlayerChatProfile pr : pcp)
                if (!profiledPlayers.contains(pr.getUUID().toString()))
                    profiledPlayers.add(pr.getUUID().toString());
            for (PlayerRankProfile pr : prp)
                if (!profiledPlayers.contains(pr.getUUID().toString()))
                    profiledPlayers.add(pr.getUUID().toString());
            for (PlayerSocialProfile pr : psp)
                if (!profiledPlayers.contains(pr.getUUID().toString()))
                    profiledPlayers.add(pr.getUUID().toString());
            for (PlayerGameProfile pr : pgp)
                if (!profiledPlayers.contains(pr.getUUID().toString()))
                    profiledPlayers.add(pr.getUUID().toString());

        }

        return profiledPlayers;

    }

    public static PlayerChatProfile getPCP(Player p) {

        for (PlayerChatProfile pr : pcp)
            if (pr.getPlayer().getUniqueId().equals(p.getUniqueId())) return pr;
        return null;

    }

    public static PlayerRankProfile getPRP(Player p) {

        for (PlayerRankProfile pr : prp)
            if (pr.getPlayer().getUniqueId().equals(p.getUniqueId())) return pr;
        return null;

    }

    public static PlayerSocialProfile getPSP(Player p) {

        for (PlayerSocialProfile pr : psp)
            if (pr.getPlayer().getUniqueId().equals(p.getUniqueId())) return pr;
        return null;

    }

    public static PlayerGameProfile getPGP(Player p) {

        for (PlayerGameProfile pr : pgp)
            if (pr.getPlayer().getUniqueId().equals(p.getUniqueId())) return pr;
        return null;

    }

    public static PlayerGameProfile getPGPfromUUID(UUID uuid) {

        for (PlayerGameProfile pr : pgp)
            if (pr.p.getUniqueId().equals(uuid)) return pr;
        return null;

    }

}

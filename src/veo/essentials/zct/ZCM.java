package veo.essentials.zct;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZCM {

    static ZFile bw;
    static List<String> blockedwords = new ArrayList<>();

    public static void init() {

        String folder = Main.mainFolder.getAbsolutePath() + "/ZCM";
        if (!new File(folder).exists()) new File(folder).mkdir();

        bw = new ZFile(folder + "/blockedWords.zra");
        for (String l : bw.lines)
            blockedwords.addAll(Arrays.asList(l.split(";")));

        Main.getInstance().getCommand("chat").setExecutor(new ZCMCommands.ChatCommand());
        Main.getInstance().getCommand("block").setExecutor(new ZCMCommands.BlockCommand());
        Main.getInstance().getCommand("unblock").setExecutor(new ZCMCommands.UnblockCommand());

        Bukkit.getPluginManager().registerEvents(new ZCMListeners(), Main.getInstance());

    }

}

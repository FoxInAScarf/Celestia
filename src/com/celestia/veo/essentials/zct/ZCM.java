package com.celestia.veo.essentials.zct;

import com.celestia.veo.Main;
import com.celestia.veo.essentials.zfm.ZFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZCM {

    static ZFile bw;
    static List<String> blockedwords = new ArrayList<>();

    public static void init(JavaPlugin main) {

        String folder = Main.mainFolder.getAbsolutePath() + "/ZCM";
        if (!new File(folder).exists()) new File(folder).mkdir();

        bw = new ZFile(folder + "/blockedWords.zra");
        for (String l : bw.lines)
            blockedwords.addAll(Arrays.asList(l.split(";")));

        main.getCommand("chat").setExecutor(new ZCMCommands.ChatCommand());
        main.getCommand("block").setExecutor(new ZCMCommands.BlockCommand());
        main.getCommand("unblock").setExecutor(new ZCMCommands.UnblockCommand());

        Bukkit.getPluginManager().registerEvents(new ZCMListeners(), main);

    }

}

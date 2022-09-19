package com.celestia.veo;

import com.celestia.veo.essentials.zct.ZCM;
import com.celestia.veo.essentials.zpm.ZPM;
import com.celestia.veo.essentials.zwp.ZWP;
import com.celestia.veo.game.custom.gens.GenManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public boolean running = true;
    public static File mainFolder;
    public static final String name = "Celestia";

    public void onEnable() {

        /*
        *
        * Display cool looking thing
        *
        * */

        mainFolder = new File(this.getDataFolder().getParentFile().getAbsolutePath()
                + "/" + name);
        if (!mainFolder.exists()) mainFolder.mkdir();

        ZPM.init(this);
        ZWP.init(this);
        ZCM.init(this);
        GenManager.init(this);

    }

    public static void sendMessage(Player p, String msg, boolean isError) {

        String prefix = "";
        if (isError) {

            if (ZPM.getPCP(p).showChatPrefix)
                prefix = ChatColor.RED + "● ";
            p.sendMessage(prefix + ChatColor.RESET + msg);
            return;

        }
        if (ZPM.getPCP(p).showChatPrefix)
            prefix = ChatColor.GREEN + "● ";
        p.sendMessage(prefix + ChatColor.RESET + msg);

    }

}

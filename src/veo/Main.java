package veo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import veo.essentials.zct.ZCM;
import veo.essentials.zfm.ZFile;
import veo.essentials.zpm.ZPM;
import veo.essentials.zwp.ZWP;
import veo.game.gens.GenManager;
import veo.game.items.ZItemManager;
import veo.game.npcs.NPCManager;
import veo.game.shop.ShopManager;

import java.io.File;

public class Main extends JavaPlugin {

    public boolean running = true;
    public static File mainFolder;
    public static final String name = "Celestia";
    private static Main main;

    public void onEnable() {

        /*
        *
        * Display cool looking thing
        *
        * */

        main = this;

        mainFolder = new File(this.getDataFolder().getParentFile().getAbsolutePath()
                + "/" + name);
        if (!mainFolder.exists()) mainFolder.mkdir();

        for (World w : Bukkit.getWorlds()) for (Entity e : w.getEntities())
            if (e.getScoreboardTags().contains("removable")) e.remove();

        ZItemManager.init();
        ZPM.init(this);
        ZWP.init(this);
        ZCM.init(this);
        GenManager.init(this);
        ShopManager.init();
        NPCManager.init();

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

    public static Main getInstance() { return main; }

}

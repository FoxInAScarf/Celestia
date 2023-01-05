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
import veo.game.gens.flag.Flag;
import veo.game.gens.flag.FlagManager;
import veo.game.items.ZItemManager;
import veo.game.npcs.NPCManager;
import veo.game.shop.ShopManager;

import java.io.File;
import java.util.List;

public class Main extends JavaPlugin {

    public static File mainFolder;
    public static final String name = "Celestia";
    private static Main main;
    public static String removableTag;

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
        ZFile removableTags = new ZFile(mainFolder.getAbsolutePath() + "/removableTags.zra");

        removableTag = createRemovableTag(removableTags.lines);

        if (!removableTags.lines.isEmpty())
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {

                for (String tag : removableTags.lines) {

                    if (tag.equals(removableTag)) continue;
                    for (World w : Bukkit.getWorlds()) for (Entity e : w.getEntities())
                        if (e.getScoreboardTags().contains("removable-" + tag)) e.remove();

                }

            }, 0L, 20L);

        ZItemManager.init();
        ZPM.init();
        ZWP.init();
        ZCM.init();
        GenManager.init();
        ShopManager.init();
        NPCManager.init();

        removableTags.addLine(removableTag);

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

    private static String createRemovableTag(List<String> existingTags) {

        String tag = Math.round(Math.random() * 1000000000) + "";
        if (existingTags.contains(tag)) return createRemovableTag(existingTags);
        return tag;

    }

}

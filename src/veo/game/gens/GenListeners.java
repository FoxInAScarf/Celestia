package veo.game.gens;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import veo.Main;

public class GenListeners implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null)
            for (Flag f : GenManager.flags)
                if (f.head.equals(e.getClickedBlock().getLocation())) {

                    if (f.owner == null) f.claim(e.getPlayer());
                    else {

                        if (f.owner.equals(e.getPlayer())) {

                            Main.sendMessage(f.owner.getPlayer(), ChatColor.RED + "You've already" +
                                    " claimed this island. Try claiming another one!", true);
                            return;

                        }

                        Main.sendMessage(e.getPlayer(), ChatColor.RED + "This island has been " +
                                "already claimed.", true);
                        Main.sendMessage(e.getPlayer(), ChatColor.GREEN + "Unclaim it by crouching for " +
                                ChatColor.YELLOW + "10" + ChatColor.GREEN + " seconds by it.", true);

                    }
                    break;

                }

    }

}

package veo.game.gens;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GenListeners implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null)
            for (Flag f : GenManager.flags)
                if (f.head.equals(e.getClickedBlock().getLocation())) {

                    f.claim(e.getPlayer());
                    break;

                }

    }

}

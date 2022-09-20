package com.celestia.veo.game.custom.gens;

import com.celestia.veo.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GenListeners implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        e.getPlayer().sendMessage("bruh");

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null)
            for (Flag f : GenManager.flags)
                if (f.head.equals(e.getClickedBlock().getLocation())) {

                    f.claim(e.getPlayer());
                    break;

                }

    }

}

package veo.game.npcs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        for (NPC n : NPCManager.npcs) if (n.l.getChunk().isLoaded()) n.createTo(e.getPlayer());

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        for (NPC n : NPCManager.npcs) if (n.l.getChunk().equals(e.getChunk()))
            for (Player p : Bukkit.getOnlinePlayers()) n.createTo(p);

    }

}

package veo.essentials.zpm.profiles;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import veo.essentials.zpm.ZPM;

import java.io.File;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        if (!ZPM.getProfiledPlayers().contains(p.getUniqueId().toString())) {

            File f1 = new File(ZPM.prpFolder + "/" + p.getUniqueId());
            ZPM.prp.add(new PlayerRankProfile(f1).reset());

            File f2 = new File(ZPM.pcpFolder + "/" + p.getUniqueId());
            ZPM.pcp.add(new PlayerChatProfile(f2).reset());

            File f3 = new File(ZPM.pgpFolder + "/" + p.getUniqueId());
            ZPM.pgp.add(new PlayerGameProfile(f3).reset());

        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        Player p = e.getPlayer();
        ZPM.getPGP(p).saveF();

    }

}

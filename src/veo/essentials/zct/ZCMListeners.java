package veo.essentials.zct;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import veo.Main;
import veo.essentials.zpm.ZPM;
import veo.essentials.zpm.profiles.PlayerChatProfile;
import veo.essentials.zpm.profiles.PlayerRankProfile;

import java.io.File;
import java.util.UUID;

public class ZCMListeners implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent e) {

        Player sender = e.getPlayer();
        System.out.println(sender.getName() + ": " + e.getMessage()); // for logging purposes

        if (!ZPM.getPCP(sender).isChatEnabled) {

            Main.sendMessage(sender, ChatColor.RED +
                    "Chat is disabled! Do '/chat toggle' to re-enable it!", true);
            e.setCancelled(true);
            return;

        }

        for (Player receiver : Bukkit.getOnlinePlayers()) {

            String message = e.getMessage(),
                    rank = ZPM.getPRP(sender).rankName;
            ChatColor mColor = ZPM.getPRP(sender).textColor;
            String name = mColor + sender.getName();

            /*if (sender.isOp()) {

                rank = ChatColor.RED + "" + ChatColor.BOLD + "ADMIN";
                name = ChatColor.RED + sender.getName();

            }*/



            message = message.replaceAll("&", "§");
            if (receiver.equals(sender)) {

                String outPrefix = "";
                if (ZPM.getPCP(sender).showChatPrefix)
                    outPrefix = ChatColor.RED + "← ";

                message = outPrefix + rank + ChatColor.DARK_GRAY + "" + ChatColor.BOLD
                        + " | " + ChatColor.RESET + name + ChatColor.RESET + "" + ChatColor.GRAY
                        + " -> " + mColor + message;
                receiver.sendMessage(message);
                continue;

            }

            if (e.getMessage().contains(receiver.getName())) {

                message = message.replaceAll(receiver.getName(), ChatColor.AQUA + "@"
                        + receiver.getName() + mColor);

                if (!ZPM.getPCP(receiver).blockedPlayers.contains(sender))
                receiver.playSound(receiver.getLocation(),
                        Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

            }

            if (ZPM.getPCP(receiver).isFilterEnabled)
                for (String w : ZCM.blockedwords) {

                    String starW = "";
                    for (int i = 0; i <= w.length() - 1; i++) starW += "*";
                    message = message.replaceAll(w, starW);

                }

            String inPrefix = "";
            if (ZPM.getPCP(receiver).showChatPrefix)
                inPrefix = ChatColor.GREEN + "→ ";
            message = inPrefix + rank + ChatColor.DARK_GRAY + "" + ChatColor.BOLD
                + " | " + ChatColor.RESET + name + ChatColor.RESET + "" + ChatColor.GRAY
                    + " -> " + mColor + message;

            if (ZPM.getPCP(receiver).isChatEnabled)
                if (ZPM.getPCP(receiver).blockedPlayers.contains(sender)) {

                    //message = inPrefix + ChatColor.RED + "Message blocked!";
                    receiver.sendMessage(message);

                } else receiver.sendMessage(message);

        }
        e.setCancelled(true);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        if (!ZPM.getProfiledPlayers().contains(p.getUniqueId().toString())) {

            File f1 = new File(ZPM.prpFolder + "/" + p.getUniqueId());
            ZPM.prp.add(new PlayerRankProfile(f1).reset());

            File f2 = new File(ZPM.pcpFolder + "/" + p.getUniqueId());
            ZPM.pcp.add(new PlayerChatProfile(f2).reset());

        }

        ChatColor c = ZPM.getPRP(p).textColor;
        e.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] "
            + c + p.getName());

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        ChatColor c = ChatColor.GRAY;
        if (e.getPlayer().isOp()) c = ChatColor.RED;

        e.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] "
                + c + e.getPlayer().getName());

    }

}

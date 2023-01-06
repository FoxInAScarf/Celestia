package veo.game.custom.holograms;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import veo.Main;

public class HologramCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player p)) return false;
        if (!p.isOp()) return false;

        switch (args[0]) {

            case "list":

                Main.sendMessage(p, ChatColor.GREEN + "List of all holograms: ", false);
                for (Hologram h : HologramManager.holograms) p.sendMessage(ChatColor.GREEN + h.name);
                break;

            case "delete":

                String name = args[1];
                Hologram holo = null;
                for (Hologram h : HologramManager.holograms) if (h.name.equals(name)) holo = h;
                if (holo == null) {

                    Main.sendMessage(p, ChatColor.RED + "There is no hologram called '" + name + "'!", true);
                    return false;

                }
                for (ArmorStand stand : holo.armorStands) stand.remove();
                break;

            case "lines":

                switch (args[1]) {

                    case "add":
                        break;

                    case "remove":
                        break;

                    case "list":

                        String name1 = args[1];
                        Hologram holo1 = null;
                        for (Hologram h : HologramManager.holograms) if (h.name.equals(name1)) holo1 = h;
                        if (holo1 == null) {

                            Main.sendMessage(p, ChatColor.RED + "There is no hologram called '" + name1 + "'!", true);
                            return false;

                        }

                        Main.sendMessage(p, ChatColor.GREEN + "List of all lines for '" + name1 + "'!", false);
                        for (ArmorStand stand : holo1.armorStands) p.sendMessage(stand.getCustomName());
                        break;

            }
            break;

            case "reload":

                HologramManager.load();
                Main.sendMessage(p, ChatColor.GREEN + "Successfully reloaded all holograms!", false);
                break;

        }

        return false;

    }

}

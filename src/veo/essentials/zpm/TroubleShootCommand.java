package veo.essentials.zpm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;

public class TroubleShootCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player p)) return false;
        if (!p.isOp()) return false;

        if (args.length < 2) return false;
        switch (args[0]) {

            case "uuid":

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {

                    Main.sendMessage(p, ChatColor.RED + "That player doesn't exist!", true);
                    return false;

                }
                Main.sendMessage(p, ChatColor.GREEN + "UUID of " + args[1] + " is: " + ChatColor.UNDERLINE + "" + ChatColor.YELLOW + target.getUniqueId().toString(), false);

                break;

                /*
                *
                * add more stuff later zraphyyyy
                *
                * */

        }

        return false;

    }

}

package veo.game.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import veo.Main;

public class ZItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player)) {

            System.out.println("[" + Main.name.toUpperCase()
                    + "-ERROR]: Only players can run this command!");
            return false;

        }
        Player p = (Player) s;

        if (!p.isOp()) {

            Main.sendMessage(p, ChatColor.RED + "You're not authorised to run this command!",
                    true);
            return false;

        }

        switch (args[0]) {

            case "list":

                Main.sendMessage(p, ChatColor.GREEN + "These are the available custom items:", false);
                for (ZItem i : ZItemManager.items)
                    Main.sendMessage(p, ChatColor.GREEN + "      - " + i.name, false);
                break;

            case "give":

                //        0    1      2
                // /zitem give Zraphy enchanted_apple_thing
                if (args.length == 1) {

                    Main.sendMessage(p, ChatColor.RED + "Please specify a player.", true);
                    return false;

                }
                Player to = Bukkit.getPlayer(args[1]);
                if (to == null) {

                    Main.sendMessage(p, ChatColor.RED + args[1] + " is not online.", true);
                    return false;

                }
                if (args.length < 3) {

                    Main.sendMessage(p, ChatColor.RED + "Please specify a custom item.", true);
                    return false;

                }
                ItemStack item = ZItemManager.getItem(args[2]);
                if (item == null) {

                    Main.sendMessage(p, ChatColor.RED + "The item '" + args[2] + "' doesn't exist!", true);
                    return false;

                }
                to.getInventory().addItem(item);
                Main.sendMessage(p, ChatColor.GREEN + "Item successfully given to " + args[1] + ".", false);
                break;

            case "reload":

                Main.sendMessage(p, ChatColor.YELLOW + "Reloading custom items...", false);
                ZItemManager.load();
                Main.sendMessage(p, ChatColor.GREEN + "DONE!", false);
                break;

            default:

                Main.sendMessage(p, ChatColor.YELLOW + "There is no such argument as '"
                        + ChatColor.RED + args[0] + ChatColor.YELLOW + "'!", true);
                break;

        }

        return false;

    }

}

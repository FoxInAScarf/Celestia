package veo.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;

public class ShopCommand implements CommandExecutor {

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

                Main.sendMessage(p, ChatColor.GREEN + "These are the available shops:", false);
                for (Shop i : ShopManager.shops)
                    Main.sendMessage(p, ChatColor.GREEN + "      - " + i.name, false);
                break;

            case "show":

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

                    Main.sendMessage(p, ChatColor.RED + "Please specify a shop.", true);
                    return false;

                }
                Shop shop = ShopManager.getShop(args[2]);
                if (shop == null) {

                    Main.sendMessage(p, ChatColor.RED + "The shop '" + args[2] + "' doesn't exist!", true);
                    return false;

                }
                ShopManager.addInstance(p, shop);
                ShopManager.instances.get(ShopManager.instances.size() - 1).display();
                Main.sendMessage(p, ChatColor.GREEN + "Shop successfully displayed to " + args[1] + ".", false);
                break;

            case "reload":

                Main.sendMessage(p, ChatColor.YELLOW + "Reloading shops...", false);
                ShopManager.load();
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
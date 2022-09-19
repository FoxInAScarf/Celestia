package com.celestia.veo.game.custom.gens;

import com.celestia.veo.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenCommand implements CommandExecutor {

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

            case "add":
                addGen(args, p);
                break;

            case "remove":
                removeGen(args, p);
                break;

            case "list":
                break;

            case "flag":

                switch (args[1]) {

                    case "add":
                        break;

                    case "remove":
                        break;

                    default:

                        Main.sendMessage(p, ChatColor.YELLOW + "There is no such sub-argument as '"
                                + ChatColor.RED + args[0] + ChatColor.YELLOW + "'!", true);
                        break;

                }

                break;

            default:

                Main.sendMessage(p, ChatColor.YELLOW + "There is no such argument as '"
                        + ChatColor.RED + args[0] + ChatColor.YELLOW + "'!", true);
                break;

        }

        return false;

    }

    public void addGen(String[] args, Player p) {

        // /gen add joeMama69 redstone_ore ~ ~ ~ 100
        String name = args[1];
        if (GenManager.getFlag(name) != null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's already a generator named '"
                    + ChatColor.RED + name + ChatColor.YELLOW + "'!", true);
            return;

        }

        Material m = Material.getMaterial(args[2]);
        if (m == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's no such material as '"
                    + ChatColor.RED + m.getName() + ChatColor.YELLOW + "'!", true);
            return;

        }

    }

    public void removeGen(String[] args, Player p) {

        if (GenManager.getGen(args[1]) == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There is no generator named '"
                    + ChatColor.RED + args[1] + ChatColor.YELLOW + "'!", true);
            return;

        }

                /*if (args.length < 2) {

                    Main.sendMessage(p, ChatColor.YELLOW + "Missing argument! Please specify the "
                        + "name of the generator to be removed.", true);
                    break;

                }*/

        GenManager.getGen(args[1]).remove();
        Main.sendMessage(p, ChatColor.GREEN + "Removed generator!", false);

    }

    public void addFlag(String[] args) {



    }

    public void removeFlag(String[] args, Player p) {

        if (GenManager.getFlag(args[2]) == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There is no flag named '"
                    + ChatColor.RED + args[2] + ChatColor.YELLOW + "'!", true);
            return;

        }

                /*if (args.length < 2) {

                    Main.sendMessage(p, ChatColor.YELLOW + "Missing argument! Please specify the "
                        + "name of the generator to be removed.", true);
                    break;

                }*/

        GenManager.getFlag(args[2]).remove();
        Main.sendMessage(p, ChatColor.GREEN + "Removed flag!", false);

    }

}

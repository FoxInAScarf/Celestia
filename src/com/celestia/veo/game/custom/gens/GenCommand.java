package com.celestia.veo.game.custom.gens;

import com.celestia.veo.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        if (GenManager.getGen(name) != null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's already a generator named '"
                    + ChatColor.RED + name + ChatColor.YELLOW + "'!", true);
            return;

        }

        Material m = Material.getMaterial(args[2]);
        if (m == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's no such material as '"
                    + ChatColor.RED + args[2] + ChatColor.YELLOW + "'!", true);
            return;

        }

        double x, y, z;
        if (args[3].equals("~")) x = p.getLocation().getX();
        else x = Double.parseDouble(args[3]);
        if (args[4].equals("~")) y = p.getLocation().getY();
        else y = Double.parseDouble(args[4]);
        if (args[5].equals("~")) z = p.getLocation().getZ();
        else z = Double.parseDouble(args[5]);

        int time = Integer.parseInt(args[6]);
        GenManager.gens.add(new Generator(name, m, new Location(p.getWorld(), x, y, z), time));
        GenManager.genFile.addLine(name + "@" + p.getWorld().getName() + "@" + x + "@" +
                y + "@" + z + "@" + time);

        // joemama69@world@1@1@1@100
        Main.sendMessage(p, ChatColor.GREEN + "Added generator!", false);

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

    public void addFlag(String[] args, Player p) {

        String name = args[1];
        if (GenManager.getGen(name) != null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's already a flag attached to " +
                    "the generator named '"
                    + ChatColor.RED + name + ChatColor.YELLOW + "'!", true);
            return;

        }

        double hx, hy, hz;
        if (args[2].equals("~")) hx = p.getLocation().getX();
        else hx = Double.parseDouble(args[2]);
        if (args[3].equals("~")) hy = p.getLocation().getY();
        else hy = Double.parseDouble(args[3]);
        if (args[4].equals("~")) hz = p.getLocation().getZ();
        else hz = Double.parseDouble(args[4]);

        Location head = new Location(p.getWorld(), hx, hy, hz);

        double px, py, pz;
        if (args[5].equals("~")) px = p.getLocation().getX();
        else px = Double.parseDouble(args[5]);
        if (args[6].equals("~")) py = p.getLocation().getY();
        else py = Double.parseDouble(args[6]);
        if (args[7].equals("~")) pz = p.getLocation().getZ();
        else pz = Double.parseDouble(args[7]);

        Location pole = new Location(p.getWorld(), px, py, pz);

        GenManager.flags.add(new Flag(name, head, pole));
        GenManager.flagFile.addLine(name + "@" + p.getWorld() + "@" + hx + "@" + hy + "@" + hz + "@"
                + px + "@" + py + "@" + pz + "@");
        Main.sendMessage(p, ChatColor.GREEN + "Added flag!", false);

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

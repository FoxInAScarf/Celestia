package veo.game.gens;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;
import veo.game.gens.flag.Flag;

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

            case "toggle":

                GenManager.running = !GenManager.running;

                Main.sendMessage(p, ChatColor.GREEN + "Generators have been turned " +
                        (GenManager.running ? "on" : "off") + "!", false);
                break;

            case "add":
                addGen(args, p);
                break;

            case "remove":
                removeGen(args, p);
                break;

            case "list":

                Main.sendMessage(p, ChatColor.GREEN + "These are the currently existing generators:", false);
                for (Generator g : GenManager.gens) {

                    ChatColor cc = GenManager.getFlag(g.name) != null ? ChatColor.GREEN : ChatColor.YELLOW;
                    Main.sendMessage(p, ChatColor.GREEN + "      - " + cc + g.name + ChatColor.GREEN + ":" +
                            " " + g.m.toString().replaceAll("_", "") + " generator",
                            false);

                }
                break;

            case "flag":

                switch (args[1]) {

                    case "add":
                        addFlag(args, p);
                        break;

                    case "remove":
                        removeFlag(args, p);
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

        Material m = Material.getMaterial(args[2].toUpperCase());
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
        GenManager.genFile.addLine(name + "@" + m.toString() + "@" + p.getWorld().getName() + "@"
                + x + "@" + y + "@" + z + "@" + time);

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
        GenManager.getFlag(args[1]).remove();
        Main.sendMessage(p, ChatColor.GREEN + "Removed generator and it's flag!", false);

    }

    public void addFlag(String[] args, Player p) {

        String name = args[2];
        if (GenManager.getFlag(name) != null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's already a flag attached to " +
                    "the generator named '"
                    + ChatColor.RED + name + ChatColor.YELLOW + "'!", true);
            return;

        }

        if (GenManager.getGen(name) == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There's no generator named "
                    + ChatColor.RED + name + ChatColor.YELLOW + "'! " +
                    "You can't attach a flag to a generator which doesn't exist.", true);
            return;

        }

        double hx, hy, hz;
        if (args[3].equals("~")) hx = p.getLocation().getX();
        else hx = Double.parseDouble(args[3]);
        if (args[4].equals("~")) hy = p.getLocation().getY();
        else hy = Double.parseDouble(args[4]);
        if (args[5].equals("~")) hz = p.getLocation().getZ();
        else hz = Double.parseDouble(args[5]);

        Location head = new Location(p.getWorld(), hx, hy, hz);

        double px, py, pz;
        if (args[6].equals("~")) px = p.getLocation().getX();
        else px = Double.parseDouble(args[6]);
        if (args[7].equals("~")) py = p.getLocation().getY();
        else py = Double.parseDouble(args[7]);
        if (args[8].equals("~")) pz = p.getLocation().getZ();
        else pz = Double.parseDouble(args[8]);

        Location pole = new Location(p.getWorld(), px, py, pz);

        GenManager.flags.add(new Flag(name, head, pole));
        GenManager.flagFile.addLine(name + "@" + head.getWorld().getName() + "@" + head.getX() +
                "@" + head.getY() + "@" + head.getZ() + "@"
                + pole.getX() + "@" + pole.getY() + "@" + pole.getZ() + "@");

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

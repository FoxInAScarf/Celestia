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
import veo.game.gens.flag.FlagManager;
import veo.game.items.ZItemManager;

import java.util.Iterator;

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

            case "modify":
                modifyGen(args, p);
                break;

            case "clear":

                for (int i = 0; i <= GenManager.gens.size() - 1; i++) GenManager.gens.get(i).remove();
                break;

            case "test":

                GenManager.genFile.clearAll();
                break;

            case "list":

                Main.sendMessage(p, ChatColor.GREEN + "These are the currently existing generators:", false);
                for (Generator g : GenManager.gens) {

                    System.out.println(g.name + "@" + g.m.getType().name() + "@" + g.l.getWorld().getName() + "@"
                            + g.l.getX() + "@" + g.l.getY() + "@" + g.l.getZ() + "@" + g.length + "@" + g.h.toString());
                    ChatColor cc = FlagManager.getFlag(g.name) != null ? ChatColor.GREEN : ChatColor.YELLOW;
                    Main.sendMessage(p, ChatColor.GREEN + "      - " + cc + g.name + ChatColor.GREEN + ":" +
                            " " + g.m.getType().name() + " generator",
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

        //Material m = Material.getMaterial(args[2].toUpperCase());
        if (Material.getMaterial(args[2].toUpperCase()) == null || ZItemManager.getItem(args[2]) == null) {

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

        x = Math.floor(x) + 0.5;
        z = Math.floor(z) + 0.5;

        int time = Integer.parseInt(args[6]);
        Material head = ZItemManager.getItem(args[2]) == null ? Material.getMaterial(args[2].toUpperCase())
                : ZItemManager.getItem(args[2]).getType();

        GenManager.gens.add(new Generator(name, args[2].toUpperCase(), new Location(p.getWorld(), x, y, z), time, head));
        GenManager.genFile.addLine(name + "@" + args[2].toUpperCase() + "@" + p.getWorld().getName() + "@"
                + x + "@" + y + "@" + z + "@" + time + "@" + head);

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
        if (GenManager.getGen(args[1]) != null) FlagManager.getFlag(args[1]).remove();
        Main.sendMessage(p, ChatColor.GREEN + "Removed generator and it's flag!", false);

    }

    public void modifyGen(String[] args, Player p) {

        // /gens modify <name> <drop;item;name;time>
        Generator gen = GenManager.getGen(args[1]);
        if (gen == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There is no generator named '"
                    + ChatColor.RED + args[1] + ChatColor.YELLOW + "'!", true);
            return;

        }
        switch (args[2]) {

            case "drop" -> gen.edit(gen.name, args[3], gen.length, gen.h);
            case "item" -> gen.edit(gen.name, gen.drop, gen.length, Material.getMaterial(args[3].toUpperCase()));
            case "name" -> gen.edit(args[3], gen.drop, gen.length, gen.h);
            case "time" -> gen.edit(gen.name, gen.drop, Integer.parseInt(args[3]), gen.h);

        }
        Main.sendMessage(p, ChatColor.GREEN + "Successfully edited something, IDK.", false);

    }

    public void addFlag(String[] args, Player p) {

        String name = args[2];
        if (FlagManager.getFlag(name) != null) {

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

        hx = Math.floor(hx);
        hz = Math.floor(hz);

        Location head = new Location(p.getWorld(), hx, hy, hz);

        double px, py, pz;
        if (args[6].equals("~")) px = p.getLocation().getX();
        else px = Double.parseDouble(args[6]);
        if (args[7].equals("~")) py = p.getLocation().getY();
        else py = Double.parseDouble(args[7]);
        if (args[8].equals("~")) pz = p.getLocation().getZ();
        else pz = Double.parseDouble(args[8]);

        px = Math.floor(hx);
        pz = Math.floor(hz);

        Location pole = new Location(p.getWorld(), px, py, pz);

        FlagManager.flags.add(new Flag(name, head, pole));
        FlagManager.flagFile.addLine(name + "@" + head.getWorld().getName() + "@" + head.getX() +
                "@" + head.getY() + "@" + head.getZ() + "@"
                + pole.getX() + "@" + pole.getY() + "@" + pole.getZ() + "@");

        Main.sendMessage(p, ChatColor.GREEN + "Added flag!", false);

    }

    public void removeFlag(String[] args, Player p) {

        if (FlagManager.getFlag(args[2]) == null) {

            Main.sendMessage(p, ChatColor.YELLOW + "There is no flag named '"
                    + ChatColor.RED + args[2] + ChatColor.YELLOW + "'!", true);
            return;

        }

                /*if (args.length < 2) {

                    Main.sendMessage(p, ChatColor.YELLOW + "Missing argument! Please specify the "
                        + "name of the generator to be removed.", true);
                    break;

                }*/

        FlagManager.getFlag(args[2]).remove();
        Main.sendMessage(p, ChatColor.GREEN + "Removed flag!", false);

    }

}

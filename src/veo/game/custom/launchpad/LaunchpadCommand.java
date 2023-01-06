package veo.game.custom.launchpad;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import veo.Main;

public class LaunchpadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player p)) {

            System.out.println("Only players can run this command!");
            return false;

        }

        if (!p.isOp()) {

            Main.sendMessage(p, ChatColor.RED + "You're not authorized to run this command.", true);
            return false;

        }

        switch (args[0]) {

            case "toggle":

                LaunchpadManager.enabled = !LaunchpadManager.enabled;
                Main.sendMessage(p, ChatColor.GREEN + "Launchpads have been turned " + (LaunchpadManager.enabled ? "on!" : "off!"), false);
                break;

            case "add":

                if (!add(p, args)) return false;
                break;

            case "remove":

                if (!remove(p, args[1])) return false;
                break;

            case "list":

                Main.sendMessage(p, ChatColor.GREEN + "All launchpads:", false);
                for (Launchpad pad : LaunchpadManager.launchpads) p.sendMessage("    " + pad.name);
                break;

            case "modify":

                if (!modify(p, args)) return false;
                break;

        }

        return false;

    }

    public boolean add(Player p, String[] args) {

        boolean contains = false;
        for (Launchpad pad : LaunchpadManager.launchpads) if (pad.name.equals(args[1])) contains = true;
        if (contains) {

            Main.sendMessage(p, ChatColor.RED + "A pad with the name '" + args[1] + "' already exists!", true);
            return false;

        }

        double x = args[2].equals("~") ? p.getLocation().getX() : Double.parseDouble(args[2]),
                y = args[3].equals("~") ? p.getLocation().getY() : Double.parseDouble(args[3]),
                z = args[2].equals("~") ? p.getLocation().getX() : Double.parseDouble(args[4]);
        LaunchpadManager.launchpads.add(new Launchpad(

                args[1],
                new Location(p.getLocation().getWorld(), x, y, z),
                new Vector(Double.parseDouble(args[5]), Double.parseDouble(args[6]), Double.parseDouble(args[7]))

        ));
        LaunchpadManager.file.addLine(args[1] + "@" + p.getWorld().getName() + "@" + x + "@" + y + "@" + z + "@" + args[5] + "@" + args[6] + "@" + args[7]);

        Main.sendMessage(p, ChatColor.GREEN + "Successfully added new launchpad!", false);
        return true;

    }

    public boolean remove(Player p, String name) {

        Launchpad pad = get(p, name);
        if (pad == null) return false;

        pad.remove();
        LaunchpadManager.file.removeLine(pad.name + "@" + p.getWorld().getName() + "@" + pad.l.getX() + "@" + pad.l.getY() + "@" + pad.l.getZ()
                + "@" + pad.v.getX() + "@" + pad.v.getY() + "@" + pad.v.getZ());

        Main.sendMessage(p, ChatColor.GREEN + "Successfully removed launchpad!", false);
        return true;

    }

    public boolean modify(Player p, String[] args) {

        Launchpad pad = get(p, args[1]);
        if (pad == null) return false;



        return true;

    }

    public Launchpad get(Player p, String name) {

        Launchpad c = null;
        for (Launchpad pad : LaunchpadManager.launchpads) if (pad.name.equals(name)) c = pad;

        if (c == null) Main.sendMessage(p, ChatColor.RED + "There is no launchpad called '" + name + "'!", true);
        return c;

    }

}

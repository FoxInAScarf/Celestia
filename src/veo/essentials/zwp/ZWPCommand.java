package veo.essentials.zwp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;

public class ZWPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String labels, String[] args) {

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

            case "containers":

                switch (args[1]) {

                    case "add":

                        addContainer(p);
                        break;

                    case "remove":

                        removeContainer(p);
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

    private void addContainer(Player p) {

        Block b = p.getTargetBlock(null, 100);
        Material bt = b.getType();

        if (bt.equals(Material.AIR)) {

            Main.sendMessage(p, ChatColor.YELLOW + "Please face at a block!",
                    true);
            return;

        }

        if (!bt.equals(Material.BARREL)
                && !bt.equals(Material.ENDER_CHEST)
                && !bt.equals(Material.CHEST)) {

            Main.sendMessage(p, ChatColor.YELLOW +
                    "Your selected block isn't a valid container!", true);
            return;

        }

        if (ZWP.allowedContainers.contains(b)) {

            Main.sendMessage(p, ChatColor.YELLOW +
                    "Your selected block is already an allowed container!", true);
            return;

        }

        ZWP.allowedContainers.add(b);
        ZWP.ac.addLine(b.getWorld().getName() + " " + b.getX() + " "
                + b.getY() + " " + b.getZ());
        Main.sendMessage(p, ChatColor.GREEN + "Added block.", false);

    }

    private void removeContainer(Player p) {

        Block b = p.getTargetBlock(null, 100);
        Material bt = b.getType();

        if (bt.equals(Material.AIR)) {

            Main.sendMessage(p, ChatColor.YELLOW + "Please face at a block!",
                    true);
            return;

        }

        if (!bt.equals(Material.BARREL)
                && !bt.equals(Material.ENDER_CHEST)
                && !bt.equals(Material.CHEST)) {

            Main.sendMessage(p, ChatColor.YELLOW +
                    "Your selected block isn't a valid container!", true);
            return;

        }

        if (!ZWP.allowedContainers.contains(b)) {

            Main.sendMessage(p, ChatColor.YELLOW +
                    "Your selected block is not an allowed container!", true);
            return;

        }

        ZWP.allowedContainers.remove(b);
        ZWP.ac.removeLine(b.getWorld().getName() + " " + b.getX() + " "
                + b.getY() + " " + b.getZ());
        Main.sendMessage(p, ChatColor.GREEN + "Removed block.", false);

    }

}

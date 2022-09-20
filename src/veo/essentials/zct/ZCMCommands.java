package com.celestia.veo.essentials.zct;

import com.celestia.veo.Main;
import com.celestia.veo.essentials.zpm.ZPM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZCMCommands {

    public static class ChatCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

            if (!(s instanceof Player)) return false;

            Player p = (Player) s;
            if (args.length == 0) {

                Main.sendMessage(p, ChatColor.RED + "Please specify arguments. For help do '" +
                        "/chat help'!", true);
                return false;

            }

            switch (args[0]) {

                case "prefix":

                    ZPM.getPCP(p).enablePrefix(args[1].equals("on"));
                    Main.sendMessage(p, ChatColor.GREEN + "Chat prefix "
                            + (ZPM.getPCP(p).showChatPrefix
                            ? "enabled" : "disabled") + "!", false);
                    break;

                case "filter":

                    ZPM.getPCP(p).enableFilter(args[1].equals("on"));
                    Main.sendMessage(p, ChatColor.GREEN + "Chat filter "
                            + (ZPM.getPCP(p).isFilterEnabled
                            ? "enabled" : "disabled") + "!", false);
                    break;

                case "toggle":

                    ZPM.getPCP(p).enableChat(!ZPM.getPCP(p).isChatEnabled);
                    Main.sendMessage(p, ChatColor.GREEN + "Chat "
                            + (ZPM.getPCP(p).isChatEnabled
                            ? "enabled" : "disabled") + "!", false);
                    break;

                default:

                    Main.sendMessage(p, ChatColor.YELLOW + "There is no such argument as '"
                            + ChatColor.RED + args[0] + ChatColor.YELLOW + "'!", true);
                    break;

            }

            return false;

        }

    }

    public static class BlockCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender s, Command c, String labels, String[] args) {

            if (!(s instanceof Player)) return false;

            Player p = (Player) s;
            if (args.length == 0) {

                Main.sendMessage(p, ChatColor.RED + "Please specify a player!", true);
                return false;

            }

            Player toBlock = Bukkit.getPlayer(args[0]);
            if (p.equals(toBlock)) {

                Main.sendMessage(p, ChatColor.RED + "You can't block yourself... " +
                        "Silly!", true);
                return false;

            }

            if (ZPM.getPCP(p).blockedPlayers.contains(toBlock)) {

                Main.sendMessage(p, ChatColor.YELLOW + "The player named '" + args[0]
                        + "'is already blocked by you!", true);
                return false;

            }

            ZPM.getPCP(p).addBlockedPlayer(toBlock);
            Main.sendMessage(p, ChatColor.GREEN + "'" + args[0] + "' blocked.", false);
            return false;

        }

    }

    public static class UnblockCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender s, Command c, String labels, String[] args) {

            if (!(s instanceof Player)) return false;

            Player p = (Player) s;
            if (args.length == 0) {

                Main.sendMessage(p, ChatColor.RED + "Please specify a player!", true);
                return false;

            }

            Player toUnblock = Bukkit.getPlayer(args[0]);
            if (p.equals(toUnblock)) {

                Main.sendMessage(p, ChatColor.RED + "You can't even block yourself... " +
                        "Silly!", true);
                return false;

            }

            if (!ZPM.getPCP(p).blockedPlayers.contains(toUnblock)) {

                Main.sendMessage(p, ChatColor.YELLOW + "The player named '" + args[0]
                        + "'isn't blocked by you!", true);
                return false;

            }

            ZPM.getPCP(p).removeBlockedPlayer(toUnblock);
            Main.sendMessage(p, ChatColor.GREEN + "'" + args[0] + "' unblocked.", false);
            return false;

        }

    }

    public class FriendCommand {



    }

}
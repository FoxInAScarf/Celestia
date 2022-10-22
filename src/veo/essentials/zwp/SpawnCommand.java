package veo.essentials.zwp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import veo.Main;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player)) {

            System.out.println("[" + Main.name.toUpperCase()
                    + "-ERROR]: Only players can run this command!");
            return false;

        }
        ((Player) s).teleport(ZWP.respawn);

        return false;

    }

}

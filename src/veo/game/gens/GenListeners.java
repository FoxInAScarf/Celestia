package veo.game.gens;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import veo.Main;

public class GenListeners implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null)
            for (Flag f : GenManager.flags)
                if (f.head.equals(e.getClickedBlock().getLocation())) {

                    if (f.owner == null) f.claim(e.getPlayer());
                    else {

                        if (f.owner.equals(e.getPlayer())) {

                            Main.sendMessage(f.owner.getPlayer(), ChatColor.RED + "You've already" +
                                    " claimed this island. Try claiming another one!", true);
                            return;

                        }

                        Main.sendMessage(e.getPlayer(), ChatColor.RED + "This island has been " +
                                "already claimed.", true);
                        Main.sendMessage(e.getPlayer(), ChatColor.GREEN + "Unclaim it by crouching for " +
                                ChatColor.YELLOW + "10" + ChatColor.GREEN + " seconds by it.", true);

                    }
                    break;

                }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        for (Generator g : GenManager.gens) {

            Flag f = GenManager.getFlag(g.name);

            if (f == null) continue;
            if (f.owner != null)
                if (f.owner.equals(e.getPlayer())) {

                ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                        new PacketPlayOutEntityDestroy(f.stands.get("hasClaimed").getEntityId()));
                ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                        new PacketPlayOutEntityDestroy(f.stands.get("crouchHere").getEntityId()));

                } else ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                        new PacketPlayOutEntityDestroy(f.stands.get("youClaimed").getEntityId()));


        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        for (Flag f : GenManager.flags) if (f.owner.equals(e.getPlayer())) {

            if (f.owner == null) continue;
            f.unclaim();

        }

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        for (Flag f : GenManager.flags) if (f.owner == null) {

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "setblock " + ((int) f.head.getX()) + " " + ((int) f.head.getY()) + " " + ((int) f.head.getZ()) +
                            " minecraft:player_head[rotation=0]{SkullOwner:{" +
                            "Id:[I;-1217896545,-1015529000,-1506369421,548850094],Properties:{textures:[{Value:" +
                            "\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJkZDExZGEwNDI1MmY3NmI2OTM0YmMyNjYxMmY1NGYyNjRmMzBlZWQ3NGRmODk5NDEyMDllMTkxYmViYzBhMiJ9fX0=\"}]}}}");

        }

    }

}
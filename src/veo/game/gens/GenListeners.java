package veo.game.gens;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import veo.Main;
import veo.game.gens.flag.Flag;
import veo.game.gens.flag.FlagManager;

import java.util.Objects;

public class GenListeners implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getClickedBlock() == null || e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        for (Flag f : FlagManager.flags)
            if (f.head.equals(e.getClickedBlock().getLocation())) {

                if (Bukkit.getOnlinePlayers().size() <= 1) {

                    Main.sendMessage(e.getPlayer(), ChatColor.RED + "There need to be at least 2" +
                            " players online for the generators to work.", false);
                    return;

                }

                // conduct checkings for cooldown
                if (FlagManager.getCooldown(e.getPlayer()) != null) {

                    int time = (int) (FlagManager.getCooldown(e.getPlayer()).duration -
                            FlagManager.getCooldown(e.getPlayer()).time);
                    Main.sendMessage(e.getPlayer(), ChatColor.RED + "You're on cooldown!"
                            + " You can't claim another island for " + ChatColor.YELLOW + (int) Math.floor(time / (60 * 20))
                            + ChatColor.RED + " minutes and " + ChatColor.YELLOW + (int) Math.floor((time % (20 * 60)) / 20)
                            + ChatColor.RED + " seconds!", true);
                    return;

                }

                // conduct checkings for flag cap
                if (f.owner != null) {

                    int counter = 0;
                    for (Flag fl : FlagManager.flags)
                        if (fl.owner.getPlayer().equals(e.getPlayer())) counter++;

                    if (counter > 2) {

                        Main.sendMessage(e.getPlayer(), ChatColor.RED + "You can only claim 2 flags at once!", true);
                        return;

                    }

                }

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

    /*@EventHandler
    public void onJoin(PlayerJoinEvent e) {*/

        /*for (Generator g : GenManager.gens) {

            Flag f = FlagManager.getFlag(g.name);

            if (f == null) continue;
            if (f.owner != null) {

                f.flag.raise(8, f.owner.getPlayer());
                if (f.owner.getUniqueId().equals(e.getPlayer().getUniqueId())) {

                    ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                            new PacketPlayOutEntityDestroy(f.stands.get("hasClaimed").getEntityId()));
                    ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                            new PacketPlayOutEntityDestroy(f.stands.get("crouchHere").getEntityId()));

                } else ((CraftPlayer) e.getPlayer()).getHandle().b.a(
                        new PacketPlayOutEntityDestroy(f.stands.get("youClaimed").getEntityId()));

            }


        }*/

        // CUSTOM FLAG SYSTEM
        /*if (FlagManager.getFlag(e.getPlayer()) == null) {

            System.out.println("not contained");

            Material[][] m = new Material[FlagData.height][FlagData.width];
            for (int row = 0; row <= FlagData.height - 1; row++) {

                Material[] mRow = new Material[FlagData.width];
                for (int column = 0; column <= FlagData.width - 1; column++)
                    mRow[column] = Material.RED_WOOL;

                m[row] = mRow;

            }
            FlagManager.addFlag(e.getPlayer(), m);

        }*/

    /*}

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {



    }*/

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        for (Flag f : FlagManager.flags) {

            if (f.owner == null) continue;
            if (Bukkit.getOnlinePlayers().size() <= 1) {

                f.unclaim();
                continue;

            }
            if (Objects.equals(f.owner.getPlayer(), e.getPlayer()))
                f.unclaim();

        }

    }

    /*@EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        for (Flag f : FlagManager.flags) if (f.owner == null) {

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "setblock " + ((int) f.head.getX()) + " " + ((int) f.head.getY()) + " " + ((int) f.head.getZ()) +
                            " minecraft:player_head[rotation=0]{SkullOwner:{" +
                            "Id:[I;-1217896545,-1015529000,-1506369421,548850094],Properties:{textures:[{Value:" +
                            "\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJkZDExZGEwNDI1MmY3NmI2OTM0YmMyNjYxMmY1NGYyNjRmMzBlZWQ3NGRmODk5NDEyMDllMTkxYmViYzBhMiJ9fX0=\"}]}}}");

        }

    }*/

}

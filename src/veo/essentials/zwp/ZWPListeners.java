package com.celestia.veo.essentials.zwp;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ZWPListeners implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();
        if (p.isOp() || !ZWP.running) return;

        p.spawnParticle(Particle.SMOKE_NORMAL, e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        Player p = e.getPlayer();
        if (p.isOp() || !ZWP.running) return;

        p.spawnParticle(Particle.SMOKE_NORMAL, e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        if (p.isOp() || !ZWP.running) return;

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null) {

            Material cb = e.getClickedBlock().getType();
            if (cb.equals(Material.CHEST) || cb.equals(Material.BARREL) || cb.equals(Material.ENDER_CHEST))
                if (!ZWP.allowedContainers.contains(e.getClickedBlock())) {

                    p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
                    p.spawnParticle(Particle.SMOKE_NORMAL,
                            e.getClickedBlock().getLocation().add(0.5, 1.2, 0.5),
                            5, 0, 0, 0, 0);
                    e.setCancelled(true);

                }

            List<Material> trapdoors = new ArrayList<>();
            {

                trapdoors.add(Material.ACACIA_TRAPDOOR);
                trapdoors.add(Material.BIRCH_TRAPDOOR);
                trapdoors.add(Material.CRIMSON_TRAPDOOR);
                trapdoors.add(Material.OAK_TRAPDOOR);
                trapdoors.add(Material.JUNGLE_TRAPDOOR);
                trapdoors.add(Material.SPRUCE_TRAPDOOR);
                trapdoors.add(Material.WARPED_TRAPDOOR);
                trapdoors.add(Material.DARK_OAK_TRAPDOOR);
                trapdoors.add(Material.WARPED_TRAPDOOR);
                trapdoors.add(Material.DAYLIGHT_DETECTOR);
                trapdoors.add(Material.LEVER);

            }
            if (trapdoors.contains(cb)) {

                p.spawnParticle(Particle.SMOKE_NORMAL,
                        e.getClickedBlock().getLocation().add(0.5, 1.2, 0.5),
                        5, 0, 0, 0, 0);
                e.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void onBlockChange(BlockPhysicsEvent e) {

        /*System.out.println(e.getEventName());
        e.getBlock().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);*/

    }

}

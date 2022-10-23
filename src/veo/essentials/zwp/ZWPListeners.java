package veo.essentials.zwp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import veo.Main;

import java.util.ArrayList;
import java.util.HashMap;
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
            if (cb.equals(Material.CHEST) || cb.equals(Material.BARREL) || cb.equals(Material.ENDER_CHEST) || cb.equals(Material.BREWING_STAND)
                || cb.equals(Material.WHITE_CANDLE) || cb.equals(Material.ORANGE_CANDLE) || cb.equals(Material.PURPLE_CANDLE) || cb.equals(Material.CYAN_CANDLE)
                || cb.equals(Material.YELLOW_CANDLE) || cb.equals(Material.LIME_CANDLE) || cb.equals(Material.PINK_CANDLE) || cb.equals(Material.GRAY_CANDLE)
                || cb.equals(Material.LIGHT_GRAY_CANDLE) || cb.equals(Material.LIGHT_BLUE_CANDLE) || cb.equals(Material.MAGENTA_CANDLE) || cb.equals(Material.BLUE_CANDLE)
                || cb.equals(Material.BROWN_CANDLE) || cb.equals(Material.GREEN_CANDLE) || cb.equals(Material.RED_CANDLE) || cb.equals(Material.BLACK_CANDLE))

                // YES I HAVE TO MANUALLY CHECK ALL COLORS!

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
    public void onBlockFade(BlockFadeEvent e) {

        e.getBlock().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);

    }

    @EventHandler
    public void onBlockChange(EntityInteractEvent e) {

        if (e.getBlock().getType().equals(Material.FARMLAND)) {

            e.getBlock().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                    e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                    5, 0, 0, 0, 0);
            e.setCancelled(true);

        }

        /*System.out.println(e.getEventName());
        e.getBlock().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                e.getBlock().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);*/

    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {

        if (e.getPlayer().isOp()) return;
        //if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)
                || e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)
                || e.getRightClicked().getType().equals(EntityType.PAINTING)) {

            e.getRightClicked().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                    e.getRightClicked().getLocation().add(0.5, 1.2, 0.5),
                    5, 0, 0, 0, 0);
            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent e) {

        if (e.getPlayer().isOp()) return;
        e.getRightClicked().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                e.getRightClicked().getLocation().add(0.5, 1.2, 0.5),
                5, 0, 0, 0, 0);
        e.setCancelled(true);

    }


    HashMap<Player, Double> distanceMap = new HashMap<>();

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (p.getLocation().getY() < -64) {

            p.teleport(ZWP.respawn);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(20);
            for (PotionEffect pe : p.getActivePotionEffects())
                p.removePotionEffect(pe.getType());

        }

        if (!distanceMap.containsKey(p)) {

            distanceMap.put(p, p.getLocation().distance(ZWP.pvpProtectionCentre));
            return;

        }
        if (p.getLocation().distance(ZWP.pvpProtectionCentre) > ZWP.pvpProtectionRadius
                && distanceMap.get(p) < ZWP.pvpProtectionRadius) {

            p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Warning!",
                    ChatColor.RED + "PVP is enabled in this area.", 5, 40, 5);

        }
        if (p.getLocation().distance(ZWP.pvpProtectionCentre) < ZWP.pvpProtectionRadius
                && distanceMap.get(p) > ZWP.pvpProtectionRadius) {

            p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "PVP disabled!",
                    ChatColor.GREEN + ":)", 5, 40, 5);

        }


        distanceMap.put(p, p.getLocation().distance(ZWP.pvpProtectionCentre));

    }

    @EventHandler
    public void onFight(EntityDamageByEntityEvent e) {

        if (e.getEntity().getType().equals(EntityType.ARMOR_STAND) || e.getEntity().getType().equals(EntityType.PAINTING)
                || e.getEntity().getType().equals(EntityType.ITEM_FRAME))
            if (e.getDamager() instanceof Player) // it sucks i know but i dont have time to finish it properly
                if (!((Player) e.getDamager()).isOp()) {

                    e.getEntity().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                            e.getEntity().getLocation().clone().add(0.5, 1.2, 0.5),
                            5, 0, 0, 0, 0);
                    e.setCancelled(true);

                }

        if (e.getEntity().getLocation().distance(ZWP.pvpProtectionCentre) < ZWP.pvpProtectionRadius ||
                e.getDamager().getLocation().distance(ZWP.pvpProtectionCentre) < ZWP.pvpProtectionRadius) {

            Main.sendMessage((Player) e.getDamager(), ChatColor.RED + "Oops! Can't fight here!", true);
            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) { e.setRespawnLocation(ZWP.respawn); }
    @EventHandler
    public void onCraft(CraftItemEvent e) { e.setCancelled(true); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) { e.getPlayer().teleport(ZWP.respawn); }

}

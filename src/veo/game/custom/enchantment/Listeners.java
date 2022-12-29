package veo.game.custom.enchantment;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import veo.Main;
import veo.essentials.zwp.ZWPListeners;
import veo.game.items.ZItemManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Listeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player p)) return;

        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;

        List<String> enchants = getEnchants(p.getInventory().getItemInMainHand());

        if (enchants.contains("FROSTBITE")) {

            //e.getEntity().getWorld().spawnParticle(Particle.ITEM_CRACK, e.getEntity().getLocation(), 100, 0.8, 2, 0.8, Material.BLUE_ICE);
            p.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getEntity().getLocation().add(0, 1, 0), 40, 0.6, 1.2, 0.6, Material.ICE.createBlockData());
            //new ZParticle(Particles.a, e.getEntity().getLocation().add(0, 1, 0), 30, new double[]{0.8, 1.5, 0.8}).playParticle(true, null);
            e.getEntity().setFreezeTicks(140);

        }
        if (enchants.contains("CRUX")) {

            p.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getEntity().getLocation().add(0, 1, 0), 40, 0.6, 1.2, 0.6, Material.YELLOW_CONCRETE_POWDER.createBlockData());
            //e.getEntity().getWorld().spawnParticle(Particle.ITEM_CRACK, e.getEntity().getLocation(), 100, 0.8, 2, 0.8, Material.SAND);
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false));
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

        }
        if (enchants.contains("MURDER"))
            p.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getEntity().getLocation().add(0, 1, 0), 40, 0.6, 1.2, 0.6, Material.RED_CONCRETE.createBlockData());

    }

    @EventHandler
    public void onKillEvent(EntityDeathEvent e) {

        if (e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();

        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;

        List<String> enchants = getEnchants(p.getInventory().getItemInMainHand());

        if (enchants.contains("MURDER") && ZItemManager.getItem("heart") != null)
            p.getInventory().addItem(ZItemManager.getItem("heart"));

    }

    static HashMap<Player, Integer> waveCooldown = new HashMap<>();
    static HashMap<Player, Integer> rayCooldown = new HashMap<>();
    int task1, task2;

    @EventHandler
    public void onRightClickEvent(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;
        List<String> enchants = getEnchants(p.getInventory().getItemInMainHand());
        if (!enchants.contains("CRUX")) return;

        if (ZWPListeners.isPlayerInProtectedArea(p)) {

            Main.sendMessage(p, ChatColor.RED + "Oops! Can't fight here!", true);
            e.setCancelled(true);
            return;

        }

        /*
         *
         *
         * WAVE
         *
         *
         * */

        if (p.isSneaking()) {

            if (waveCooldown.containsKey(p)) {

                Main.sendMessage(p, ChatColor.RED + "You're on cooldown! You have to wait " + waveCooldown.get(p) + " seconds!", true);
                return;

            }

            for (Entity en : p.getNearbyEntities(6, 6, 6)) {

                if (!(en instanceof Player cp)) continue;
                cp.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);

            }
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);
            AtomicReference<Double> iteration = new AtomicReference<>(0.0);
            task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

                if (iteration.get() >= 5) Bukkit.getScheduler().cancelTask(task1);

                for (Entity en : p.getNearbyEntities(6, 6, 6)) if (en instanceof Player c) {

                    if (p.getUniqueId().equals(c.getUniqueId())) continue;
                    if (c.getLocation().distance(p.getLocation()) <= iteration.get()) {

                        c.damage(6);
                        double factor = 0.5, xVel = (p.getLocation().getX() - c.getLocation().getX()) * factor,
                                yVel = 0.1 * factor,
                                zVel = (p.getLocation().getZ() - c.getLocation().getZ()) * factor;
                        // "im such a gay furboy i wanna suck cock UwU" - JovannMC
                        c.setVelocity(new Vector(-xVel, yVel, -zVel));
                        c.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false));
                        c.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

                    }

                }

                drawCircle(0, 360, p.getLocation().clone().add(0, 1, 0), iteration.get());
                iteration.set(iteration.get() + 0.5);

            }, 0L, 1L);

            waveCooldown.put(p, 20);
            return;

        }

        /*
        *
        *
        * RAY
        *
        *
        * */

        if (rayCooldown.containsKey(p)) {

            Main.sendMessage(p, ChatColor.RED + "You're on cooldown! You have to wait " + rayCooldown.get(p) + " seconds!", true);
            return;

        }

        for (Entity en : p.getNearbyEntities(6, 6, 6)) {

            if (!(en instanceof Player cp)) continue;
            cp.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);

        }
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);
        AtomicReference<Double> iteration = new AtomicReference<>(0.0);
        task2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (iteration.get() >= 5) Bukkit.getScheduler().cancelTask(task2);

            for (Entity en : p.getNearbyEntities(6, 6, 6)) if (en instanceof Player c) {

                if (p.getUniqueId().equals(c.getUniqueId())) continue;
                double daa = Math.toDegrees(Math.atan2(en.getLocation().getX() - p.getLocation().getX(), en.getLocation().getZ() - p.getLocation().getZ()));
                if (c.getLocation().distance(p.getLocation()) <= iteration.get() && (daa >= -p.getLocation().getYaw() - 20 && daa <= -p.getLocation().getYaw() + 20)) {

                    c.damage(16);
                    c.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false));
                    c.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

                }

            }

            drawCircle(-p.getLocation().getYaw() - 20, -p.getLocation().getYaw() + 20, p.getLocation().clone().add(0, 1, 0), iteration.get());
            iteration.set(iteration.get() + 0.5);

        }, 0L, 1L);

        rayCooldown.put(p, 8);


    }

    public static void startCooldownLoop() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            Iterator<Map.Entry<Player, Integer>> waveIterator = waveCooldown.entrySet().iterator(),
                    rayIterator = rayCooldown.entrySet().iterator();

            while (waveIterator.hasNext()) {

                Map.Entry<Player, Integer> e = waveIterator.next();
                if (e.getValue() == 0) {

                    waveIterator.remove();
                    continue;

                }
                waveCooldown.put(e.getKey(), e.getValue() - 1);

            }

            while (rayIterator.hasNext()) {

                Map.Entry<Player, Integer> e = rayIterator.next();
                if (e.getValue() == 0) {

                    rayIterator.remove();
                    continue;

                }
                rayCooldown.put(e.getKey(), e.getValue() - 1);

            }

        }, 0L, 20L);

    }

    private void drawCircle(double from, double to, Location l, double r) {

        for (double theta = from; theta <= to; theta += 1) {

            double sin = Math.sin(Math.toRadians(theta)) * r, cos = Math.cos(Math.toRadians(theta)) * r;
            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, new Location(l.getWorld(), l.getX() + sin, l.getY(), l.getZ() + cos),
                    1, 0.01, 0.01, 0.01, Material.YELLOW_CONCRETE_POWDER.createBlockData());

        }

    }

    private List<String> getEnchants(ItemStack i) {

        ItemMeta meta = i.getItemMeta();
        List<String> enchants = new ArrayList<>();
        for (Map.Entry<Enchantment, Integer> en : meta.getEnchants().entrySet())
            enchants.add(en.getKey().getName());

        //System.out.println(meta.getDisplayName() + ": " + enchants);
        return enchants;

    }

}

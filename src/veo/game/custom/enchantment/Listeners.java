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
import veo.game.custom.enchantment.misc.RayAbility;
import veo.game.custom.enchantment.misc.WaveAbility;
import veo.game.items.ZItemManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Listeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player p)) return;

        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;
        if (ZWPListeners.isPlayerInProtectedArea(p)) return;

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

        if (p.isSneaking()) {

            new WaveAbility(p);
            return;

        }
        new RayAbility(p);


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

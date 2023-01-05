package veo.game.custom.enchantment.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import veo.Main;

import java.util.concurrent.atomic.AtomicReference;

public class RayAbility {

    int loop;

    public RayAbility(Player p) {

        if (CruxAbilityManager.rayCooldown.containsKey(p)) {

            Main.sendMessage(p, ChatColor.RED + "You're on cooldown! You have to wait " + CruxAbilityManager.rayCooldown.get(p) + " seconds!", true);
            return;

        }

        for (Entity en : p.getNearbyEntities(6, 6, 6)) {

            if (!(en instanceof Player cp)) continue;
            cp.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);

        }
        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 12.0F);
        AtomicReference<Double> iteration = new AtomicReference<>(0.0);
        loop = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {

            if (iteration.get() >= 5) Bukkit.getScheduler().cancelTask(loop);

            for (Entity en : p.getNearbyEntities(6, 6, 6)) if (en instanceof Player c) {

                if (p.getUniqueId().equals(c.getUniqueId())) continue;
                double daa = Math.toDegrees(Math.atan2(en.getLocation().getX() - p.getLocation().getX(), en.getLocation().getZ() - p.getLocation().getZ()));
                if (c.getLocation().distance(p.getLocation()) <= iteration.get() && (daa >= -p.getLocation().getYaw() - 20 && daa <= -p.getLocation().getYaw() + 20)) {

                    c.damage(10);
                    c.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false));
                    c.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

                }

            }

            CruxAbilityManager.drawCircle(-p.getLocation().getYaw() - 20, -p.getLocation().getYaw() + 20, p.getLocation().clone().add(0, 1, 0), iteration.get());
            iteration.set(iteration.get() + 0.5);

        }, 0L, 1L);

        CruxAbilityManager.rayCooldown.put(p, 8);

    }

}

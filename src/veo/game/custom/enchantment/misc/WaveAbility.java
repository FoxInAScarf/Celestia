package veo.game.custom.enchantment.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import veo.Main;
import veo.essentials.zpm.ZPM;
import veo.essentials.zpm.profiles.PlayerGameProfile;

import javax.xml.stream.Location;
import java.util.concurrent.atomic.AtomicReference;

public class WaveAbility {

    int loop;

    public WaveAbility(Player p) {

        if (CruxAbilityManager.waveCooldown.containsKey(p)) {

            Main.sendMessage(p, ChatColor.RED + "You're on cooldown! You have to wait " + CruxAbilityManager.waveCooldown.get(p) + " seconds!", true);
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
                if (c.getLocation().distance(p.getLocation()) <= iteration.get()) {

                    PlayerGameProfile pgp = ZPM.getPGP(c);
                    if (pgp.timePlayed < 30) {

                        Main.sendMessage(p, ChatColor.RED + c.getName() + " is a new player. They still have "
                                + (30 - pgp.timePlayed) + " minutes of their grace period.", true);
                        continue;

                    }
                    c.damage(5);
                    double factor = 0.5, xVel = (p.getLocation().getX() - c.getLocation().getX()) * factor,
                            yVel = 0.1 * factor,
                            zVel = (p.getLocation().getZ() - c.getLocation().getZ()) * factor;
                    // "im such a gay furboy i wanna suck cock UwU" - JovannMC
                    c.setVelocity(new Vector(-xVel, yVel, -zVel));
                    c.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2, false));
                    c.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

                }

            }

            CruxAbilityManager.drawCircle(0, 360, p.getLocation().clone().add(0, 1, 0), iteration.get());
            iteration.set(iteration.get() + 0.5);

        }, 0L, 1L);

        CruxAbilityManager.waveCooldown.put(p, 20);

    }

}

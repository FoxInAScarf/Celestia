package veo.game.custom.enchantment.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import veo.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CruxAbilityManager {

    static HashMap<Player, Integer> waveCooldown = new HashMap<>();
    static HashMap<Player, Integer> rayCooldown = new HashMap<>();

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

    static void drawCircle(double from, double to, Location l, double r) {

        for (double theta = from; theta <= to; theta += 1) {

            double sin = Math.sin(Math.toRadians(theta)) * r, cos = Math.cos(Math.toRadians(theta)) * r;
            l.getWorld().spawnParticle(Particle.BLOCK_CRACK, new Location(l.getWorld(), l.getX() + sin, l.getY(), l.getZ() + cos),
                    1, 0.01, 0.01, 0.01, Material.YELLOW_CONCRETE_POWDER.createBlockData());

        }

    }

    /*static double calcDamage(double total, Player p) {

        total = 0;

        ItemStack helmet = p.getInventory().getArmorContents()[0];
        if (helmet != null) {

            ItemMeta meta = helmet.getItemMeta();
            if (meta == null) {

                switch (helmet.getType()) {

                    case Material.IRON_HELMET ->
                    case Material. ->

                }

            } else {

                total += helmet.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR).toArray()[0];

            }

        }
        p.getInventory().getArmorContents()[1]
        p.getInventory().getArmorContents()[2]
        p.getInventory().getArmorContents()[3]

    }*/

}

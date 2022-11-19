package veo.game.custom.enchantment;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import veo.game.items.ZItemManager;

public class Listeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) return;
        Player p = (Player) e.getDamager();

        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (!p.getInventory().getItemInMainHand().hasItemMeta()) return;

        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.FROSTBITE))
            e.getEntity().setFreezeTicks(140);

        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.CRUX)) {

            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, false));
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30 * 20, 3, false));

        }

        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchantments.MURDER)
            && e.getEntity().isDead() && ZItemManager.getItem("heart") != null)
            p.getInventory().addItem(ZItemManager.getItem("heart"));

    }

}

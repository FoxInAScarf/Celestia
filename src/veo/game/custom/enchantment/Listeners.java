package veo.game.custom.enchantment;

import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(damager.getInventory().getItemInMainHand());
        System.out.println(nmsItem.b("FROSTBITE").toString());
        if (nmsItem.a("FROSTBITE") != null) {

            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 10, true));
            e.getEntity().setFreezeTicks(40);
            /*
            *
            * add frostbite effect
            *
            * */

        }

    }

}

package veo.game.npcs;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import veo.game.shop.Shop;
import veo.game.shop.ShopManager;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        for (NPC n : NPCManager.npcs) if (n.l.getChunk().isLoaded()) n.createTo(e.getPlayer());

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {

        for (NPC n : NPCManager.npcs) if (n.l.getChunk().equals(e.getChunk()))
            for (Player p : Bukkit.getOnlinePlayers()) n.createTo(p);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        for (NPC n : NPCManager.npcs)
            if (e.getEntity().equals(n.hitbox1) || e.getEntity().equals(n.hitbox2)) e.setCancelled(true);

    }

    @EventHandler
    public void playerInteractEntity(PlayerInteractEntityEvent e) {

        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        for (NPC n : NPCManager.npcs)
            if (e.getRightClicked().equals(n.hitbox1) || e.getRightClicked().equals(n.hitbox2))
                switch (n.actionType) {

                    case 0:

                        Shop shop = (Shop) n.value;
                        ShopManager.addInstance(e.getPlayer(), shop);
                        ShopManager.instances.get(ShopManager.instances.size() - 1).display();
                        break;

                    case 1:
                        e.getPlayer().getInventory().addItem((ItemStack) n.value);
                        break;

                    case 3:
                        e.getPlayer().teleport((Location) n.value);
                        break;

                    case 4:
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), (String) n.value);
                        break;

                    case 5:
                        e.getPlayer().sendMessage((String) n.value);

                }

    }

}

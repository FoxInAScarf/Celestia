package veo.game.shop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class Listeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        ShopInstance si = getInstanceByPlayer((Player) e.getWhoClicked());
        if (si == null) return;
        if (e.getCurrentItem() == null) return;
        else e.setCancelled(true);

        switch (e.getRawSlot()) {

            case 0:

                if (si.currentPage > 1) si.currentPage--;
                e.getWhoClicked().closeInventory();
                si.shop.build(si.currentPage);
                e.getWhoClicked().openInventory(si.shop.inv);
                break;

            case 8:

                if (si.currentPage < 4) si.currentPage++;
                e.getWhoClicked().closeInventory();
                si.shop.build(si.currentPage);
                e.getWhoClicked().openInventory(si.shop.inv);
                break;

        }

    }

    /*@EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {

        ShopInstance si = getInstanceByPlayer((Player) e.getPlayer());
        if (si != null) ShopManager.instances.remove(si);

    }*/

    private ShopInstance getInstance(Inventory i) {

        for (ShopInstance si : ShopManager.instances) if (si.shop.inv.equals(i)) return si;
        return null;

    }

    private ShopInstance getInstanceByPlayer(Player p) {

        for (ShopInstance si : ShopManager.instances) if (si.owner.equals(p)) return si;
        return null;

    }

}

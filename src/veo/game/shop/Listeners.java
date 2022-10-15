package veo.game.shop;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import veo.Main;

import java.util.Iterator;
import java.util.Map;

public class Listeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        ShopInstance si = getInstanceByPlayer(p);
        if (si == null) return;
        if (e.getCurrentItem() == null) return;
        if (e.getInventory().equals(si.shop.inv)) {

            e.setCancelled(true);

            if (si.shop.pages != 1) switch (e.getRawSlot()) {

                case 0:

                    if (si.currentPage > 1) si.currentPage--;
                    //p.closeInventory();
                    si.shop.build(si.currentPage);
                    p.openInventory(si.shop.inv);
                    break;

                case 8:

                    if (si.currentPage < si.shop.pages) si.currentPage++;
                    //p.closeInventory();
                    si.shop.build(si.currentPage);
                    p.openInventory(si.shop.inv);
                    break;

            }

            Recipe r = getRecipe(si, e.getCurrentItem());
            if (r == null) return;
            if (hasAllIngredients(p, r)) {

                for (Map.Entry<ItemStack, Integer> map : r.elements.entrySet()) for (int i = 0; i <= map.getValue(); i++)
                    p.getInventory().remove(map.getKey().clone()); // Fix this Zraphy!
                p.getInventory().addItem(r.item);
                Main.sendMessage(p, ChatColor.GREEN + "You bought: " + r.item.getItemMeta().getDisplayName()
                        + ChatColor.GREEN + (r.item.getAmount() > 1 ? " x" + r.item.getAmount() + "!" : "!"), false);
                return;

            }
            Main.sendMessage(p, ChatColor.RED + "You don't have the necessary resources to buy this item!", true);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

        }

    }

    private boolean hasAllIngredients(Player p, Recipe r) {

        Iterator<Map.Entry<ItemStack, Integer>> i = r.elements.entrySet().iterator();
        int count = 0;
        while (i.hasNext()) {

            Map.Entry<ItemStack, Integer> map = i.next();
            if (p.getInventory().containsAtLeast(map.getKey(), map.getValue())) count++;

        }

        return count >= r.elements.size();

    }

    private Recipe getRecipe(ShopInstance si, ItemStack is) {

        for (Recipe r : si.shop.recipes) if (r.item.equals(is)) return r;
        return null;

    }

    private ShopInstance getInstanceByPlayer(Player p) {

        for (ShopInstance si : ShopManager.instances) if (si.owner.equals(p)) return si;
        return null;

    }

}

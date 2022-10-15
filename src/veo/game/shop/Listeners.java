package veo.game.shop;

import net.minecraft.world.entity.player.PlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import veo.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Listeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        int a = 0, b = 0;
        for (ItemStack is : e.getWhoClicked().getInventory().getContents()) if (is != null) {

            a++;
            if (equal(is, "Iron Ingot")) b++;

        }
        e.getWhoClicked().sendMessage("A: " + a);
        e.getWhoClicked().sendMessage("B: " + b);

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

            Inventory correctedInv = Bukkit.createInventory(p, InventoryType.PLAYER);
            correctedInv.setContents(p.getInventory().getContents());
            for (ItemStack is : si.shop.inv.getContents()) if (is != null)
                correctedInv.remove(is);

            Recipe r = getRecipeByResult(si, e.getCurrentItem().getItemMeta().getDisplayName());
            if (r == null) return;
            if (correctedInv.getContents().length == 0) {

                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
                Main.sendMessage(p, ChatColor.RED + "You don't have enough items for this specific item!", true);
                return;

            }

            int check = 0;
            Iterator<Map.Entry<ItemStack, Integer>> i = r.elements.entrySet().iterator();
            while (i.hasNext()) {

                Map.Entry<ItemStack, Integer> m = i.next();
                if (hasEnoughOf(correctedInv, m.getKey().getItemMeta().getDisplayName(), m.getValue()))
                    check++;

            }

            if (check == r.elements.size() - 1) {

                Iterator<Map.Entry<ItemStack, Integer>> i1 = r.elements.entrySet().iterator();
                while (i.hasNext()) {

                    Map.Entry<ItemStack, Integer> m = i1.next();
                    for (int j = 0; j <= m.getValue() - 1; j++)
                        for (ItemStack is : correctedInv.getContents())
                            if (is != null)
                                if (equal(is, m.getKey()))
                                    is.setAmount(is.getAmount() - 1);

                }
                Main.sendMessage(p, ChatColor.GREEN + "You bought: "
                        + r.item.getItemMeta().getDisplayName() + ChatColor.GREEN
                        + (r.item.getAmount() > 1 ? " x" + r.item.getAmount() + "!" : "!"), false);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                p.getInventory().addItem(r.item);
                return;

            }
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            Main.sendMessage(p, ChatColor.RED + "You don't have enough items for this specific item!", true);

        }

    }

    private boolean hasEnoughOf(Inventory inv, String name, int amount) {

        int count = 0;
        for (ItemStack is : inv.getContents()) if (is != null)
            if (equal(name, is)) count++;

        return count >= amount;

    }

    private Recipe getRecipeByResult(ShopInstance si, String name) {

        for (Recipe r : si.shop.recipes) if (equal(r.item, name)) return r;
        return null;

    }

    private ShopInstance getInstanceByPlayer(Player p) {

        for (ShopInstance si : ShopManager.instances) if (si.owner.equals(p)) return si;
        return null;

    }

    private boolean equal(Object a, Object b) {

        String an, bn;
        if (a instanceof ItemStack) {

            ItemStack aS = (ItemStack) a;
            if (aS.getItemMeta() == null) an = aS.getType().name();
            else an = aS.getItemMeta().getDisplayName();

        } else {

            String[] aa = ((String) a).toLowerCase().split("_");
            an = "";
            for (String as : aa) {

                String[] ab = as.split("");
                an += ab[0].toUpperCase();
                for (int i = 1; i <= ab.length - 1; i++) an += ab[i];
                an += " ";

            }
            an = an.substring(an.length(), an.length() - 1);
            System.out.println(an);

        }
        if (b instanceof ItemStack) {

            ItemStack bS = (ItemStack) b;
            if (bS.getItemMeta() == null) bn = bS.getType().name();
            else bn = bS.getItemMeta().getDisplayName();

        } else {

            String[] aa = ((String) b).toLowerCase().split("_");
            bn = "";
            for (String as : aa) {

                String[] ab = as.split("");
                bn += ab[0].toUpperCase();
                for (int i = 1; i <= ab.length - 1; i++) bn += ab[i];
                bn += " ";

            }
            bn = bn.substring(bn.length(), bn.length() - 1);
            System.out.println(bn);

        }

        return an.equals(bn);

    }

}

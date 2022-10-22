package veo.game.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import veo.Main;

import java.util.HashMap;
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

                for (Map.Entry<ItemStack, Integer> map : r.elements.entrySet()) {

                    ItemStack is = map.getKey().clone();
                    is.setAmount(map.getValue());
                    removeItems(p.getInventory(), is);

                }
                p.getInventory().addItem(r.item);
                String name;
                if (r.item.hasItemMeta()) name = r.item.getItemMeta().getDisplayName();
                else {

                    name = "";
                    for (String as : r.item.getType().name().toLowerCase().split("_")) {

                        String[] ab = as.split("");
                        name += ab[0].toUpperCase();
                        for (int i = 1; i <= ab.length - 1; i++) name += ab[i];
                        name += " ";

                    }
                    String nCopy = "";
                    String[] nCopyS = name.split("");
                    for (int i = 0; i <= nCopyS.length - 2; i++) nCopy += nCopyS[i];
                    name = nCopy;

                }

                Main.sendMessage(p, ChatColor.GREEN + "You bought: " + name
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
            //if (p.getInventory().containsAtLeast(map.getKey(), map.getValue())) count++;
            if (containsAtLeast(p.getInventory(), map.getKey(), map.getValue())) count++;

        }

        return count >= r.elements.size();

    }

    private boolean containsAtLeast(Inventory i, ItemStack is, int value) {

        HashMap<String, Integer> iVmap = new HashMap<>();

        for (ItemStack isL : i.getContents()) {

            if (isL == null) continue;

            String isLNAME = getName(isL);
            if (iVmap.containsKey(isLNAME)) iVmap.put(isLNAME, iVmap.get(isLNAME) + isL.getAmount());
            else iVmap.put(isLNAME, isL.getAmount());

        }

        //System.out.println("");
        for (Map.Entry<String, Integer> entry : iVmap.entrySet()) {

            //System.out.println(entry.getKey() + ": " + entry.getValue());
            /*if (entry.getKey().equals(getName(is))) System.out.println("COCK 1");
            if (entry.getValue() >= value) System.out.println("COCK 2");*/
            if (entry.getKey().equals(getName(is)) && entry.getValue() >= value) return true;

        }
        return false;

    }

    private Recipe getRecipe(ShopInstance si, ItemStack is) {

        for (Recipe r : si.shop.recipes) if (nameEquals(r.item, is) && r.item.getAmount() == is.getAmount()) return r;
        return null;

    }

    private ShopInstance getInstanceByPlayer(Player p) {

        for (ShopInstance si : ShopManager.instances) if (si.owner.equals(p)) return si;
        return null;

    }

    // You might want to use this later, Zraphy...
    private void removeItems(Inventory i, ItemStack is) {

        int amount = is.getAmount();
        for (int j = 0; j <= i.getContents().length - 1; j++) {

            if (amount == 0) return;

            ItemStack is1 = i.getItem(j);
            if (is1 != null) if (ignoreAmountEquals(is, is1)) {

                if (is1.getAmount() - amount < 0) { // if is1 doesn't contain enough items

                    amount = amount - is1.getAmount();
                    i.setItem(j, new ItemStack(Material.AIR));
                    continue;

                }
                if (is1.getAmount() - amount > 0) { // if is1 contains enough items BUT there are leftovers

                    ItemStack is1COPY = is1.clone();
                    is1COPY.setAmount(is1.getAmount() - amount);
                    i.setItem(j, is1COPY);
                    return;

                }
                i.setItem(j, new ItemStack(Material.AIR));
                return;

            }
        }

    }

    private boolean ignoreAmountEquals(ItemStack is1, ItemStack is2) {

        /*ItemStack is1COPY = is1.clone();
        is1COPY.setAmount(1);
        net.minecraft.world.item.ItemStack is1COPYNBT = CraftItemStack.asNMSCopy(is1COPY);
        System.out.println(is1COPYNBT.u().toString());

        ItemStack is2COPY = is2.clone();
        is2COPY.setAmount(1);
        net.minecraft.world.item.ItemStack is2COPYNBT = CraftItemStack.asNMSCopy(is2COPY);
        System.out.println(is2COPYNBT.u().toString());

        return is1COPY.equals(is2COPY);*/

        return nameEquals(is1, is2);

    }

    private boolean nameEquals(ItemStack i1, ItemStack i2) {

        return getName(i1).equals(getName(i2));

    }

    private String getName(ItemStack i) {

        if (i.hasItemMeta()) return i.getItemMeta().getDisplayName();
        return i.getType().name();

    }

}

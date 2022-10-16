package veo.game.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import veo.game.items.ZItemManager;

import java.util.HashMap;

public class Recipe {

    public ItemStack item;
    public HashMap<ItemStack, Integer> elements = new HashMap<>();

    public Recipe(String item, int amount) {

        this.item = getItem(item, amount);

    }

    public void addElement(String item, int amount) {

        ItemStack i = getItem(item, amount);
        elements.put(i, amount);

        //System.out.println("Added: " + item + " " + amount + " -> "
        //        + elements.get(elements.size() - 1).getType() + " " + elements.get(elements.size() - 1).getAmount());

    }

    private ItemStack getItem(String item, int amount) {

        ItemStack zitem = ZItemManager.getItem(item);
        if (zitem != null) {

            zitem.setAmount(amount);
            return zitem;

        }
        Material m = Material.getMaterial(item.toUpperCase());
        if (m == null) {

            System.out.println("[ShopReader-ERROR]: Specified item does not exist." /*+ (this.item != null ? this.item.getItemMeta().getDisplayName() : this.item.getType())*/);
            return null;

        }
        return new ItemStack(m, amount);

    }

}

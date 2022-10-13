package veo.game.shop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import veo.game.items.ZItem;
import veo.game.items.ZItemManager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public ItemStack item;
    public List<ItemStack> elements = new ArrayList<>();

    public Recipe(String item, int amount) {

        this.item = getItem(item, amount);

    }

    public void addElement(String item, int amount) {

        ItemStack i = getItem(item, amount);
        elements.add(i);

    }

    private ItemStack getItem(String item, int amount) {

        ItemStack zitem = ZItemManager.getItem(item);
        if (zitem != null) {

            zitem.setAmount(amount);
            return zitem;

        }
        zitem = new ItemStack(Material.getMaterial(item.toUpperCase()), amount);
        if (zitem == null) {

            System.out.println("[ShopReader-ERROR]: Specified item does not exist.");
            return null;

        }
        return zitem;

    }

}
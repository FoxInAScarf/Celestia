package veo.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopInstance {

    int currentPage = 1;
    Player owner;
    Shop shop;

    public ShopInstance(Player owner, Shop shop) {

        this.shop = shop.clone();
        this.owner = owner;

    }

    public void display() {

        owner.openInventory(shop.inv);

    }

}

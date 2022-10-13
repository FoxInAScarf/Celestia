package veo.game.shop;

import org.bukkit.entity.Player;

public class ShopInstance {

    int currentPage = 1;
    Player owner;
    Shop shop;

    public ShopInstance(Player owner, Shop shop) {

        this.shop = shop;
        this.owner = owner;

    }

    public void display() {

        shop.displayGUI(owner, currentPage);

    }

}

package veo.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import veo.Main;
import veo.game.items.ZItem;
import veo.game.items.ZItemCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopManager {

    static List<Shop> shops = new ArrayList<>();
    static String folder;

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/Shops";
        if (!new File(folder).exists()) new File(folder).mkdir();

        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
        load();
        Main.getInstance().getCommand("shop").setExecutor(new ShopCommand());

    }

    public static void load() {

        shops.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) shops.add(new Shop(f));

    }

    public static Inventory getShop(String name) {

        return null;

    }
    
}

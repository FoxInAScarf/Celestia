package veo.game.shop;

import org.bukkit.Bukkit;
import veo.Main;

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

    public static Shop getShop(String name) {

        for (Shop s : shops) if (s.name.equals(name)) return s;
        return null;

    }
    
}

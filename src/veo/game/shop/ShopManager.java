package veo.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import veo.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopManager {

    static List<Shop> shops = new ArrayList<>();
    static List<ShopInstance> instances = new ArrayList<>();
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
        instances.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) shops.add(new Shop(f));

    }

    public static Shop getShop(String name) {

        for (Shop s : shops) if (s.name.equals(name)) return s;
        return null;

    }

    public static void addInstance(Player p, Shop s) {

        for (int i = 0; i <= instances.size() - 1; i++) if (instances.get(i).owner.equals(p))
            instances.remove(instances.get(i));

        instances.add(new ShopInstance(p, s));

    }
    
}

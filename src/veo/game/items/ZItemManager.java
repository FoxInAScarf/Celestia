package veo.game.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import veo.Main;
import veo.game.custom.enchantment.CustomEnchantments;
import veo.game.custom.enchantment.Listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZItemManager {

    static List<ZItem> items = new ArrayList<>();
    static String folder;

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/Items";
        if (!new File(folder).exists()) new File(folder).mkdir();

        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
        CustomEnchantments.register();
        load();
        Main.getInstance().getCommand("zitem").setExecutor(new ZItemCommand());

    }

    public static void load() {

        items.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) items.add(new ZItem(f));

    }

    public static ItemStack getItem(String name) {

        for (ZItem i : items) if (i.name.equalsIgnoreCase(name)) return i.item;
        Material m = Material.getMaterial(name.toUpperCase());
        return m == null ? null : new ItemStack(m);

    }

}

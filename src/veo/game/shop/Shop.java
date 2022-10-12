package veo.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop extends ZFile {

    String name, displayName;
    List<Recipe> recipes = new ArrayList<>();
    int pages = 1, currentPage = 1;

    public Shop(File f) {

        super(f.getAbsolutePath());

        List<String> nLines = new ArrayList<>();
        for (String s : lines) nLines.add(s.replaceAll("&", "§"));
        lines.clear();
        lines.addAll(nLines);
        name = f.getName().replaceAll(".zra", "");

        HashMap<String, String> data = getData();
        if (!data.containsKey("name")) {

            System.out.println("[ShopReader-ERROR]: Display name is not defined!" + name);
            return;

        }
        displayName = data.get("name");
        if (!lines.contains("recipes")) {

            System.out.println("[ShopReader-WARNING]: '" + name + "' has no recipes!");
            return;

        }
        readRecipes();

        pages = (int) Math.ceil(recipes.size() / 4);

    }

    private HashMap<String, String> getData() {

        HashMap<String, String> data = new HashMap<>();
        for (String s : lines) if (s.contains("=")) {

            String[] ss = s.split("=");
            data.put(ss[0], ss[1]);

        }
        return data;

    }

    public void readRecipes() {

        // example:
        // enchanted_diamond@2 AND custom_iron_sword@1 -> frozen_diamond_sword@1
        int i = lines.indexOf("recipes");
        while (lines.get(i + 1).split("")[0].equals(" ")
                && lines.get(i + 1).split("")[1].equals(" ")
                && lines.get(i + 1).split("")[2].equals(" ")
                && lines.get(i + 1).split("")[3].equals(" ")) {

            String[] rar = lines.get(i + 1).split(" -> ");
            Recipe r = new Recipe(rar[1].split("@")[0], Integer.parseInt(rar[1].split("@")[1]));
            for (String s : rar[0].split(" AND "))
                r.addElement(s.split("@")[0], Integer.parseInt(s.split("@")[1]));

            recipes.add(r);
            i++;

        }

    }

    public void dispalyGUI(Player p) {

        /*
        *
        * 0  1  2  3  4  5  6  7  8
        * 9  10 11 12 13 14 15 16 17
        * 18 19 20 21 22 23 24 25 26
        * 27 28 29 30 31 32 33 34 35
        * 36 37 38 39 40 41 42 43 44
        * 45 46 47 48 49 50 51 52 53
        *
        * */

        Inventory inv = Bukkit.createInventory(p, 54, displayName);

        // stupid gray shit
        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayPaneMeta = grayPane.getItemMeta();
        grayPaneMeta.setDisplayName("");
        grayPane.setItemMeta(grayPaneMeta);



        if (pages == 1) {

            for (int i = 0; i <= 8; i++) inv.setItem(i, grayPane);
            for (int i = 9; i <= 36; i += 9) inv.setItem(i, grayPane);
            for (int i = 18; i <= 45; i += 9) inv.setItem(i - 1, grayPane);
            for (int i = 18; i <= 45; i += 9) inv.setItem(i - 3, grayPane);
            for (int i = 45; i <= 53; i++) inv.setItem(i, grayPane);

            for (int i = 0; i <= recipes.size() - 1; i++) {

                Recipe r = recipes.get(i);
                inv.setItem(16 + (9 * i), r.item);
                for (int j = 0; j <= r.elements.size() - 1; j++)
                    inv.setItem(i - (2 + j), r.elements.get(j));

            }

            p.openInventory(inv);
            return;

        }

        p.openInventory(inv);

    }

}

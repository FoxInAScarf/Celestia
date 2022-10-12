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

        Inventory inv = Bukkit.createInventory(p, 54, displayName);

        // stupid gray shit
        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayPaneMeta = grayPane.getItemMeta();
        grayPaneMeta.setDisplayName("");
        grayPane.setItemMeta(grayPaneMeta);



        if (pages == 1) {



        }

        p.openInventory(inv);

    }

}

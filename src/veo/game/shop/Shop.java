package veo.game.shop;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class Shop extends ZFile {

    String name, displayName;
    List<Recipe> recipes = new ArrayList<>();
    int pages = 1;
    Inventory inv;

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

        pages = (int) Math.ceil((double) recipes.size() / 4.0);
        if (pages == 0) pages = 1;

        build(1);

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

            String[] rar = lines.get(i + 1).replace("    ", "").split(" -> ");
            Recipe r = new Recipe(rar[1].split("@")[0], Integer.parseInt(rar[1].split("@")[1]));
            for (String s : rar[0].split(" AND "))
                r.addElement(s.split("@")[0], Integer.parseInt(s.split("@")[1]));

            recipes.add(r);
            if (i != lines.size() - 2) i++;
            else break;

        }

    }

    public void build(int currentPage) {

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

        inv = Bukkit.createInventory(null, 54, displayName);

        // stupid gray shit
        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayPaneMeta = grayPane.getItemMeta();
        grayPaneMeta.setDisplayName(" ");
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
                    inv.setItem((16 + (9 * i)) - (2 + j), r.elements.get(j));

            }

            return;

        }

        // stupid gray shit
        for (int i = 0; i <= 8; i++) inv.setItem(i, grayPane);
        for (int i = 9; i <= 17; i++) inv.setItem(i, grayPane);
        for (int i = 9; i <= 45; i += 9) inv.setItem(i, grayPane);
        for (int i = 18; i <= 54; i += 9) inv.setItem(i - 1, grayPane);
        for (int i = 18; i <= 54; i += 9) inv.setItem(i - 3, grayPane);

        // ADD ARROWS
        ItemStack left = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta lsMeta = (SkullMeta) left.getItemMeta();
        GameProfile lsProfile = new GameProfile(UUID.randomUUID(), null);
        lsProfile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQyZmRlOGI4MmU4YzFiOGMyMmIyMjY3OTk4M2ZlMzVjYjc2YTc5Nzc4NDI5YmRhZGFiYzM5N2ZkMTUwNjEifX19"));
        Field lsField;
        try {

            lsField = lsMeta.getClass().getDeclaredField("profile");
            lsField.setAccessible(true);
            lsField.set(lsMeta, lsProfile);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        { e.printStackTrace(); }
        lsMeta.setDisplayName(ChatColor.GREEN + "<- Scroll left");
        left.setItemMeta(lsMeta);

        inv.setItem(0, left);

        ItemStack right = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta rsMeta = (SkullMeta) right.getItemMeta();
        GameProfile rsProfile = new GameProfile(UUID.randomUUID(), null);
        rsProfile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA2MjYyYWYxZDVmNDE0YzU5NzA1NWMyMmUzOWNjZTE0OGU1ZWRiZWM0NTU1OWEyZDZiODhjOGQ2N2I5MmVhNiJ9fX0="));
        Field rsField;
        try {

            rsField = rsMeta.getClass().getDeclaredField("profile");
            rsField.setAccessible(true);
            rsField.set(rsMeta, rsProfile);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        { e.printStackTrace(); }
        rsMeta.setDisplayName(ChatColor.GREEN + "Scroll right ->");
        right.setItemMeta(rsMeta);

        inv.setItem(8, right);

        // ADD PAGES
        for (int i = 0; i <= pages - 1; i++) {

            ItemStack page = new ItemStack(currentPage == i + 1 ? Material.RED_STAINED_GLASS_PANE
                    : Material.LIME_STAINED_GLASS_PANE);
            ItemMeta pageMeta = page.getItemMeta();
            if (currentPage == i + 1) {

                pageMeta.setDisplayName(ChatColor.RED + "Page #" + (i + 1));
                pageMeta.setLore(Arrays.asList(ChatColor.GRAY + "Selected"));

            }
            else pageMeta.setDisplayName(ChatColor.GREEN + "Page #" + (i + 1));

            page.setItemMeta(pageMeta);
            inv.setItem(2 + i, page);

        }

        // ADD ITEMS
        int[] intrv = getIndexIntervals(currentPage);
        System.out.println(intrv[0] + " " + intrv[1]);
        for (int i = intrv[0]; i <= intrv[1]; i++) {

            Recipe r = recipes.get(i);
            int index = 25 + (9 * (i - (4 * (currentPage - 1))));
            System.out.println(index);
            inv.setItem(index, r.item);
            for (int j = 0; j <= r.elements.size() - 1; j++) // this causes problems
                inv.setItem(index - (2 + j), r.elements.get(j));

        }

    }

    private int[] getIndexIntervals(int page) {

        /*
         * page begin-end
         * 1 -> 0 - 3
         * 2 -> 4 - 7
         * 3 -> 8 - 11
         * ...
         * ..
         * .
         * 10 -> 36 - 37 END OF ARRAY
         *
         * */
        int begin = (page - 1) * 4,
                end = page * 4 - 1;
        if (end >= recipes.size() - 1) return new int[]{begin, recipes.size() - 1};
        return new int[]{begin, end};

    }

}

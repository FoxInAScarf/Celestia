package veo.game.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZItem extends ZFile {

    ItemStack item;
    String name;
    HashMap<String, String> data = new HashMap<>();

    public ZItem(File f) {

        super(f.getAbsolutePath());

        name = f.getName().replaceAll(".zra", "");
        List<ZEnchantment> e = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        for (String s : lines) if (s.contains("=")) {

            String[] ss = s.split("=");
            data.put(ss[0], ss[1]);

            if (s.equals("enchantments")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals("\t")) {

                    String[] se = lines.get(i + 1).split("@");
                    if (!lines.get(i + 1).contains("@") || se.length <= 1) {

                        error(3.1);
                        return;

                    }
                    int lvl = 0;
                    try {lvl = Integer.parseInt(se[1]); }
                    catch (Exception ignored) {

                        error(3.2);
                        return;

                    }
                    e.add(new ZEnchantment(se[0], lvl, this));
                    i++;

                }

            }
            if (s.equals("lore")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals("\t")) {

                    lore.add(lines.get(i + 1));
                    i++;

                }

            }

        }

        if (!data.containsKey("material")) {

            error(0.0);
            return;

        }
        Material m = Material.getMaterial(data.get("material").toUpperCase());
        if (m == null) {

            error(0.1);
            return;

        }
        item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();

        if (data.containsKey("name")) meta.setDisplayName(data.get("name"));
        if (lines.contains("hideAttributes")) // this might cause errors
            meta.removeItemFlags(meta.getItemFlags().toArray(new ItemFlag[0]));
        if (data.containsKey("damage")) {

            int value = 0;
            try { value = Integer.parseInt(data.get("damage")); }
            catch (Exception ignored) {

                error(1.0);
                return;

            }
            if (!(meta instanceof Damageable dm)) {

                error(1.1);
                return;

            }
            dm.setDamage(value);
            meta = dm; // this might not work

        }
        if (data.containsKey("durability")) {

            int durability = 0;
            try { durability = Integer.parseInt(data.get("durability")); }
            catch (Exception ignored) {

                if (!data.get("durability").equals("inf") && !data.get("durability").equals("infinity")) {

                    error(2.0);
                    return;

                }
                durability = -1;
                meta.setUnbreakable(true);

            }
            if (durability != -1) item.setDurability((short) durability);

        }

        for (ZEnchantment ze : e) meta.addEnchant(ze.e, ze.lvl, true);
        meta.setLore(lore);

        item.setItemMeta(meta);

    }

    public void error(double code) {

        System.out.println("[ZItemReader-ERROR]: An error has occurred while trying to read '" + name
                + "'. Error code: " + code);

    }

}

/*
*
* material=diamond_sword
* name=§2SUS SWORD
* lore
*   this
*   sword
*   is
*   very
*   sus!
* durability=inf
* damage=10
* enchantments
*   frost@2
*   sweeping_edge@2
* hideAttributes
*
* */
/*
*
* ERROR CODES:
* 0.0 - no material specified
* 0.1 - invalid material name
* 1.0 - damage value is not an integer
* 1.1 - given item is not a damageable
* 2.0 - durability value is not an integer
* 3.0 - enchantment does not exist
* 3.1 - no enchantment level provided
* 3.2 - enchantment level is not an integer
*
* */
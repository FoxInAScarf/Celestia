package veo.game.items;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.*;

public class ZItem extends ZFile {

    ItemStack item;
    String name;
    HashMap<String, String> data = new HashMap<>();

    public ZItem(File f) {

        super(f.getAbsolutePath());

        name = f.getName().replaceAll(".zra", "");
        List<ZEnchantment> e = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        for (String s : lines) {

            if (s.contains("=")) {

                String[] ss = s.split("=");
                data.put(ss[0], ss[1]);

            }

            if (s.equals("enchantments")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals(" ")
                        && lines.get(i + 1).split("")[1].equals(" ")
                        && lines.get(i + 1).split("")[2].equals(" ")
                        && lines.get(i + 1).split("")[3].equals(" ")) {

                    String[] se = lines.get(i + 1).replace("    ", "").split("@");
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

                    if (i != lines.size() - 2) i++;
                    else break;

                }

            }
            if (s.equals("lore")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals(" ")
                        && lines.get(i + 1).split("")[1].equals(" ")
                        && lines.get(i + 1).split("")[2].equals(" ")
                        && lines.get(i + 1).split("")[3].equals(" ")) {

                    lore.add(lines.get(i + 1).replace("    ", ""));

                    if (i != lines.size() - 2) i++;
                    else break;

                }

            }

        }

        if (!data.containsKey("type")) {

            error(4.0);
            return;

        }
        int type = 0;
        switch (data.get("type")) {

            case "normal":
                type = 0;
                break;

            case "sword":
                type = 1;
                break;

            case "axe":
                type = 2;
                break;

            case "bow":
                type = 3;
                break;

            case "armor":
                type = 4;
                break;

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

        if (data.containsKey("name")) {

            String name = data.get("name"),
                    color = name.split("")[0] + name.split("")[1];
            String prefix;
            switch (type) {

                case 1:
                    prefix = ChatColor.DARK_GRAY + "[" + color + "\uD83D\uDDE1" + ChatColor.DARK_GRAY + "] ";
                    break;

                case 2:
                    prefix = ChatColor.DARK_GRAY + "[" + color + "\uD83E\uDE93" + ChatColor.DARK_GRAY + "] ";
                    break;

                case 3:
                    prefix = ChatColor.DARK_GRAY + "[" + color + "\uD83C\uDFF9" + ChatColor.DARK_GRAY + "] ";
                    break;

                case 4:
                    prefix = ChatColor.DARK_GRAY + "[" + color + "\uD83D\uDEE1" + ChatColor.DARK_GRAY + "] ";
                    break;

                default:
                    prefix = "";
                    break;

            }
            meta.setDisplayName(prefix + ChatColor.RESET + name);

        }
        if (lines.contains("hideAttributes")) {

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_DYE);

        }
        if (data.containsKey("damage")) {

            int value = 0;
            try { value = Integer.parseInt(data.get("damage")); }
            catch (Exception ignored) {

                error(1.0);
                return;

            }
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

        }
        if (data.containsKey("armor")) {

            int value = 0;
            try { value = Integer.parseInt(data.get("armor")); }
            catch (Exception ignored) {

                error(1.2);
                return;

            }
            AttributeModifier headM = new AttributeModifier(UUID.randomUUID(), "generic.armor", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD);
            AttributeModifier chestM = new AttributeModifier(UUID.randomUUID(), "generic.armor", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
            AttributeModifier legsM = new AttributeModifier(UUID.randomUUID(), "generic.armor", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS);
            AttributeModifier feetM = new AttributeModifier(UUID.randomUUID(), "generic.armor", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, headM);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, chestM);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, legsM);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, feetM);

        }
        if (data.containsKey("speed")) {

            double value = 0;
            try { value = Double.parseDouble(data.get("speed")); }
            catch (Exception ignored) {

                error(1.0);
                return;

            }
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", value, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);

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

        switch (type) {

            case 1, 2 -> {
                lore.add("");
                lore.add(ChatColor.RED + "☠ " + data.get("damage") + " Attack Damage");
                lore.add(ChatColor.YELLOW + "⚔ " + data.get("speed") + " Attack Speed");
            }
            case 3 -> {
                lore.add("");
                lore.add("idfk bruh");
            }
            case 4 -> {
                lore.add("");
                lore.add(ChatColor.DARK_AQUA + "\uD83D\uDEE1 " + data.get("armor") + " Armor");
            }

        }

        for (ZEnchantment ze : e) meta.addEnchant(ze.e, ze.lvl, true);
        meta.setLore(lore);

        item.setItemMeta(meta);
        System.out.println("[ZItemReader]: Successfully imported '" + name + "'!");

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
* 1.2 - armor value is not an integer
* 2.0 - durability value is not an integer
* 3.0 - enchantment does not exist
* 3.1 - no enchantment level provided
* 3.2 - enchantment level is not an integer
* 4.0 - type not specified (normal, sword, axe, bow, armor)
*
* */
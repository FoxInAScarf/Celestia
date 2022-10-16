package veo.game.items;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import veo.essentials.zfm.ZFile;
import veo.game.custom.enchantment.ZEnchantment;

import java.io.File;
import java.util.*;
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
        List<PotionEffect> potions = new ArrayList<>();

        List<String> nLines = new ArrayList<>();
        for (String s : lines) nLines.add(s.replaceAll("&", "§"));
        lines.clear();
        lines.addAll(nLines);
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
            if (s.equals("potions")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals(" ")
                        && lines.get(i + 1).split("")[1].equals(" ")
                        && lines.get(i + 1).split("")[2].equals(" ")
                        && lines.get(i + 1).split("")[3].equals(" ")) {

                    String[] ae = lines.get(i + 1).replace("    ", "").split("@");
                    String pType = ae[0].toUpperCase();
                    int pStrength = Integer.parseInt(ae[1]),
                            pTime = Integer.parseInt(ae[2]);
                    potions.add(new PotionEffect(PotionEffectType.getByName(pType), pTime, pStrength));

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

            case "potion":
                type = 5;
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

                case 5:
                    prefix = ChatColor.DARK_GRAY + "[" + color + "⚗" + ChatColor.DARK_GRAY + "] ";
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
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

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

                error(1.3);
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
        if (data.containsKey("dye")) {

            try {

                LeatherArmorMeta laM = (LeatherArmorMeta) meta;
                laM.setColor(Color.fromBGR(Integer.parseInt(data.get("dye").split("-")[0]),
                        Integer.parseInt(data.get("dye").split("-")[1]),
                        Integer.parseInt(data.get("dye").split("-")[2])));

            } catch (Exception ignored) {

                error(4.1);
                return;

            }

        }
        if (data.containsKey("potionColor")) {

            try {

                PotionMeta pm = (PotionMeta) meta;
                String c = data.get("potionColor");
                pm.setColor(Color.fromRGB(java.awt.Color.decode(c).getRed(),
                        java.awt.Color.decode(c).getGreen(), java.awt.Color.decode(c).getBlue()));
                net.md_5.bungee.api.ChatColor cc = net.md_5.bungee.api.ChatColor.of(c);

                String finA;
                {

                    String[] a = this.data.get("name").split("§");
                    finA = a[0];
                    for (int i = 1; i <= a.length - 1; i++) {

                        String[] b = a[i].split("");
                        String finB = "";
                        for (int j = 1; j <= b.length - 1; j++) finB += b[j];
                        finA += finB;

                    }

                }
                String name = ChatColor.DARK_GRAY + "[" + cc + "⚗" + ChatColor.DARK_GRAY + "] " + cc + finA;
                pm.setDisplayName(name);
                meta = pm;

            } catch (Exception ignored) {

                error(4.2);
                return;

            }

        }

        switch (type) {

            case 1, 2 -> {
                lore.add("");
                lore.add(ChatColor.RED + "☠ " + data.get("damage") + " Attack Damage");
                lore.add(ChatColor.YELLOW + "⚔ " +
                        ((double) Math.round((Math.abs(Double.parseDouble(data.get("speed"))) - 0.8) * 10) / 10)
                        + " Attack Speed");
            }
            /*case 3 -> {
                lore.add("");
                lore.add("idfk bruh");
            }*/
            case 4 -> {
                lore.add("");
                lore.add(ChatColor.DARK_AQUA + "\uD83D\uDEE1 " + data.get("armor") + " Armor");
            }
            case 5 -> {

                lore.add("");
                for (PotionEffect pe : potions) {

                    //lore.add(ChatColor.DARK_PURPLE + "⚗");
                    String sc = pe.getType().getColor().toString().replaceAll("Color:\\[rgb0x", "#").replaceAll("]", "");
                    net.md_5.bungee.api.ChatColor color = net.md_5.bungee.api.ChatColor.of(sc);
                    String[] aa = pe.getType().getName().toLowerCase().split("_");
                    String a = "";
                    for (String as : aa) {

                        String[] ab = as.split("");
                        a += ab[0].toUpperCase();
                        for (int i = 1; i <= ab.length - 1; i++) a += ab[i];
                        a += " ";

                    }

                    String b = color + "⚗ " + a, c;
                    switch (pe.getAmplifier()) {

                        case 1 -> c = "I";
                        case 2 -> c = "II";
                        case 3 -> c = "III";
                        case 4 -> c = "IV";
                        case 5 -> c = "V";
                        default -> c = "" + pe.getAmplifier();

                    }
                    b += c;

                    String d = " (" + (int) Math.floor((double) pe.getDuration() / (20 * 60))
                            + ":" + ((pe.getDuration() / 20) % 60) + ")";
                    b += d;
                    lore.add(b);

                }

            }

        }

        for (ZEnchantment ze : e) {

            if (ze.isCustom) {

                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.a(ze.eName, "");
                nmsItem.b(nbt);
                item = CraftItemStack.asBukkitCopy(nmsItem);
                continue;

            }
            meta.addEnchant(ze.e, ze.lvl, true);

        }
        meta.setLore(lore);
        for (PotionEffect pe : potions) try {

            PotionMeta pm = (PotionMeta) meta;
            pm.addCustomEffect(pe, true);
            meta = pm;

        } catch (Exception ignored) {

            error(4.2);
            return;

        }

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
* 1.3 - speed value is not a double
* 2.0 - durability value is not an integer
* 3.0 - enchantment does not exist
* 3.1 - no enchantment level provided
* 3.2 - enchantment level is not an integer
* 4.0 - type not specified (normal, sword, axe, bow, armor)
* 4.1 - item is not dyeable
* 4.2 - item cannot pick up potion effects
*
* */
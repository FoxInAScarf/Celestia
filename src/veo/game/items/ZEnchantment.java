package veo.game.items;


import org.bukkit.enchantments.Enchantment;

public class ZEnchantment {

    public boolean isCustom;
    public String eName;
    public Enchantment e;
    public int lvl;

    public ZEnchantment(String eName, int lvl, ZItem i) {

        this.eName = eName.toUpperCase();
        if (this.eName.equals("FROST")) {

            isCustom = true;
            return;

        }
        e = Enchantment.getByName(this.eName);
        this.lvl = lvl;

        if (e == null) i.error(3.0);


    }

}

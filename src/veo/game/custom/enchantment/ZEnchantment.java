package veo.game.custom.enchantment;


import org.bukkit.enchantments.Enchantment;
import veo.game.items.ZItem;

public class ZEnchantment {

    public boolean isCustom;
    public String eName;
    public Enchantment e;
    public int lvl;

    public ZEnchantment(String eName, int lvl, ZItem i) {

        this.eName = eName.toUpperCase();
        if (this.eName.equals("FROSTBITE")) {

            isCustom = true;
            return;

        }
        e = Enchantment.getByName(this.eName);
        this.lvl = lvl;

        if (e == null) i.error(3.0);


    }

}

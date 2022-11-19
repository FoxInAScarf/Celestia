package veo.game.custom.enchantment;

import org.bukkit.enchantments.Enchantment;

public class ZEnchantment {

    public Enchantment e;
    public int lvl;

    public ZEnchantment(String name, int lvl) {

        e = Enchantment.getByName(name.toUpperCase());
        if (e == null) e = CustomEnchantments.getByName(name);
        if (e == null) System.out.println("[ZCUSTOMENCHANTS]: ERROR!");
        this.lvl = lvl;

    }

}

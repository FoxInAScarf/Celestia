package veo.game.custom.enchantment;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomEnchantments {

    // https://www.youtube.com/watch?v=wd1FUOT-BJY
    // thx for this! ^^

    public static final Enchantment FROSTBITE = new EnchantmentWrapper("FROSTBITE", 1);
    public static final Enchantment CRUX = new EnchantmentWrapper("CRUX", 1);
    public static final Enchantment MURDER = new EnchantmentWrapper("MURDER", 1);

    static List<Enchantment> customEnchantments = new ArrayList<>();
    static {

        customEnchantments.add(FROSTBITE);
        customEnchantments.add(CRUX);
        customEnchantments.add(MURDER);

    }

    public static void register() {

        boolean registeredFrostbite = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(FROSTBITE);
        if (!registeredFrostbite) registerEnchantments(FROSTBITE);

        boolean registeredCrux = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(CRUX);
        if (!registeredCrux) registerEnchantments(CRUX);

        boolean registeredMurder = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(MURDER);
        if (!registeredMurder) registerEnchantments(MURDER);

    }

    public static void registerEnchantments(Enchantment e) {

        try {

            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(e);

        } catch (Exception ignored) {}

    }

    public static Enchantment getByName(String n) {

        for (Enchantment e : customEnchantments) if (e.getName().equalsIgnoreCase(n)) return e;
        return null;

    }

}

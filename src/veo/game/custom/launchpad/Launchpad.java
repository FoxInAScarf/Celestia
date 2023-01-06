package veo.game.custom.launchpad;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import veo.Main;

public class Launchpad {

    Location l;
    Vector v;
    String name;
    private ArmorStand base, middle;

    public Launchpad(String name, Location l, Vector v) {

        this.l = l;
        this.v = v;
        this.name = name;

    }

    public Launchpad build() {

        base = (ArmorStand) l.getWorld().spawnEntity(l.clone().subtract(0, 1.5 + 0.45, 0), EntityType.ARMOR_STAND);
        base.setMarker(true);
        base.setInvulnerable(true);
        base.setInvisible(true);
        base.setGravity(false);
        base.setHelmet(new ItemStack(Material.GREEN_WOOL));
        base.addScoreboardTag("removable-" + Main.removableTag);

        middle = (ArmorStand) l.getWorld().spawnEntity(l.clone().subtract(0, 0.2 + 0.45, 0), EntityType.ARMOR_STAND);
        middle.setMarker(true);
        middle.setInvulnerable(true);
        middle.setInvisible(true);
        middle.setGravity(false);
        middle.setSmall(true);
        middle.setHelmet(new ItemStack(Material.WHITE_CARPET));
        middle.addScoreboardTag("removable-" + Main.removableTag);

        return this;

    }

    public void yeet(Player p) {

        if (p.getLocation().distance(l) > 1) return;

        p.setVelocity(v);
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 10, 10);
        base.setHelmet(new ItemStack(Material.RED_WOOL));
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> base.setHelmet(new ItemStack(Material.GREEN_WOOL)), 10L);

    }

    public void remove() {

        base.remove();
        middle.remove();
        LaunchpadManager.launchpads.remove(this);

    }

}

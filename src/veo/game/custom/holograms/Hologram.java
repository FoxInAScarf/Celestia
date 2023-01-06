package veo.game.custom.holograms;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import veo.Main;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hologram extends ZFile {

    HashMap<String, String> data = new HashMap<>();
    List<ArmorStand> armorStands = new ArrayList<>();
    String name;

    public Hologram(File f) {

        /*
         *
         * world=world
         * x=14.5
         * y=52.6
         * z=2.0
         * text
         *     Hi!
         *     I am -HEX-#6fferv-HEX-gay!
         *     UwU
         *     And am going to suck your &ccock~
         *
         * */

        super(f.getAbsolutePath());
        name = f.getName().replaceAll(".zra", "");

        List<String> nLines = new ArrayList<>();
        for (String s : lines) nLines.add(s.replaceAll("&", "§"));
        lines.clear();
        lines.addAll(nLines);

        List<String> text = new ArrayList<>();

        for (String s : lines) {

            if (s.contains("=")) {

                String[] ss = s.split("=");
                data.put(ss[0], ss[1]);

            }

            if (s.equals("text")) {

                int i = lines.indexOf(s);
                while (lines.get(i + 1).split("")[0].equals(" ")
                        && lines.get(i + 1).split("")[1].equals(" ")
                        && lines.get(i + 1).split("")[2].equals(" ")
                        && lines.get(i + 1).split("")[3].equals(" ")) {

                    text.add(buildHexIntoString(lines.get(i + 1)).replaceAll("    ", ""));
                    System.out.println(buildHexIntoString(s));

                    if (i != lines.size() - 2) i++;
                    else break;

                }

            }

        }

        for (int i = 0; i <= text.size() - 1; i++) {

            String c = text.get(i);

            if (c.equals("break")) continue;

            Location l = new Location(Bukkit.getWorld(data.get("world")), Double.parseDouble(data.get("x")), Double.parseDouble(data.get("y")) + 0.2 * i, Double.parseDouble(data.get("z")));
            ArmorStand stand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
            stand.setInvisible(true);
            stand.setMarker(true);
            stand.addScoreboardTag("removable-" + Main.removableTag);
            stand.setGravity(false);
            stand.setCustomName(c);
            stand.setCustomNameVisible(true);

            armorStands.add(stand);

        }

    }

    private String buildHexIntoString(String s) {

        String[] ss = s.split("#");
        String finalString = ss[0];
        for (int i = 1; i <= ss.length - 1; i++) {

            String[] letters = ss[i].split("");
            String hex = "#";
            String string = "";
            for (int j = 0; j <= 5; j++) hex += letters[j];
            for (int k = 6; k <= letters.length - 1; k++) string += letters[k];
            finalString += ChatColor.of(hex) + string;

        }

        return finalString;

    }

}

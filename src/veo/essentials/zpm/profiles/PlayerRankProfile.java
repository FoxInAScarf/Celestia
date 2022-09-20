package com.celestia.veo.essentials.zpm.profiles;

import com.celestia.veo.essentials.zpm.Profile;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.ArrayList;

public class PlayerRankProfile extends Profile {

    public static boolean isAdmin = false;
    public static String rankName = ChatColor.GRAY + "Default";
    public static ChatColor textColor = ChatColor.GRAY;

    public PlayerRankProfile(File f) {

        super(f);

        for (String s : lines) {

            s = s.replaceAll(" ", "");
            String[] ss = s.split(":");
            switch (ss[0]) {

                case "rank":
                    rankName = ss[1];
                    break;

                case "color":
                    textColor = ChatColor.getByChar(ss[1].replaceAll("§", ""));
                    break;

                case "isAdmin":
                    isAdmin = Boolean.parseBoolean(ss[1]);

            }

        }

    }



    public PlayerRankProfile reset() {

        lines = new ArrayList<>();

        clear();
        save();
        addLine("rank: §7Default");
        addLine("color: §7");
        addLine("isAdmin: false");
        save();

        return this;

    }

    public void setRankName(String rn) {

        rankName = rn;
        saveF();

    }

    public void setTextColor(ChatColor c) {

        textColor = c;
        saveF();

    }

    public void setAdmin(boolean v) {

        isAdmin = v;
        saveF();

    }

    public void saveF() {

        lines = new ArrayList<>();

        clear();
        save();
        addLine("rank: " + rankName);
        addLine("color: §" + textColor.getChar());
        addLine("isAdmin: " + isAdmin);
        save();

    }

}

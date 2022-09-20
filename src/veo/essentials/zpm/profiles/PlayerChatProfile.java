package veo.essentials.zpm.profiles;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import veo.essentials.zpm.Profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerChatProfile extends Profile {

    public boolean isChatEnabled = true,
            showChatPrefix = true,
            isFilterEnabled = true;
    public List<Player> blockedPlayers = new ArrayList<>();

    public PlayerChatProfile(File f) {

        super(f);
        for (int i = 0; i <= lines.size() - 1; i++) {

            String s = lines.get(i);
            s = s.replaceAll(" ", "");
            s = s.replaceAll("\t", "@");
            String[] ss = s.split(":");
            switch (ss[0]) {

                case "chatEnabled":
                    isChatEnabled = Boolean.parseBoolean(ss[1]);
                    break;

                case "showPrefix":
                    showChatPrefix = Boolean.parseBoolean(ss[1]);
                    break;

                case "filterEnabled":
                    isFilterEnabled = Boolean.parseBoolean(ss[1]);

                case "blockedUsers":
                    {

                        int i1 = i + 1;
                        while (lines.size() - 1 >= i1)
                            if (lines.get(i1).split("")[0].equals("@")) {

                                UUID uuid = UUID.fromString(lines.get(i1).replaceAll("@", ""));
                                blockedPlayers.add(Bukkit.getPlayer(uuid));
                                i1++;

                            } else break;

                    }

                    break;

            }

        }

    }

    public void showChatPrefix(boolean v) {

        showChatPrefix = v;
        saveF();

    }

    public void enableChat(boolean v) {

        isChatEnabled = v;
        saveF();

    }

    public void enableFilter(boolean v) {

        isFilterEnabled = v;
        saveF();

    }

    public void enablePrefix(boolean v) {

        showChatPrefix = v;
        saveF();

    }

    public void addBlockedPlayer(Player p) {

        blockedPlayers.add(p);
        saveF();

    }

    public void removeBlockedPlayer(Player p) {

        blockedPlayers.remove(p);
        saveF();

    }

    public PlayerChatProfile reset() {

        lines = new ArrayList<>();

        clear();
        save();
        addLine("isChatEnabled: true");
        addLine("showPrefix: true");
        addLine("filterEnabled: true");
        addLine("blockedUsers:");
        save();

        return this;

    }


    public void saveF() {

        lines = new ArrayList<>();

        clear();
        save();
        addLine("isChatEnabled: " + isChatEnabled);
        addLine("showPrefix: " + showChatPrefix);
        addLine("filterEnabled: " + isFilterEnabled);
        addLine("blockedUsers:");
        for (Player p : blockedPlayers)
            addLine("\t" + p.getUniqueId());
        save();

    }

}

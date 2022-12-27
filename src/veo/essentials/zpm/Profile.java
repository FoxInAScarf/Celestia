package veo.essentials.zpm;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import veo.essentials.zfm.ZFile;

import java.io.File;
import java.util.UUID;

public class Profile extends ZFile {

    public OfflinePlayer p;

    public Profile(File f) {

        super(f.getAbsolutePath());

        UUID uuid = UUID.fromString(f.getName());
        p = Bukkit.getOfflinePlayer(uuid);

    }

    public OfflinePlayer getPlayer() { return p; }
    public UUID getUUID() { return p.getUniqueId(); }

}

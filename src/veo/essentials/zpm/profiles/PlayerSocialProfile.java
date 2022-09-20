package veo.essentials.zpm.profiles;

import org.bukkit.entity.Player;
import veo.essentials.zpm.Profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerSocialProfile extends Profile {

    List<Player> friends = new ArrayList<>();

    public PlayerSocialProfile(File f) {

        super(f);

    }

}

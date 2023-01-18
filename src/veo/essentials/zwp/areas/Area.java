package veo.essentials.zwp.areas;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Area {

    int type;
    /*
    *
    * 0 = noPVPArea
    * 1 =
    *
    * */
    Location a, b;

    public Area(int type, Location a, Location b) {

        this.type = type;
        this.a = a;
        this.b = b;


    }

}

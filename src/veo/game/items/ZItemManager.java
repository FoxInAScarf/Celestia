package veo.game.items;

import veo.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZItemManager {

    static List<ZItem> items = new ArrayList<>();
    static String folder;

    public static void init() {

        folder = Main.mainFolder.getAbsolutePath() + "/Items";
        if (!new File(folder).exists()) new File(folder).mkdir();

        load();
        Main.getInstance().getCommand("zitem").setExecutor(new ZItemCommand());

    }

    public static void load() {

        items.clear();
        for (File f : Objects.requireNonNull(new File(folder).listFiles())) items.add(new ZItem(f));

    }

    public static ZItem getItem(String name) {

        return null;

    }

}

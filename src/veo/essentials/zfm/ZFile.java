package veo.essentials.zfm;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ZFile {

    public List<String> lines = new ArrayList<>();
    public File file;

    public ZFile(String path) {

        try {

            file = new File(path);
            if (file.isDirectory() || !file.exists()) file.createNewFile();

            read();

        } catch (Exception ignored) {}

    }

    public void clear() {

        try {

            file.delete();
            file.createNewFile();

        } catch (Exception ignored) {}

    }

    public List<String> read() {

        try {

            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {

                String l = s.nextLine();
                if (!l.equals("") && l != null) lines.add(l);

            }

        } catch (Exception ignored) {}

        return lines;

    }

    public void save() {

        try {

            clear();

            FileWriter w = new FileWriter(file);
            for (String s : lines) {

                w.write(s);
                w.write("\n");

            }
            w.close();

        } catch (Exception ignored) {}

    }

    public void addLine(String line) {

        lines.add(line);
        save();

    }

    public void removeLine(String line) {

        lines.remove(line);
        save();

    }

    public void clearAll() {

        lines.clear();
        clear();

    }

}

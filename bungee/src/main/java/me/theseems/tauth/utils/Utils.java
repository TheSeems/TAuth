package me.theseems.tauth.utils;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;

public class Utils {
    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);

            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File loadResource(Plugin plugin, String name) throws IOException {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        File config = new File(plugin.getDataFolder(), name);
        if (!config.exists()) {
            config.createNewFile();
            copy(plugin.getResourceAsStream(name), config);
        }
        return config;
    }
}

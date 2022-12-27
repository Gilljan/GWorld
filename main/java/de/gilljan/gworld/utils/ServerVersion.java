package de.gilljan.gworld.utils;

import org.bukkit.Bukkit;

public class ServerVersion {


    public static String getMinecraftVersion() {
        String version = Bukkit.getServer().getVersion();
        int start = version.indexOf("MC: ") + 4;
        int end = version.length() - 1;
        return version.substring(start, end);
    }
}

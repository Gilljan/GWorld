package de.gilljan.gworld.utils;

import de.gilljan.gworld.Main;

public class SendMessage_util {
    public static String sendMessage(String key) {
        return Main.getConfigs().get("language").getString(key)
                .replaceAll("&0", "§0")
                .replaceAll("&1", "§1")
                .replaceAll("&2", "§2")
                .replaceAll("&3", "§3")
                .replaceAll("&4", "§4")
                .replaceAll("&5", "§5")
                .replaceAll("&6", "§6")
                .replaceAll("&7", "§7")
                .replaceAll("&8", "§8")
                .replaceAll("&9", "§9")
                .replaceAll("&a", "§a")
                .replaceAll("&b", "§b")
                .replaceAll("&c", "§c")
                .replaceAll("&d", "§d")
                .replaceAll("&e", "§e")
                .replaceAll("&f", "§f")
                .replaceAll("&l", "§l")
                .replaceAll("&m", "§m")
                .replaceAll("&n", "§n")
                .replaceAll("&o", "§o")
                .replaceAll("&u", "§u")
                .replaceAll("&r", "§r")
                .replaceAll("&k", "§k");
    }
}

/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.utils;

import de.gilljan.gworld.Main;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

public class GeneratorUtil {
    public static void getGenerators() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            try {
                if (plugin.isEnabled()) {
                    ChunkGenerator generator = plugin.getDefaultWorldGenerator("", "");
                    if (generator != null) {
                        Main.availableGenerators.add(plugin.getDescription().getName());
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

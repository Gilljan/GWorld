/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.api.GWorldAPI;
import de.gilljan.gworld.api.IGWorldAPI;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

public class LoadWorld_listener implements Listener {

    @EventHandler
    public void onLoadWorld(WorldLoadEvent event) {
        if (Main.getConfigs().get("config").getBoolean("AutoImport")) {
            String worldName = event.getWorld().getName();
            if (!Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(event.getWorld()) && Main.getConfigs().get("worlds").get("Worlds." + worldName) == null) {
                GWorldAPI api = new GWorldAPI(worldName);
                String generator = null;
                for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
                    ChunkGenerator plC = pl.getDefaultWorldGenerator("", "");
                    if (plC != null && pl.isEnabled() && event.getWorld().getGenerator() != null) {
                        if (plC.getClass().equals(event.getWorld().getGenerator().getClass())) {
                            generator = pl.getName();
                        }
                    }
                }
                switch (event.getWorld().getWorldType()) {
                    case AMPLIFIED:
                        api.importExisting(IGWorldAPI.WorldType.AMPLIFIED, generator);
                        break;
                    case FLAT:
                        api.importExisting(IGWorldAPI.WorldType.FLAT, generator);
                        break;
                    case NORMAL:
                        api.importExisting(IGWorldAPI.WorldType.NORMAL, generator);
                        break;
                    case LARGE_BIOMES:
                        api.importExisting(IGWorldAPI.WorldType.LARGE_BIOMES, generator);
                        break;
                }
                if (event.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    api.importExisting(IGWorldAPI.WorldType.NETHER, generator);
                } else if (event.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                    api.importExisting(IGWorldAPI.WorldType.END, generator);
                }
                Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("AutoImport").replaceAll("%world%", worldName));
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.hasPermission("Gworld.notify")) {
                        all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("AutoImport").replaceAll("%world%", worldName));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInitWorld(WorldInitEvent ev) {
        ev.getWorld().setKeepSpawnInMemory(false);
    }
}

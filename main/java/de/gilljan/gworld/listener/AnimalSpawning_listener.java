/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class AnimalSpawning_listener implements Listener {
    @EventHandler
    public void onAnimalSpawn(CreatureSpawnEvent event) {
        String world = event.getEntity().getLocation().getWorld().getName();
        if (Main.getMapinfos().get(world) != null) {
            String serverVersion = ServerVersion.getMinecraftVersion();
            int version = Integer.parseInt(serverVersion.split("\\.")[1]);
            switch (version) {
                case 8:
                case 9:
                case 10:
                case 11:
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube || event.getEntity() instanceof EnderDragon) {
                        if (!Main.getMapinfos().get(world).isMobSpawning()) {
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat || event.getEntity() instanceof Villager) {
                        if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                            event.setCancelled(true);
                        }
                    }
                    break;
                case 12:
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube || event.getEntity() instanceof Shulker || event.getEntity() instanceof EnderDragon) {
                        if (!Main.getMapinfos().get(world).isMobSpawning()) {
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat
                            || event.getEntity() instanceof Villager) {
                        if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                            event.setCancelled(true);
                        }
                    }
                    break;
                case 13:
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube || event.getEntity() instanceof Shulker || event.getEntity() instanceof EnderDragon) {
                        if (!Main.getMapinfos().get(world).isMobSpawning()) {
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat || event.getEntity() instanceof Fish
                            || event.getEntity() instanceof Dolphin || event.getEntity() instanceof Villager) {
                        if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                            event.setCancelled(true);
                        }
                    }
                    break;
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube || event.getEntity() instanceof Shulker || event.getEntity() instanceof EnderDragon) {
                        if (!Main.getMapinfos().get(world).isMobSpawning()) {
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat || event.getEntity() instanceof Fish
                            || event.getEntity() instanceof Dolphin || event.getEntity() instanceof Villager
                            || event.getEntity() instanceof WanderingTrader) {
                        if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                            event.setCancelled(true);
                        }
                    }
                    break;
                case 19:
                    if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube || event.getEntity() instanceof Shulker || event.getEntity() instanceof EnderDragon) {
                        if (!Main.getMapinfos().get(world).isMobSpawning()) {
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat || event.getEntity() instanceof Fish
                            || event.getEntity() instanceof Dolphin || event.getEntity() instanceof Villager
                            || event.getEntity() instanceof WanderingTrader || event.getEntity() instanceof Allay) {
                        if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                            event.setCancelled(true);
                        }
                    }
                    break;
                default:
                    Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + serverVersion);
                    break;
            }
        }
    }
}

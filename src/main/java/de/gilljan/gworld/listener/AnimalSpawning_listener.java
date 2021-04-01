/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class AnimalSpawning_listener implements Listener {
    @EventHandler
    public void onAnimalSpawn(CreatureSpawnEvent event) {
        String world = event.getEntity().getLocation().getWorld().getName();
        if (Main.getMapinfos().get(world) != null) {
            if (event.getEntity() instanceof Monster || event.getEntity() instanceof Slime || event.getEntity() instanceof MagmaCube) {
                if (!Main.getMapinfos().get(world).isMobSpawning()) {
                    event.setCancelled(true);
                }
            } else if (event.getEntity() instanceof Animals || event.getEntity() instanceof Squid || event.getEntity() instanceof Bat) {
                if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}

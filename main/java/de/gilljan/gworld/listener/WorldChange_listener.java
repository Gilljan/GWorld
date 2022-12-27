/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange_listener implements Listener {
    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        if (Main.loadedWorlds.contains(p.getWorld().getName())) {
            if (Main.getMapinfos().get(p.getWorld().getName()).isForcedGamemode()) {
                p.setGameMode(GameMode.valueOf(Main.getMapinfos().get(p.getWorld().getName()).getDefaultGamemode().toUpperCase()));
            }
        }
    }
}

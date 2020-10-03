/*
 * Copyright (c) Gilljan 2020. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin_listener implements Listener {
    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        MapInformation mapInformation = Main.getMapinfos().get(p.getWorld().getName());
        if (mapInformation.isForcedGamemode())
            p.setGameMode(GameMode.valueOf(mapInformation.getDefaultGamemode()));
    }
}

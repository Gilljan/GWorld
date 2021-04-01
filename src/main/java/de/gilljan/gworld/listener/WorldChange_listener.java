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
        if(Main.loadedWorlds.contains(p.getWorld().getName())) {
            if (Main.getMapinfos().get(p.getWorld().getName()).isForcedGamemode()) {
                p.setGameMode(GameMode.valueOf(Main.getMapinfos().get(p.getWorld().getName()).getDefaultGamemode().toUpperCase()));
            }
        }
    }

    /*
    @EventHandler
    public void WeatherChange(WeatherChangeEvent event) {
        if(Main.getMapinfos().containsKey(event.getWorld().getName())) {
            if(!Main.getMapinfos().get(event.getWorld().getName()).isWeatherCycle()) {
                String weather = Main.getMapinfos().get(event.getWorld().getName()).getDefaultWeather();
                if(weather.equalsIgnoreCase("storm")) {
                    event.getWorld().setThundering(true);
                    event.getWorld().setWeatherDuration(1000000);
                } else if(weather.equalsIgnoreCase("sun")) {
                    event.getWorld().setThundering(false);
                    event.getWorld().setStorm(false);
                    event.getWorld().setWeatherDuration(1000000);
                } else if(weather.equalsIgnoreCase("rain")) {
                    event.getWorld().setStorm(true);
                    event.getWorld().setWeatherDuration(1000000);
                }
            }
        }
    }

     */
}

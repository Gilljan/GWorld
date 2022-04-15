/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.listener;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerJoin_listener implements Listener {
    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (Main.loadedWorlds.contains(p.getWorld().getName())) {
            MapInformation mapInformation = Main.getMapinfos().get(p.getWorld().getName());
            if (mapInformation.isForcedGamemode())
                p.setGameMode(GameMode.valueOf(mapInformation.getDefaultGamemode()));
        }


        //UpdateNotification
        if (event.getPlayer().hasPermission("GWorld.updateNotification")) {
            if (Main.getConfigs().get("config").getBoolean("UpdateNotification")) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL("https://gilljan.de/versions/GWorld/index.html").openConnection();
                        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        String version = reader.readLine();
                        String[] sVersions = version.split("\\.");
                        int one = Integer.parseInt(sVersions[0]);
                        int two = Integer.parseInt(sVersions[1]);
                        int three = Integer.parseInt(sVersions[2]);
                        String[] pVersions = Main.getInstance().getDescription().getVersion().split("\\.");
                        int pOne = Integer.parseInt(pVersions[0]);
                        int pTwo = Integer.parseInt(pVersions[1]);
                        int pThree = Integer.parseInt(pVersions[2]);
                        if (one > pOne) {
                            event.getPlayer().sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("UpdateNotification").replaceAll("%version%", version).replaceAll("%link%", "https://gworld.gilljan.de"));
                        } else if (two > pTwo && one == pOne) {
                            event.getPlayer().sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("UpdateNotification").replaceAll("%version%", version).replaceAll("%link%", "https://gworld.gilljan.de"));
                        } else if (three > pThree && one == pOne && two == pTwo) {
                            event.getPlayer().sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("UpdateNotification").replaceAll("%version%", version).replaceAll("%link%", "https://gworld.gilljan.de"));
                        }
                        connection.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        }
    }
}

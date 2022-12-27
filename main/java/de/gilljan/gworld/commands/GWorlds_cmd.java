/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.SendMessage_util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GWorlds_cmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gworlds")) {
            if (sender.hasPermission("Gworld.worlds")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args.length == 0) {
                        if (Main.loadedWorlds.size() != 0) {
                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Worlds.loadedMaps"));
                            Collections.sort(Main.loadedWorlds);
                            for (String maps : Main.loadedWorlds) {
                                if (Bukkit.getWorlds().contains(Bukkit.getWorld(maps))) {
                                    TextComponent tc = new TextComponent();
                                    tc.setText(" &7- &a".replaceAll("&", "§") + maps);
                                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtp " + maps));
                                    p.spigot().sendMessage(tc);
                                }
                            }
                        } else
                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Worlds.noMapsloaded"));
                    }
                } else {
                    if (args.length == 0) {
                        if (Main.loadedWorlds.size() != 0) {
                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Worlds.loadedMaps"));
                            Collections.sort(Main.loadedWorlds);
                            for (String maps : Main.loadedWorlds) {
                                if (Bukkit.getWorlds().contains(Bukkit.getWorld(maps))) {
                                    sender.sendMessage(" &7- &a".replaceAll("&", "§") + maps);
                                }
                            }
                        } else
                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Worlds.noMapsloaded"));
                    }
                }
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }
}

/*
 * Copyright (c) Gilljan 2020. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GInfo_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ginfo")) {
            if (sender.hasPermission("Gworld.info")) {
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        String world = p.getWorld().getName();
                        if (Bukkit.getWorld(world) != null && Main.loadedWorlds.contains(world)) {
                            MapInformation mapInformation = Main.getMapinfos().get(world);
                            sender.sendMessage(SendMessage_util.sendMessage("Info.header"));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.name") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", world));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.type") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getType())));
                            if (mapInformation.isDayNight()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.timeCycle") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.timeCycle") + SendMessage_util.sendMessage("Info.flags.false"));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.time") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultTime())));
                            if (mapInformation.isWeatherCycle()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weatherCycle") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weatherCycle") + SendMessage_util.sendMessage("Info.flags.false"));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weather") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultWeather())));
                            if (mapInformation.isEnablePVP()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.pvp") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.pvp") + SendMessage_util.sendMessage("Info.flags.false"));
                            if (mapInformation.isMobSpawning()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.mobs") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.mobs") + SendMessage_util.sendMessage("Info.flags.false"));
                            if (mapInformation.isAnimalSpawning()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.animals") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.animals") + SendMessage_util.sendMessage("Info.flags.false"));
                            if (mapInformation.isForcedGamemode()) {
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.forcedGamemode") + SendMessage_util.sendMessage("Info.flags.true"));
                            } else
                                sender.sendMessage(SendMessage_util.sendMessage("Info.flags.forcedGamemode") + SendMessage_util.sendMessage("Info.flags.false"));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.defaultGamemode") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultGamemode())));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.difficulty") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDifficulty())));
                            sender.sendMessage(SendMessage_util.sendMessage("Info.footer"));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Info.failed_world"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Info.use"));
                } else if (args.length == 1) {
                    String world = args[0];
                    if (Bukkit.getWorld(world) != null && Main.loadedWorlds.contains(world)) {
                        MapInformation mapInformation = Main.getMapinfos().get(world);
                        sender.sendMessage(SendMessage_util.sendMessage("Info.header"));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.name") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", world));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.flags.type") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getType())));
                        if (mapInformation.isDayNight()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.timeCycle") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.timeCycle") + SendMessage_util.sendMessage("Info.flags.false"));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.flags.time") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultTime())));
                        if (mapInformation.isWeatherCycle()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weatherCycle") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weatherCycle") + SendMessage_util.sendMessage("Info.flags.false"));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.flags.weather") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultWeather())));
                        if (mapInformation.isEnablePVP()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.pvp") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.pvp") + SendMessage_util.sendMessage("Info.flags.false"));
                        if (mapInformation.isMobSpawning()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.mobs") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.mobs") + SendMessage_util.sendMessage("Info.flags.false"));
                        if (mapInformation.isAnimalSpawning()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.animals") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.animals") + SendMessage_util.sendMessage("Info.flags.false"));
                        if (mapInformation.isForcedGamemode()) {
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.forcedGamemode") + SendMessage_util.sendMessage("Info.flags.true"));
                        } else
                            sender.sendMessage(SendMessage_util.sendMessage("Info.flags.forcedGamemode") + SendMessage_util.sendMessage("Info.flags.false"));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.flags.defaultGamemode") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDefaultGamemode())));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.flags.difficulty") + SendMessage_util.sendMessage("Info.flags.values").replaceAll("%value%", String.valueOf(mapInformation.getDifficulty())));
                        sender.sendMessage(SendMessage_util.sendMessage("Info.footer"));
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Info.failed").replaceAll("%world%", world));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Info.use"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("ginfo")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.info"))))
            return null;
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            List<String> options = new ArrayList<String>();
            for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
                options.add(Bukkit.getWorlds().get(i).getName());
            }
            String search = args[0].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        }
        return tab;
    }

}



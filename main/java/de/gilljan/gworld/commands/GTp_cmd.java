/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GTp_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gtp")) {
            switch (args.length) {
                case 1:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        String worldName = args[0];
                        if (Bukkit.getWorld(worldName) != null) {
                            if (p.hasPermission("Gworld.teleport.*") || p.hasPermission("Gworld.teleport." + worldName)) {
                                Location loc = new Location(Bukkit.getWorld(worldName), Bukkit.getWorld(worldName).getSpawnLocation().getX(), Bukkit.getWorld(worldName).getSpawnLocation().getY(), Bukkit.getWorld(worldName).getSpawnLocation().getZ());
                                p.teleport(loc);
                                p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.success").replaceAll("%world%", worldName));

                            } else p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
                        } else
                            p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.failed").replaceAll("%world%", worldName));
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.failed_console"));
                    break;
                case 2:
                    if (sender.hasPermission("Gworld.teleport.other")) {
                        String worldName = args[0];
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null && target.isOnline()) {
                            if (Bukkit.getWorld(worldName) != null) {
                                Location loc = new Location(Bukkit.getWorld(worldName), Bukkit.getWorld(worldName).getSpawnLocation().getX(), Bukkit.getWorld(worldName).getSpawnLocation().getY(), Bukkit.getWorld(worldName).getSpawnLocation().getZ());
                                target.teleport(loc);
                                target.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.player_target_success").replaceAll("%world%", worldName).replaceAll("%player%", sender.getName()));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.sender_target_success").replaceAll("%world%", worldName).replaceAll("%player%", target.getDisplayName()));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.failed").replaceAll("%world%", worldName));
                        }
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
                    break;
                default:
                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.use"));
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gtp")) || (args.length == 0))
            return null;
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("GWorld.teleport.*")) {
                List<String> options = new ArrayList<String>();
                for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
                    options.add(Bukkit.getWorlds().get(i).getName());
                }
                String search = args[0].toLowerCase();
                for (String option : options) {
                    if (option.toLowerCase().startsWith(search))
                        tab.add(option);
                }
            } else {
                List<String> options = new ArrayList<String>();
                for (World world : Bukkit.getWorlds()) {
                    if (sender.hasPermission("Gworld.teleport." + world.getName())) {
                        options.add(world.getName());
                    }
                }
                String search = args[0].toLowerCase();
                for (String option : options) {
                    if (option.toLowerCase().startsWith(search))
                        tab.add(option);
                }
            }
        }

        if (args.length == 2 && sender.hasPermission("Gworld.teleport.other")) {
            List<String> options = new ArrayList<String>();
            for (Player players : Bukkit.getOnlinePlayers()) {
                options.add(players.getName());
            }

            String search = args[1].toLowerCase();
            for (String option : options) {
                if (option.toLowerCase().startsWith(search))
                    tab.add(option);
            }
        }
        return tab;
    }
}

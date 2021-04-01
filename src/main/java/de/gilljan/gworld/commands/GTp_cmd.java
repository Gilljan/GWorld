/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("Gworld.teleport")) {
                    if (args.length == 1) {
                        String worldName = args[0];
                        if (Bukkit.getWorld(worldName) != null) {
                            Location loc = new Location(Bukkit.getWorld(worldName), Bukkit.getWorld(worldName).getSpawnLocation().getX(), Bukkit.getWorld(worldName).getSpawnLocation().getY(), Bukkit.getWorld(worldName).getSpawnLocation().getZ());
                            p.teleport(loc);
                            p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.success").replaceAll("%world%", worldName));
                        } else
                            p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.failed").replaceAll("%world%", worldName));
                    } else p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.use"));
                } else p.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Teleport.failed_console"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gtp")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.teleport"))))
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

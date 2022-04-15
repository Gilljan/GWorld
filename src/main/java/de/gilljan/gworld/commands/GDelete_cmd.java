/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.SendMessage_util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GDelete_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gdelete")) {
            if (sender.hasPermission("Gworld.delete")) {
                if (args.length == 1) {
                    String worldName = args[0];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gdelete " + worldName + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.confirm_console"));
                } else if (args.length == 2) {
                    String worldName = args[0];
                    String confirm = args[1];
                    if (confirm.equalsIgnoreCase("confirm")) {
                        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            if (Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && Main.loadedWorlds.contains(worldName)) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (Bukkit.getWorld(worldName).getEntities().contains(all)) {
                                        all.teleport(Bukkit.getWorld(Main.getConfigs().get("config").getString("MainWorld")).getSpawnLocation());
                                        all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.teleport_players").replaceAll("%world%", worldName));
                                    }
                                }
                                Bukkit.getWorlds().remove(Bukkit.getWorld(worldName));
                                Bukkit.unloadWorld(Bukkit.getWorld(worldName), true);
                                Main.loadedWorlds.remove(worldName);
                                Main.getMapinfos().remove(worldName);
                                Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                                File world = new File(Bukkit.getWorldContainer(), worldName);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.success").replaceAll("%world%", worldName));
                                Main.getConfigs().get("worlds").getConfigurationSection("Worlds").set(worldName, null);
                                try {
                                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                                    FileUtils.deleteDirectory(world);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.failed").replaceAll("%world%", worldName));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Delete.use"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gdelete")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.delete"))))
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

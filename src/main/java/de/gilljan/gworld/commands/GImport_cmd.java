/*
 * Copyright (c) Gilljan 2020. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GImport_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gimport")) {
            if (sender.hasPermission("Gworld.import")) {
                if (args.length == 2) {
                    String worldName = args[0];
                    String type = args[1];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gimport " + worldName + " " + type + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.confirm_console"));
                } else if (args.length == 3) {
                    String worldName = args[0];
                    String type = args[1];
                    String confirm = args[2];
                    File file = new File(Bukkit.getWorldContainer(), worldName);
                    if (confirm.equalsIgnoreCase("confirm")) {
                        if(!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            if (file.exists()) {
                                if (!Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && !Main.loadedWorlds.contains(worldName)) {
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.creating").replaceAll("%world%", worldName));
                                    if (type.equalsIgnoreCase("normal")) {
                                        createWorld(sender, worldName, type, WorldType.NORMAL, null);
                                    } else if (type.equalsIgnoreCase("end")) {
                                        createWorld(sender, worldName, type, null, World.Environment.THE_END);
                                    } else if (type.equalsIgnoreCase("amplified")) {
                                        createWorld(sender, worldName, type, WorldType.AMPLIFIED, null);
                                    } else if (type.equalsIgnoreCase("nether")) {
                                        createWorld(sender, worldName, type, null, World.Environment.NETHER);
                                    } else if (type.equalsIgnoreCase("flat")) {
                                        createWorld(sender, worldName, type, WorldType.FLAT, null);
                                    } else if (type.equalsIgnoreCase("large_biomes")) {
                                        createWorld(sender, worldName, type, WorldType.LARGE_BIOMES, null);
                                    } else
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.failed").replaceAll("%world%", worldName));
                                } else if (Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && !Main.loadedWorlds.contains(worldName)) {
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.creating").replaceAll("%world%", worldName));
                                    if (type.equalsIgnoreCase("normal")) {
                                        MapInformation.createMapInfos(worldName, "normal");
                                    } else if (type.equalsIgnoreCase("end")) {
                                        MapInformation.createMapInfos(worldName, "end");
                                    } else if (type.equalsIgnoreCase("amplified")) {
                                        MapInformation.createMapInfos(worldName, "amplified");
                                    } else if (type.equalsIgnoreCase("nether")) {
                                        MapInformation.createMapInfos(worldName, "nether");
                                    } else if (type.equalsIgnoreCase("flat")) {
                                        MapInformation.createMapInfos(worldName, "flat");
                                    } else if (type.equalsIgnoreCase("large_biomes")) {
                                        MapInformation.createMapInfos(worldName, "large_biomes");
                                    } else
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.failed").replaceAll("%world%", worldName));
                                    Main.loadedWorlds.add(worldName);
                                    Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                                    try {
                                        Main.getConfigs().get("worlds").save(Main.getWorlds());
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.success").replaceAll("%world%", worldName));
                                } else
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.failed").replaceAll("%world%", worldName));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.failed").replaceAll("%world%", worldName));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.use"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }

    public static void createWorld(CommandSender sender, String worldName, String type, WorldType worldType, World.Environment environment) {
        MapInformation.createMapInfos(worldName, type);
        WorldCreator w = WorldCreator.name(worldName);
        if (worldType == null) {
            w.environment(environment);
        } else if (environment == null) {
            w.type(worldType);
        }
        File target = new File(Bukkit.getWorldContainer(), worldName);
        try {
            new File(target, "uid.dat").delete();
        } catch (Exception ex) {

        }
        Bukkit.createWorld(w);
        Bukkit.getWorlds().add(Bukkit.getWorld(worldName));
        Main.loadedWorlds.add(worldName);
        Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
        try {
            Main.getConfigs().get("worlds").save(Main.getWorlds());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Import.success").replaceAll("%world%", worldName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gimport")) || (args.length == 0) || (!(sender.hasPermission("Gworld.import"))))
            return null;
        List<String> tab = new ArrayList<>();
        List<String> options = new ArrayList<String>();

        if (args.length == 1) {
            for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
                options.add(Bukkit.getWorlds().get(i).getName());
            }
            String search = args[0].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        } else if (args.length == 2) {
            options.add("normal");
            options.add("end");
            options.add("nether");
            options.add("flat");
            options.add("amplified");
            options.add("large_biomes");
            String search = args[1].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        }
        return tab;
    }
}

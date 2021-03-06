/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
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

public class GCreate_cmdBackup1 {
    /*
    private static Long value1;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gcreate")) {
            if (sender.hasPermission("Gworld.create")) {
                if (args.length == 2) {
                    String worldName = args[0];
                    String type = args[1];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gcreate " + worldName + " " + type + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.confirm_console"));
                } else if (args.length == 3) {
                    String worldName = args[0];
                    File world = new File(Bukkit.getWorldContainer(), worldName);
                    String type = args[1];
                    String value = args[2];
                    try {
                        value1 = Long.parseLong(args[2]);
                    } catch (NumberFormatException | NullPointerException ignored) {
                    }
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        if (value.equalsIgnoreCase("confirm") && value1 == null) {
                            if(!worldName.contains(".") & !worldName.contains("/")  & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                                if (!world.exists()) {
                                    if (type != null) {
                                        if (type.equalsIgnoreCase("normal")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.NORMAL;
                                            w.type(wT);
                                            MapInformation.createMapInfos(worldName, "normal");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("end")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            w.environment(World.Environment.THE_END);
                                            MapInformation.createMapInfos(worldName, "end");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("nether")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            w.environment(World.Environment.NETHER);
                                            MapInformation.createMapInfos(worldName, "nether");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("amplified")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.AMPLIFIED;
                                            w.type(wT);
                                            MapInformation.createMapInfos(worldName, "ampliefied");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("large_biomes")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.LARGE_BIOMES;
                                            w.type(wT);
                                            MapInformation.createMapInfos(worldName, "large_biomes");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("flat")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.FLAT;
                                            w.type(wT);
                                            MapInformation.createMapInfos(worldName, "flat");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.use"));
                                    } else
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.use"));
                                    if (!Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                        Bukkit.getWorlds().add(Bukkit.getWorld(worldName));
                                    }
                                    if (!Main.loadedWorlds.contains(worldName)) {
                                        Main.loadedWorlds.add(worldName);
                                        Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                                        try {
                                            Main.getConfigs().get("worlds").save(Main.getWorlds());
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                } else
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.failed").replaceAll("%world%", worldName));
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                        } else if (value1 != null) {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.confirm_player"));
                                TextComponent tc = new TextComponent();
                                tc.setText("§a[Confirm]");
                                tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gcreate " + worldName + " " + type + " " + value1 + " confirm"));
                                p.spigot().sendMessage(tc);
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.confirm_console"));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.use"));
                    });
                } else if (args.length == 4) {
                    String worldName = args[0];
                    File world = new File(Bukkit.getWorldContainer(), worldName);
                    String type = args[1];
                    String sValue;
                    try {
                        value1 = Long.parseLong(args[2]);
                    } catch (NumberFormatException | NullPointerException ex) {

                    }
                    String value2 = args[3];
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        if (value2.equalsIgnoreCase("confirm")) {
                            if(!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                                if (!world.exists()) {
                                    if (type != null) {
                                        if (type.equalsIgnoreCase("normal")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.NORMAL;
                                            w.type(wT);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "normal");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("end")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            w.environment(World.Environment.THE_END);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "end");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("nether")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            w.environment(World.Environment.NETHER);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "nether");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("amplified")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.AMPLIFIED;
                                            w.type(wT);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "ampliefied");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("large_biomes")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.LARGE_BIOMES;
                                            w.type(wT);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "large_biomes");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else if (type.equalsIgnoreCase("flat")) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.creating").replaceAll("%world%", worldName));
                                            WorldCreator w = WorldCreator.name(worldName);
                                            WorldType wT = WorldType.FLAT;
                                            w.type(wT);
                                            w.seed(value1);
                                            MapInformation.createMapInfos(worldName, "flat");
                                            Bukkit.createWorld(w);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.success").replaceAll("%world%", worldName));
                                        } else
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.use"));
                                    } else
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.use"));
                                    if (!Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                        Bukkit.getWorlds().add(Bukkit.getWorld(worldName));
                                    }
                                    if (!Main.loadedWorlds.contains(worldName)) {
                                        Main.loadedWorlds.add(worldName);
                                        Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                                        try {
                                            Main.getConfigs().get("worlds").save(Main.getWorlds());
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                        } else
                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Create.failed").replaceAll("%world%", worldName));
                    });
                } else if(args.length == )

            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gcreate")) || (args.length == 0) || (!(sender.hasPermission("Gworld.Create"))))
            return null;
        List<String> tab = new ArrayList<>();

        if (args.length == 2) {
            List<String> options = new ArrayList<String>();
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
    */
}

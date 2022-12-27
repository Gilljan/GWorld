/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GReCreate_cmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("grecreate")) {
            if (sender.hasPermission("Gworld.recreate")) {
                if (args.length == 2) {
                    String worldName = args[0];
                    String keepSaved = args[1];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/grecreate " + worldName + " " + keepSaved + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.confirm_console"));
                } else if (args.length == 3) {
                    String worldName = args[0];
                    String confirmation = args[2];
                    File world = new File(Bukkit.getWorldContainer(), worldName);
                    String keepSaved = args[1];
                    long seed;
                    ChunkGenerator generator;
                    if (confirmation.equalsIgnoreCase("confirm")) {
                        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            if (world.exists()) {
                                if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                    seed = Bukkit.getWorld(worldName).getSeed();
                                    generator = Bukkit.getWorld(worldName).getGenerator();
                                    if (keepSaved.equalsIgnoreCase("false")) {
                                        for (Player all : Bukkit.getOnlinePlayers()) {
                                            if (Bukkit.getWorld(worldName).getEntities().contains(all)) {
                                                all.teleport(Bukkit.getWorld(Main.getConfigs().get("config").getString("MainWorld")).getSpawnLocation());
                                                all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.teleport_players").replaceAll("%world%", worldName));
                                            }
                                        }
                                        Bukkit.unloadWorld(worldName, true);
                                        try {
                                            FileUtils.deleteDirectory(world);
                                        } catch (IOException e) {
                                            try {
                                                File dir = new File("old_Maps//");
                                                if (!dir.exists()) {
                                                    dir.mkdir();
                                                }
                                                File target = new File("old_Maps//" + worldName + "-" + new SimpleDateFormat("dd.MM.yyyy - HH-mm-ss").format(new Date()));
                                                FileUtils.moveDirectory(world, target);
                                                recreateMap(worldName, seed, generator);
                                            } catch (IOException ex) {
                                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.failed"));
                                            }
                                        }
                                        recreateMap(worldName, seed, generator);
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.success").replaceAll("%world%", worldName));
                                    } else {
                                        for (Player all : Bukkit.getOnlinePlayers()) {
                                            if (Bukkit.getWorld(worldName).getEntities().contains(all)) {
                                                all.teleport(Bukkit.getWorld(Main.getConfigs().get("config").getString("MainWorld")).getSpawnLocation());
                                                all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.teleport_players").replaceAll("%world%", worldName));
                                            }
                                        }
                                        Bukkit.unloadWorld(worldName, true);
                                        File dir = new File("old_Maps//");
                                        if (!dir.exists()) {
                                            dir.mkdir();
                                        }
                                        File target = new File("old_Maps//" + worldName + "-" + new SimpleDateFormat("dd.MM.yyyy - HH-mm-ss").format(new Date()));
                                        try {
                                            FileUtils.moveDirectory(world, target);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.creating").replaceAll("%world%", worldName));
                                            recreateMap(worldName, seed, generator);
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.success").replaceAll("%world%", worldName));
                                        } catch (Exception e) {
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.failed").replaceAll("%world%", worldName));
                                        }
                                    }
                                }
                            }
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Recreate.use"));
            }
        }
        return false;
    }

    private void recreateMap(String name, long seed, ChunkGenerator generator) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            String type = Main.getMapinfos().get(name).getType();
            WorldCreator worldCreator = WorldCreator.name(name);
            WorldType worldType;
            switch (type) {
                case "normal":
                    worldType = WorldType.NORMAL;
                    worldCreator.type(worldType);
                    break;
                case "amplified":
                    worldType = WorldType.AMPLIFIED;
                    worldCreator.type(worldType);
                    break;
                case "large_biomes":
                    worldType = WorldType.LARGE_BIOMES;
                    worldCreator.type(worldType);
                    break;
                case "flat":
                    worldType = WorldType.FLAT;
                    worldCreator.type(worldType);
                    break;
                case "nether":
                    worldCreator.environment(World.Environment.NETHER);
                    break;
                case "end":
                    worldCreator.environment(World.Environment.THE_END);
                    break;
                default:
                    break;
            }
            worldCreator.seed(seed);
            worldCreator.generator(generator);
            Bukkit.createWorld(worldCreator);
            MapInformation.setMapValues(name);
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("grecreate")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.recreate"))))
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
            options.add("true");
            options.add("false");
            String search = args[1].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        }

        return tab;
    }
}
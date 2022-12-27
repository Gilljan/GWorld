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
import java.util.ArrayList;
import java.util.List;

public class GLoad_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gload")) {
            if (sender.hasPermission("gworld.load")) {
                if (args.length == 1) {
                    String worldName = args[0];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gload " + worldName + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.confirm_console"));
                } else if (args.length == 2) {
                    String worldName = args[0];
                    if (args[1].equalsIgnoreCase("confirm")) {
                        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            if (!Main.loadedWorlds.contains(worldName) && !Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                try {
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.loading").replaceAll("%world%", worldName));
                                    if (Main.getConfigs().get("worlds").get("Worlds." + worldName) != null && new File(Bukkit.getWorldContainer(), worldName).exists()) {
                                        Main.getMapinfos().put(worldName, new MapInformation(
                                                Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".generator"),
                                                Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".type"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".mobs"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".animals"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".weatherCycle"),
                                                Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".weather"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".timeCycle"),
                                                Main.getConfigs().get("worlds").getLong("Worlds." + worldName + ".time"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".pvp"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".forcedGamemode"),
                                                Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".defaultGamemode"),
                                                Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".difficulty"),
                                                Main.getConfigs().get("worlds").getInt("Worlds." + worldName + ".randomTickSpeed"),
                                                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".announceAdvancements")
                                        ));
                                        WorldCreator w = new WorldCreator(worldName);
                                        if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("normal")) {
                                            w.type(WorldType.NORMAL);
                                        } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("end")) {
                                            w.environment(World.Environment.THE_END);
                                        } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("amplified")) {
                                            w.type(WorldType.AMPLIFIED);
                                        } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("nether")) {
                                            w.environment(World.Environment.NETHER);
                                        } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("flat")) {
                                            w.type(WorldType.FLAT);
                                        } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("large_biomes")) {
                                            w.type(WorldType.LARGE_BIOMES);
                                        } else w.type(WorldType.NORMAL);
                                        if (!Main.getMapinfos().get(worldName).getGenerator().equalsIgnoreCase("null")) {
                                            w.generator(Main.getMapinfos().get(worldName).getGenerator());
                                        }
                                        Bukkit.createWorld(w);
                                        Main.loadedWorlds.add(worldName);
                                        Main.getConfigs().get("worlds").save(Main.getWorlds());
                                        MapInformation.setMapValues(worldName);
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.success").replaceAll("%world%", worldName));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.failed").replaceAll("%world%", worldName));
                                }
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.alreadyLoaded").replaceAll("%world%", worldName));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.use"));
            }
        }
        return false;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gload")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.load"))))
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


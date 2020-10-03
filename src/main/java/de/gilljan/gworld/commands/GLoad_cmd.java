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
import org.bukkit.entity.Player;

import java.io.File;

public class GLoad_cmd implements CommandExecutor {
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
                } else if(args.length == 2) {
                    String worldName = args[0];
                    if(args[1].equalsIgnoreCase("confirm")) {
                        if(!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            try {
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.loading").replaceAll("%world%", worldName));
                                if(Main.getConfigs().get("worlds").get("Worlds." + worldName) != null && new File(Bukkit.getWorldContainer(), worldName).exists()) {
                                    Main.getMapinfos().put(worldName, new MapInformation(
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
                                            Main.getConfigs().get("worlds").getString("Worlds." + worldName + ".difficulty")
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
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Load.use"));
            }
        }
        return false;
    }
}


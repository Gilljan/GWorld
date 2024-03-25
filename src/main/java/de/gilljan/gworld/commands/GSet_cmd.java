/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import de.gilljan.gworld.utils.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GSet_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gset")) {
            if (sender.hasPermission("Gworld.set")) {
                if (args.length == 2 || args.length == 3) {
                    String world;
                    String type;
                    String value;
                    if(args.length == 2) {
                        if(sender instanceof Player) {
                            world = ((Player) sender).getWorld().getName();
                            type = args[0];
                            value = args[1];
                        } else {
                            sender.sendMessage(SendMessage_util.sendMessage("Set.use"));
                            return true;
                        }
                    } else {
                        world = args[0];
                        type = args[1];
                        value = args[2];
                    }
                    if (Bukkit.getWorld(world) != null && Main.loadedWorlds.contains(world)) {
                        MapInformation mapInformation = Main.getMapinfos().get(world);
                        if (type.equalsIgnoreCase("timecycle")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setDayNight(true);
                                Bukkit.getWorld(world).setGameRuleValue("doDaylightCycle", "true");
                                sender.sendMessage(SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.timeCycle")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setDayNight(false);
                                Bukkit.getWorld(world).setTime(mapInformation.getDefaultTime());
                                Bukkit.getWorld(world).setGameRuleValue("doDaylightCycle", "false");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.timeCycle")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("time")) {
                            try {
                                long i = Long.parseLong(value);
                                if (i <= 24000 && i >= 0) {
                                    mapInformation.setDefaultTime(i);
                                    if (!mapInformation.isDayNight())
                                        Bukkit.getWorld(world).setTime(i);
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.time")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + i));
                                } else
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                            } catch (NumberFormatException nfe) {
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                            }
                        } else if (type.equalsIgnoreCase("weathercycle")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setWeatherCycle(true);
                                Bukkit.getWorld(world).setGameRuleValue("doWeatherCycle", "true");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.weatherCycle")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setWeatherCycle(false);
                                Bukkit.getWorld(world).setGameRuleValue("doWeatherCycle", "false");
                                String weather = mapInformation.getDefaultWeather();
                                if (weather.equalsIgnoreCase("sun")) {
                                    Bukkit.getWorld(world).setStorm(false);
                                    Bukkit.getWorld(world).setThundering(false);
                                } else if (weather.equalsIgnoreCase("rain")) {
                                    Bukkit.getWorld(world).setStorm(true);
                                    Bukkit.getWorld(world).setThundering(false);
                                } else if (weather.equalsIgnoreCase("storm")) {
                                    Bukkit.getWorld(world).setStorm(true);
                                    Bukkit.getWorld(world).setThundering(true);
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.weatherCycle")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("weather")) {
                            if (value.equalsIgnoreCase("sun")) {
                                if (!mapInformation.isWeatherCycle()) {
                                    Bukkit.getWorld(world).setStorm(false);
                                    Bukkit.getWorld(world).setThundering(false);
                                }
                                mapInformation.setDefaultWeather("sun");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.weather")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "sun"));
                            } else if (value.equalsIgnoreCase("storm")) {
                                if (!mapInformation.isWeatherCycle()) {
                                    Bukkit.getWorld(world).setStorm(true);
                                    Bukkit.getWorld(world).setThundering(true);
                                }
                                mapInformation.setDefaultWeather("storm");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.weather")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "storm"));
                            } else if (value.equalsIgnoreCase("rain")) {
                                if (!mapInformation.isWeatherCycle()) {
                                    Bukkit.getWorld(world).setStorm(true);
                                    Bukkit.getWorld(world).setThundering(false);
                                }
                                mapInformation.setDefaultWeather("rain");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.weather")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "rain"));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("pvp")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setEnablePVP(true);
                                Bukkit.getWorld(world).setPVP(true);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.pvp")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setEnablePVP(false);
                                Bukkit.getWorld(world).setPVP(false);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.pvp")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("mobs")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setMobSpawning(true);
                                Bukkit.getWorld(world).setGameRuleValue("doMobSpawning", "true");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.mobs")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setMobSpawning(false);
                                Bukkit.getWorld(world).setGameRuleValue("doMobSpawning", "false");
                                for (Entity mobs : Bukkit.getWorld(world).getEntities()) {
                                    switch (Main.getServerversion()) {
                                        case 8:
                                        case 9:
                                        case 10:
                                        case 11:
                                            if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof EnderDragon) {
                                                if (!Main.getMapinfos().get(world).isMobSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        case 12:
                                        case 13:
                                        case 14:
                                        case 15:
                                        case 16:
                                        case 17:
                                        case 18:
                                        case 19:
                                        case 20:
                                            if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof Shulker || mobs instanceof EnderDragon) {
                                                if (!Main.getMapinfos().get(world).isMobSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        default:
                                            Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                                            sender.sendMessage(Main.getPrefix() + "§4Unsupported Version: §e" + Main.getFullServerversion());
                                            return false;
                                    }

                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.mobs")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("animals")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setAnimalSpawning(true);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.animals")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setAnimalSpawning(false);
                                for (Entity mobs : Bukkit.getWorld(world).getEntities()) {
                                    switch (Main.getServerversion()) {
                                        case 8:
                                        case 9:
                                        case 10:
                                        case 11:
                                        case 12:
                                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Villager) {
                                                if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        case 13:
                                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                                    || mobs instanceof Dolphin || mobs instanceof Villager) {
                                                if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        case 14:
                                        case 15:
                                        case 16:
                                        case 17:
                                        case 18:
                                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                                    || mobs instanceof Dolphin || mobs instanceof Villager
                                                    || mobs instanceof WanderingTrader) {
                                                if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        case 19:
                                        case 20:
                                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                                    || mobs instanceof Dolphin || mobs instanceof Villager
                                                    || mobs instanceof WanderingTrader || mobs instanceof Allay) {
                                                if (!Main.getMapinfos().get(world).isAnimalSpawning()) {
                                                    mobs.remove();
                                                }
                                            }
                                            break;
                                        default:
                                            Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                                            sender.sendMessage(Main.getPrefix() + "§4Unsupported Version: §e" + Main.getFullServerversion());
                                            return false;
                                    }

                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.animals")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("forcedgamemode")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setForceGamemode(true);
                                for (Player all : Bukkit.getWorld(world).getPlayers()) {
                                    try {
                                        if (mapInformation.getDefaultGamemode().equalsIgnoreCase("survival")) {
                                            all.setGameMode(GameMode.SURVIVAL);
                                        } else if (mapInformation.getDefaultGamemode().equalsIgnoreCase("creative")) {
                                            all.setGameMode(GameMode.CREATIVE);
                                        } else if (mapInformation.getDefaultGamemode().equalsIgnoreCase("spectator")) {
                                            all.setGameMode(GameMode.SPECTATOR);
                                        } else if (mapInformation.getDefaultGamemode().equalsIgnoreCase("adventure")) {
                                            all.setGameMode(GameMode.ADVENTURE);
                                        }
                                    } catch (Exception ex) {
                                        Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + world + " §cis not right. Please use creative, survival, adventure or spectator");
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                                    }
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.forcedGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setForceGamemode(false);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.forcedGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else if (type.equalsIgnoreCase("defaultGamemode")) {
                            if (value.equalsIgnoreCase("creative")) {
                                mapInformation.setDefaultGamemode("creative");
                                if (mapInformation.isForcedGamemode()) {
                                    for (Player all : Bukkit.getWorld(world).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.CREATIVE);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + world + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                                        }
                                    }
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.defaultGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "creative"));
                            } else if (value.equalsIgnoreCase("survival")) {
                                mapInformation.setDefaultGamemode("survival");
                                if (mapInformation.isForcedGamemode()) {
                                    for (Player all : Bukkit.getWorld(world).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.SURVIVAL);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + world + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                                        }
                                    }
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.defaultGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "survival"));
                            } else if (value.equalsIgnoreCase("spectator")) {
                                mapInformation.setDefaultGamemode("spectator");
                                if (mapInformation.isForcedGamemode()) {
                                    for (Player all : Bukkit.getWorld(world).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.SPECTATOR);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + world + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                                        }
                                    }
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.defaultGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "spectator"));
                            } else if (value.equalsIgnoreCase("adventure")) {
                                mapInformation.setDefaultGamemode("adventure");
                                if (mapInformation.isForcedGamemode()) {
                                    for (Player all : Bukkit.getWorld(world).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.ADVENTURE);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + world + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                                        }
                                    }
                                }
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.defaultGamemode")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "adventure"));
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.use"));
                        } else if (type.equalsIgnoreCase("difficulty")) {
                            if (value.equalsIgnoreCase("peaceful")) {
                                mapInformation.setDifficulty("peaceful");
                                Bukkit.getWorld(world).setDifficulty(Difficulty.PEACEFUL);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.difficulty")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "peaceful"));
                            } else if (value.equalsIgnoreCase("easy")) {
                                mapInformation.setDifficulty("easy");
                                Bukkit.getWorld(world).setDifficulty(Difficulty.EASY);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.difficulty")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "easy"));
                            } else if (value.equalsIgnoreCase("normal")) {
                                mapInformation.setDifficulty("normal");
                                Bukkit.getWorld(world).setDifficulty(Difficulty.NORMAL);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.difficulty")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "normal"));
                            } else if (value.equalsIgnoreCase("hard")) {
                                mapInformation.setDifficulty("hard");
                                Bukkit.getWorld(world).setDifficulty(Difficulty.HARD);
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.difficulty")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + "hard"));
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.use"));
                        } else if (type.equalsIgnoreCase("randomtickspeed")) {
                            try {
                                int rTS = Integer.parseInt(value);
                                mapInformation.setRandomTickSpeed(rTS);
                                Bukkit.getWorld(world).setGameRuleValue("randomTickSpeed", String.valueOf(rTS));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.announceAdvancements")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.values") + rTS));
                            } catch (NumberFormatException ex) {
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                            }
                        } else if(type.equalsIgnoreCase("announceAdvancements")) {
                            if (value.equalsIgnoreCase("true")) {
                                mapInformation.setAnnounceAdvancements(true);
                                Bukkit.getWorld(world).setGameRuleValue("announceAdvancements", "true");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.announceAdvancements")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.true")));
                            } else if (value.equalsIgnoreCase("false")) {
                                mapInformation.setAnnounceAdvancements(false);
                                Bukkit.getWorld(world).setGameRuleValue("announceAdvancements", "false");
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.success").replaceAll("%world%", world));
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.changes").replaceAll("%flag%", SendMessage_util.sendMessage("Set.flags.announceAdvancements")).replaceAll("%value%", SendMessage_util.sendMessage("Set.flags.false")));
                            } else
                                sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.use"));

                        {
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".timeCycle", mapInformation.isDayNight());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".time", mapInformation.getDefaultTime());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".weatherCycle", mapInformation.isWeatherCycle());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".weather", mapInformation.getDefaultWeather());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".pvp", mapInformation.isEnablePVP());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".mobs", mapInformation.isMobSpawning());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".animals", mapInformation.isAnimalSpawning());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".forcedGamemode", mapInformation.isForcedGamemode());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".defaultGamemode", mapInformation.getDefaultGamemode());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".difficulty", mapInformation.getDifficulty());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".randomTickSpeed", mapInformation.getRandomTickSpeed());
                            Main.getConfigs().get("worlds").set("Worlds." + world + ".announceAdvancements", mapInformation.isAnnounceAdvancements());
                            try {
                                Main.getConfigs().get("worlds").save(Main.getWorlds());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", world));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.use"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gset")) || (args.length == 0) || (!(sender.hasPermission("Gworld.set"))))
            return null;
        List<String> tab = new ArrayList<>();
        List<String> options = new ArrayList<String>();
        if (args.length == 1) {
            if(sender instanceof Player) {
                listFlags(options);
            }

            for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
                options.add(Bukkit.getWorlds().get(i).getName());
            }

            String search = args[0].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        } else if (args.length == 2) {
            if(sender instanceof Player) {
                String type = args[0];
                listOptions(options, type);
            }

            listFlags(options);

            String search = args[1].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        } else if (args.length == 3) {


            String type = args[1];
            listOptions(options, type);

            String search = args[2].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        }
        return tab;
    }

    private void listFlags(List<String> options) {
        options.add("timeCycle");
        options.add("time");
        options.add("weatherCycle");
        options.add("weather");
        options.add("pvp");
        options.add("mobs");
        options.add("animals");
        options.add("forcedGamemode");
        options.add("defaultGamemode");
        options.add("difficulty");
        options.add("randomTickSpeed");
        options.add("announceAdvancements");
    }

    private void listOptions(List<String> options, String type) {
        switch (type) {
            case "timeCycle":
            case "weatherCycle":
            case "pvp":
            case "mobs":
            case "animals":
            case "announceAdvancements":
            case "forcedGamemode":
                options.add("true");
                options.add("false");
                break;
            case "difficulty":
                options.add("peaceful");
                options.add("easy");
                options.add("normal");
                options.add("hard");
                break;
            case "defaultGamemode":
                options.add("survival");
                options.add("creative");
                options.add("spectator");
                options.add("adventure");
                break;
            case "weather":
                options.add("sun");
                options.add("rain");
                options.add("storm");
                break;
            default:
                break;
        }
    }
}

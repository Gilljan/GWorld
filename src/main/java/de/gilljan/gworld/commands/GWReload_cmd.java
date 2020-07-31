package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import java.io.File;

public class GWReload_cmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gwreload")) {
            if (sender.hasPermission("Gworld.reload")) {
                if (args.length == 0) {
                    try {
                        Main.getConfigs().remove("worlds");
                        Main.getConfigs().put("worlds", YamlConfiguration.loadConfiguration(Main.getWorlds()));
                        Main.loadedWorlds.clear();
                        for (int i = 0; i < Main.getConfigs().get("worlds").getStringList("LoadWorlds").size(); i++) {
                            Main.loadedWorlds.add(Main.getConfigs().get("worlds").getStringList("LoadWorlds").get(i));
                        }
                        Main.getMapinfos().clear();
                        for (int i = 0; i < Main.loadedWorlds.size(); i++) {
                            Main.getMapinfos().put(Main.loadedWorlds.get(i), new MapInformation(
                                    Main.getConfigs().get("worlds").getString("Worlds." + Main.loadedWorlds.get(i) + ".type"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".mobs"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".animals"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".weatherCycle"),
                                    Main.getConfigs().get("worlds").getString("Worlds." + Main.loadedWorlds.get(i) + ".weather"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".timeCycle"),
                                    Main.getConfigs().get("worlds").getLong("Worlds." + Main.loadedWorlds.get(i) + ".time"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".pvp"),
                                    Main.getConfigs().get("worlds").getBoolean("Worlds." + Main.loadedWorlds.get(i) + ".forcedGamemode"),
                                    Main.getConfigs().get("worlds").getString("Worlds." + Main.loadedWorlds.get(i) + ".defaultGamemode"),
                                    Main.getConfigs().get("worlds").getString("Worlds." + Main.loadedWorlds.get(i) + ".difficulty")
                            ));
                            WorldCreator w = WorldCreator.name(Main.loadedWorlds.get(i));
                            Bukkit.createWorld(w);
                            if (!Main.getMapinfos().get(Main.loadedWorlds.get(i)).isMobSpawning()) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doMobSpawning", "false");
                                for (Entity mobs : Bukkit.getWorld(Main.loadedWorlds.get(i)).getEntities()) {
                                    if (mobs instanceof Monster || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof Ghast) {
                                        mobs.remove();
                                    }
                                }
                            } else {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doMobSpawning", "true");
                            }
                            if (!Main.getMapinfos().get(Main.loadedWorlds.get(i)).isAnimalSpawning()) {
                                for (Entity mobs : Bukkit.getWorld(Main.loadedWorlds.get(i)).getEntities()) {
                                    if (mobs instanceof Animals || mobs instanceof Squid) {
                                        mobs.remove();
                                    }
                                }
                            }
                            if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).isForcedGamemode()) {
                                if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("creative")) {
                                    for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.CREATIVE);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", Main.loadedWorlds.get(i)));
                                        }
                                    }
                                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("survival")) {
                                    for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.SURVIVAL);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", Main.loadedWorlds.get(i)));
                                        }
                                    }
                                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("spectator")) {
                                    for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.SPECTATOR);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", Main.loadedWorlds.get(i)));
                                        }
                                    }
                                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("adventure")) {
                                    for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                                        try {
                                            all.setGameMode(GameMode.ADVENTURE);
                                        } catch (Exception ex) {
                                            Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Set.failed").replaceAll("%world%", Main.loadedWorlds.get(i)));
                                        }

                                    }
                                }
                            }
                            if (!Main.getMapinfos().get(Main.loadedWorlds.get(i)).isDayNight()) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doDaylightCycle", "false");
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setTime(Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultTime());
                            } else {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doDaylightCycle", "true");
                            }
                            if (!Main.getMapinfos().get(Main.loadedWorlds.get(i)).isWeatherCycle()) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doWeatherCycle", "false");
                                if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("sun")) {
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setStorm(false);
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setThundering(false);
                                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("rain")) {
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setStorm(true);
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setThundering(false);
                                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("storm")) {
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setStorm(true);
                                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setThundering(true);
                                }
                            } else {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setGameRuleValue("doWeatherCycle", "true");
                            }
                            Bukkit.getWorld(Main.loadedWorlds.get(i)).setPVP(Main.getMapinfos().get(Main.loadedWorlds.get(i)).isEnablePVP());
                            if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("peaceful")) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(Difficulty.PEACEFUL);
                            } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("easy")) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(Difficulty.EASY);
                            } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("normal")) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(Difficulty.NORMAL);
                            } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("hard")) {
                                Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(Difficulty.HARD);
                            }
                            if (!Main.getMapinfos().get(Main.loadedWorlds.get(i)).isAnimalSpawning()) {
                                for (Entity mobs : Bukkit.getWorld(Main.loadedWorlds.get(i)).getEntities()) {
                                    if (mobs instanceof Animals) {
                                        mobs.remove();
                                    }
                                }
                            }
                        }
                        Main.getConfigs().remove("language");
                        Main.getConfigs().remove("config");
                        Main.getConfigs().put("config", YamlConfiguration.loadConfiguration(Main.getConfiguration()));
                        if (Main.getConfigs().get("config").getString("Language").equalsIgnoreCase("de")) {
                            Main.getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder().getPath() + "//de_DE.yml")));
                        } else if (Main.getConfigs().get("config").getString("Language").equalsIgnoreCase("en")) {
                            Main.getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder().getPath() + "//en_EN.yml")));
                        } else if (Main.getConfigs().get("config").getString("Language").equalsIgnoreCase("fr")) {
                            Main.getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder().getPath() + "//fr_FR.yml")));
                        }
                        Main.setPrefix(Main.getConfigs().get("config").getString("Prefix")
                                .replaceAll("&0", "§0")
                                .replaceAll("&1", "§1")
                                .replaceAll("&2", "§2")
                                .replaceAll("&3", "§3")
                                .replaceAll("&4", "§4")
                                .replaceAll("&5", "§5")
                                .replaceAll("&6", "§6")
                                .replaceAll("&7", "§7")
                                .replaceAll("&8", "§8")
                                .replaceAll("&9", "§9")
                                .replaceAll("&a", "§a")
                                .replaceAll("&b", "§b")
                                .replaceAll("&c", "§c")
                                .replaceAll("&d", "§d")
                                .replaceAll("&e", "§e")
                                .replaceAll("&f", "§f")
                                .replaceAll("&l", "§l")
                                .replaceAll("&m", "§m")
                                .replaceAll("&n", "§n")
                                .replaceAll("&o", "§o")
                                .replaceAll("&u", "§u")
                                .replaceAll("&r", "§r")
                                .replaceAll("&k", "§k"));
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Reload.success"));
                    } catch (Exception ex) {
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Reload.failed"));
                        ex.printStackTrace();
                    }
                } else
                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Reload.use")));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }
}

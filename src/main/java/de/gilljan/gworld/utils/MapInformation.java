/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.utils;

import de.gilljan.gworld.Main;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import java.io.IOException;

public class MapInformation {
    private final String generator;
    private final String type;
    private boolean mobSpawning;
    private boolean animalSpawning;
    private boolean weatherCycle;
    private String defaultWeather;
    private boolean dayNightCycle;
    private long defaultTime;
    private boolean enablePVP;
    private boolean forcedGamemode;
    private String defaultGamemode;
    private String difficulty;
    private int randomTickSpeed;

    private boolean announceAdvancements;

    public MapInformation(String generator, String type, boolean mobSpawning, boolean animalSpawning, boolean weatherCycle, String defaultWeather, boolean dayNightCycle, long defaultTime, boolean enablePVP, boolean forcedGamemode, String defaultGamemode, String difficulty, int randomTickSpeed, boolean announceAdvancements) {
        this.generator = generator;
        this.type = type;
        this.mobSpawning = mobSpawning;
        this.animalSpawning = animalSpawning;
        this.weatherCycle = weatherCycle;
        this.defaultWeather = defaultWeather;
        this.dayNightCycle = dayNightCycle;
        this.defaultTime = defaultTime;
        this.enablePVP = enablePVP;
        this.forcedGamemode = forcedGamemode;
        this.defaultGamemode = defaultGamemode;
        this.difficulty = difficulty;
        this.randomTickSpeed = randomTickSpeed;
        this.announceAdvancements = announceAdvancements;
    }

    public static void createMapInfos(String worldName, String type, String generator) {
        if (Main.getConfigs().get("worlds").get("Worlds." + worldName) == null) {
            YamlConfiguration config = Main.getConfigs().get("config");
            Main.getConfigs().get("worlds").set("Worlds." + worldName, null);
            if (generator == null) {
                Main.getConfigs().get("worlds").set("Worlds." + worldName + ".generator", "null");
            } else Main.getConfigs().get("worlds").set("Worlds." + worldName + ".generator", generator);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".type", type);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".timeCycle", config.getBoolean("World.timeCycle"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".time", config.getLong("World.time"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weatherCycle", config.getBoolean("World.weatherCycle"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weather", config.getString("World.weather"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".pvp", config.getBoolean("World.pvp"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".mobs", config.getBoolean("World.mobs"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".animals", config.getBoolean("World.animals"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".forcedGamemode", config.getString("World.forcedGamemode"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".defaultGamemode", config.getString("World.defaultGamemode"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".difficulty", config.getString("World.difficulty"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".randomTickSpeed", config.getInt("World.randomTickSpeed"));
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".announceAdvancements", config.getBoolean("World.announceAdvancements"));
            try {
                Main.getConfigs().get("worlds").save(Main.getWorlds());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
                Main.getConfigs().get("worlds").get("Worlds." + worldName + ".randomTickSpeed") == null ? 3 : Main.getConfigs().get("worlds").getInt("Worlds." + worldName + ".randomTickSpeed"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + worldName + ".announceAdvancements")
        ));
    }

    public static void copyMapInfos(String world, String targetWorld) {
        if (Main.getConfigs().get("worlds").get("Worlds." + targetWorld) == null) {
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld, null);
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".generator", Main.getMapinfos().get(world).getGenerator());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".type", Main.getMapinfos().get(world).getType());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".timeCycle", Main.getMapinfos().get(world).isDayNight());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".time", Main.getMapinfos().get(world).getDefaultTime());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".weatherCycle", Main.getMapinfos().get(world).isWeatherCycle());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".weather", Main.getMapinfos().get(world).getDefaultWeather());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".pvp", Main.getMapinfos().get(world).isEnablePVP());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".mobs", Main.getMapinfos().get(world).isMobSpawning());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".animals", Main.getMapinfos().get(world).isAnimalSpawning());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".forcedGamemode", Main.getMapinfos().get(world).isForcedGamemode());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".defaultGamemode", Main.getMapinfos().get(world).getDefaultGamemode());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".difficulty", Main.getMapinfos().get(world).getDifficulty());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".randomTickSpeed", Main.getMapinfos().get(world).getRandomTickSpeed());
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld + ".announceAdvancements", Main.getMapinfos().get(world).isAnnounceAdvancements());
            try {
                Main.getConfigs().get("worlds").save(Main.getWorlds());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Main.getMapinfos().put(targetWorld, new MapInformation(
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".generator"),
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".type"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".mobs"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".animals"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".weatherCycle"),
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".weather"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".timeCycle"),
                Main.getConfigs().get("worlds").getLong("Worlds." + targetWorld + ".time"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".pvp"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".forcedGamemode"),
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".defaultGamemode"),
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".difficulty"),
                Main.getConfigs().get("worlds").getInt("Worlds." + targetWorld + ".randomTickSpeed"),
                Main.getConfigs().get("worlds").getBoolean("Worlds." + targetWorld + ".announceAdvancements")
        ));
    }

    public static void setMapValues(String name) {
        if (!Main.getMapinfos().get(name).isMobSpawning()) {
            Bukkit.getWorld(name).setGameRuleValue("doMobSpawning", "false");
            for (Entity mobs : Bukkit.getWorld(name).getEntities()) {
                switch (Main.getServerversion()) {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof EnderDragon) {
                            if (!Main.getMapinfos().get(name).isMobSpawning()) {
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
                        if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof Shulker || mobs instanceof EnderDragon) {
                            if (!Main.getMapinfos().get(name).isMobSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    case 19:
                        if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof Shulker || mobs instanceof EnderDragon) {
                            if (!Main.getMapinfos().get(name).isMobSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    default:
                        Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                }
            }
        } else {
            Bukkit.getWorld(name).setGameRuleValue("doMobSpawning", "true");
        }
        if (!Main.getMapinfos().get(name).isDayNight()) {
            Bukkit.getWorld(name).setGameRuleValue("doDaylightCycle", "false");
            Bukkit.getWorld(name).setTime(Main.getMapinfos().get(name).getDefaultTime());
        } else {
            Bukkit.getWorld(name).setGameRuleValue("doDaylightCycle", "true");
        }
        Bukkit.getWorld(name).setPVP(Main.getMapinfos().get(name).isEnablePVP());
        if (Main.getMapinfos().get(name).getDifficulty().equalsIgnoreCase("peaceful")) {
            Bukkit.getWorld(name).setDifficulty(Difficulty.PEACEFUL);
        } else if (Main.getMapinfos().get(name).getDifficulty().equalsIgnoreCase("easy")) {
            Bukkit.getWorld(name).setDifficulty(Difficulty.EASY);
        } else if (Main.getMapinfos().get(name).getDifficulty().equalsIgnoreCase("normal")) {
            Bukkit.getWorld(name).setDifficulty(Difficulty.NORMAL);
        } else if (Main.getMapinfos().get(name).getDifficulty().equalsIgnoreCase("hard")) {
            Bukkit.getWorld(name).setDifficulty(Difficulty.HARD);
        }
        if (!Main.getMapinfos().get(name).isAnimalSpawning()) {
            for (Entity mobs : Bukkit.getWorld(name).getEntities()) {
                switch (Main.getServerversion()) {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Villager) {
                            if (!Main.getMapinfos().get(name).isAnimalSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    case 13:
                        if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                || mobs instanceof Dolphin || mobs instanceof Villager) {
                            if (!Main.getMapinfos().get(name).isAnimalSpawning()) {
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
                            if (!Main.getMapinfos().get(name).isAnimalSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    case 19:
                        if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                || mobs instanceof Dolphin || mobs instanceof Villager
                                || mobs instanceof WanderingTrader) {
                            if (!Main.getMapinfos().get(name).isAnimalSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    default:
                        Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                }
            }
        }
        if (!Main.getMapinfos().get(name).isWeatherCycle()) {
            Bukkit.getWorld(name).setGameRuleValue("doWeatherCycle", "false");
            if (Main.getMapinfos().get(name).getDefaultWeather().equalsIgnoreCase("sun")) {
                Bukkit.getWorld(name).setStorm(false);
                Bukkit.getWorld(name).setThundering(false);
            }
            if (Main.getMapinfos().get(name).getDefaultWeather().equalsIgnoreCase("rain")) {
                Bukkit.getWorld(name).setStorm(true);
                Bukkit.getWorld(name).setThundering(false);
            } else if (Main.getMapinfos().get(name).getDefaultWeather().equalsIgnoreCase("storm")) {
                Bukkit.getWorld(name).setStorm(true);
                Bukkit.getWorld(name).setThundering(true);
            }
        } else {
            Bukkit.getWorld(name).setGameRuleValue("doWeatherCycle", "true");
        }
        Bukkit.getWorld(name).setGameRuleValue("announceAdvancements", String.valueOf(Main.getMapinfos().get(name).isAnnounceAdvancements()));
        Bukkit.getWorld(name).setGameRuleValue("randomTickSpeed", String.valueOf(Main.getMapinfos().get(name).getRandomTickSpeed()));
    }

    public String getType() {
        return type;
    }

    public String getGenerator() {
        return generator;
    }

    public long getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    public String getDefaultGamemode() {
        return defaultGamemode;
    }

    public void setDefaultGamemode(String defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
    }

    public String getDefaultWeather() {
        return defaultWeather;
    }

    public void setDefaultWeather(String defaultWeather) {
        this.defaultWeather = defaultWeather;
    }

    public int getRandomTickSpeed() {
        return randomTickSpeed;
    }

    public void setRandomTickSpeed(int randomTickSpeed) {
        this.randomTickSpeed = randomTickSpeed;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isAnimalSpawning() {
        return animalSpawning;
    }

    public void setAnimalSpawning(boolean animalSpawning) {
        this.animalSpawning = animalSpawning;
    }

    public boolean isDayNight() {
        return dayNightCycle;
    }

    public void setDayNight(boolean dayNightCycle) {
        this.dayNightCycle = dayNightCycle;
    }

    public boolean isEnablePVP() {
        return enablePVP;
    }

    public void setEnablePVP(boolean enablePVP) {
        this.enablePVP = enablePVP;
    }

    public boolean isForcedGamemode() {
        return forcedGamemode;
    }

    public boolean isMobSpawning() {
        return mobSpawning;
    }

    public void setMobSpawning(boolean mobSpawning) {
        this.mobSpawning = mobSpawning;
    }

    public boolean isWeatherCycle() {
        return weatherCycle;
    }

    public void setWeatherCycle(boolean weatherCycle) {
        this.weatherCycle = weatherCycle;
    }

    public void setForceGamemode(boolean forcedGamemode) {
        this.forcedGamemode = forcedGamemode;
    }

    public boolean isAnnounceAdvancements() {
        return announceAdvancements;
    }

    public void setAnnounceAdvancements(boolean announceAdvancements) {
        this.announceAdvancements = announceAdvancements;
    }
}

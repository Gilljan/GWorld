/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.api;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface IGWorldAPI {
    /**
     * Create a world (based on a seed)
     * @param type Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     * @param seed The world will be created based on the seed.
     * @param generator The chunkgenerator. Set null, to use Bukkit Generator
     */
    void create(WorldType type, @Nullable Long seed, @Nullable String generator);

    /**
     * Create a world (based on a seed)
     * @param type Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     */
    void create(WorldType type);

    /**
     * Import an existing world
     * @param type Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     * @param generator The chunkgenerator. Set null, to use Bukkit Generator
     */
    void importExisting(WorldType type, @Nullable String generator);

    /**
     * Import an existing world
     * @param type Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     */
    void importExisting(WorldType type);

    /**
     * Clone a world based on an existing world
     * @param newWorld The name of the cloned world.
     */
    GWorldAPI clone(String newWorld); // gibt die geklonte welt zurück

    /**
     * Delete and unload a world
     */
    void delete();

    /**
     * Load an existing world.
     */
    void load();

    /**
     * Unload an existing world.
     */
    void unload();

    /**Recreate a world based on an existing world.
     * @param saveOldWorld Decide, if the should be deleted or not: true (save), false (delete)
     **/
    void reCreate(boolean saveOldWorld);

    /**
     * @return true, when the world is loaded.
     */
    boolean isLoaded();

    /**
     * @return true, when the world is imported.
     */
    boolean isImported();

    /**
     * @param autoLoad Decide, whether the world will be loaded or not while startup.
     */
    void setAutoLoad(boolean autoLoad);

    /**
     * @return true, when the world will be loaded at startup.
     */
    boolean isAutoLoad();

    /**
     * @return the name of the world.
     */
    String getName();

    /**
     * @return the seed of the world.
     */
    long getSeed();

    /**
     * @return the generator of the world.
     */
    String getGenerator();

    /**
     * @return the type of the world.
     */
    WorldType getType();

    /**Get the Bukkit-World*/
    World getBukkitWorld();

    void setPvp(boolean enablePvp);
    void setMobSpawning(boolean enableMobSpawning);
    void setAnimalSpawning(boolean enableAnimalSpawning);
    void setWeatherCycle(boolean enableWeatherCycle);
    void setForceGameMode(boolean enableForceGameMode);
    void setDayNightCycle(boolean enableDayNightCycle);

    void setDefaultWeather(WeatherType weatherType);
    void setDefaultTime(long defaultTime);
    void setDefaultGameMode(Gamemode gameMode);
    void setDifficulty(Difficulty difficulty);

    boolean isPvp();
    boolean isMobSpawning();
    boolean isAnimalSpawning();
    boolean isWeatherCycle();
    boolean isForceGameMode();
    boolean isDayNightCycle();

    WeatherType getDefaultWeather();
    long getDefaultTime();
    Gamemode getDefaultGameMode();
    Difficulty getDifficulty();

    /**
     * @return All found generators by GWorld.
     */
    static List<String> getAvailableGenerators() {
        return Main.availableGenerators;
    }

    /**
     * Reload the plugin with all configurations.
     */
    static void reloadPlugin() {
        try {
            Main.getConfigs().remove("worlds");
            Main.getConfigs().put("worlds", YamlConfiguration.loadConfiguration(Main.getWorlds()));
            Main.loadedWorlds.clear();
            for (int i = 0; i < Main.getConfigs().get("worlds").getStringList("LoadWorlds").size(); i++) {
                if(Main.getConfigs().get("worlds").get("Worlds." + Main.loadedWorlds.get(i)) != null && new File(Bukkit.getWorldContainer(), Main.loadedWorlds.get(i)).exists())
                    Main.loadedWorlds.add(Main.getConfigs().get("worlds").getStringList("LoadWorlds").get(i));
            }
            Main.getMapinfos().clear();
            for (int i = 0; i < Main.loadedWorlds.size(); i++) {
                Main.getMapinfos().put(Main.loadedWorlds.get(i), new MapInformation(
                        Main.getConfigs().get("worlds").getString("Worlds." + Main.loadedWorlds.get(i) + ".generator"),
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
                if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("normal")) {
                    w.type(org.bukkit.WorldType.NORMAL);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("end")) {
                    w.environment(World.Environment.THE_END);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("amplified")) {
                    w.type(org.bukkit.WorldType.AMPLIFIED);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("nether")) {
                    w.environment(World.Environment.NETHER);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("flat")) {
                    w.type(org.bukkit.WorldType.FLAT);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getType().equalsIgnoreCase("large_biomes")) {
                    w.type(org.bukkit.WorldType.LARGE_BIOMES);
                } else w.type(org.bukkit.WorldType.NORMAL);
                if(!Main.getMapinfos().get(Main.loadedWorlds.get(i)).getGenerator().equalsIgnoreCase("null")) {
                    w.generator(Main.getMapinfos().get(Main.loadedWorlds.get(i)).getGenerator());
                }
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
                            }
                        }
                    } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("survival")) {
                        for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                            try {
                                all.setGameMode(GameMode.SURVIVAL);
                            } catch (Exception ex) {
                                Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                            }
                        }
                    } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("spectator")) {
                        for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                            try {
                                all.setGameMode(GameMode.SPECTATOR);
                            } catch (Exception ex) {
                                Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
                            }
                        }
                    } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDefaultGamemode().equalsIgnoreCase("adventure")) {
                        for (Player all : Bukkit.getWorld(Main.loadedWorlds.get(i)).getPlayers()) {
                            try {
                                all.setGameMode(GameMode.ADVENTURE);
                            } catch (Exception ex) {
                                Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + Main.loadedWorlds.get(i) + " §cis not right. Please use creative, adventure, survival or spectator");
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
                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(org.bukkit.Difficulty.PEACEFUL);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("easy")) {
                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(org.bukkit.Difficulty.EASY);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("normal")) {
                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(org.bukkit.Difficulty.NORMAL);
                } else if (Main.getMapinfos().get(Main.loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("hard")) {
                    Bukkit.getWorld(Main.loadedWorlds.get(i)).setDifficulty(org.bukkit.Difficulty.HARD);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    enum WorldType {
        NORMAL ("normal"),
        NETHER ("nether"),
        END ("end"),
        LARGE_BIOMES ("large_biomes"),
        AMPLIFIED ("amplified"),
        FLAT ("flat");

        private final String type;
        WorldType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    enum WeatherType {
        SUN ("sun"),
        STORM ("storm"),
        RAIN ("rain");

        private final String weatherType;
        WeatherType(String weatherType) {
            this.weatherType = weatherType;
        }

        public String getWeatherType() {
            return weatherType;
        }
    }

    enum Gamemode {
        CREATIVE ("creative", GameMode.CREATIVE),
        SURVIVAL ("survival", GameMode.SURVIVAL),
        ADVENTURE ("adventure", GameMode.ADVENTURE),
        SPECTATOR ("spectator", GameMode.SPECTATOR);

        private final String gamemode;
        private final GameMode gameMode;
        Gamemode(String gamemode, GameMode gameMode) {
            this.gamemode = gamemode;
            this.gameMode = gameMode;
        }

        public String getGamemode() {
            return gamemode;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        /**
         * Get the GWorld Gamemode by a Bukkit-Gamemode
         * @param gameMode org.Bukkit.GameMode
         * @return GWorld-Gamemode
         */
        public static Gamemode getFromBukkitGameMode(GameMode gameMode) {
            switch (gameMode) {
                case CREATIVE:
                    return Gamemode.CREATIVE;
                case SURVIVAL:
                    return Gamemode.SURVIVAL;
                case ADVENTURE:
                    return Gamemode.ADVENTURE;
                case SPECTATOR:
                    return Gamemode.SPECTATOR;
            }
            return null;
        }

    }

    enum Difficulty {
        EASY ("easy", org.bukkit.Difficulty.EASY),
        PEACEFUL ("peaceful", org.bukkit.Difficulty.PEACEFUL),
        NORMAL ("normal", org.bukkit.Difficulty.NORMAL),
        HARD ("hard", org.bukkit.Difficulty.HARD);

        private final String difficulty;
        private final org.bukkit.Difficulty difficultMode;
        Difficulty(String difficulty, org.bukkit.Difficulty difficultMode) {
            this.difficulty = difficulty;
            this.difficultMode = difficultMode;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public org.bukkit.Difficulty getDifficultMode() {
            return difficultMode;
        }
    }
}

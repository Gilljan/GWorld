/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.api;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.MapInformation;
import de.gilljan.gworld.utils.SendMessage_util;
import de.gilljan.gworld.utils.ServerVersion;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GWorldAPI implements IGWorldAPI {
    private String worldName;

    /**
     * @param worldName The name of the world
     */
    public GWorldAPI(String worldName) {
        this.worldName = worldName;
    }

    /**
     * Create a world (based on a seed)
     *
     * @param type      Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     * @param seed      The world will be created based on the seed.
     * @param generator The chunkgenerator. Set null, to use Bukkit Generator
     */
    @Override
    public void create(WorldType type, @Nullable Long seed, @Nullable String generator) {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (!Main.loadedWorlds.contains(worldName) && !Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && !new File(Bukkit.getWorldContainer(), worldName).exists()) {
                if (seed != null) {
                    createWorld(worldName, type, null, generator);
                } else {
                    createWorld(worldName, type, seed, generator);
                }
            }
        }
    }

    @Override
    public void create(WorldType type) {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (!Main.loadedWorlds.contains(worldName) && !Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && !new File(Bukkit.getWorldContainer(), worldName).exists()) {
                createWorld(worldName, type, null, null);
            }
        }
    }

    /**
     * Import an existing world
     *
     * @param type      Worldgenertation type: normal, end, amplified, nether, flat, large_biomes
     * @param generator The chunkgenerator. Set null, to use Bukkit Generator
     */
    @Override
    public void importExisting(WorldType type, @Nullable String generator) {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (!Main.loadedWorlds.contains(worldName) && new File(Bukkit.getWorldContainer(), worldName).exists()) {
                createWorld(worldName, type, null, generator);
            }
        }
    }

    @Override
    public void importExisting(WorldType type) {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (!Main.loadedWorlds.contains(worldName) && new File(Bukkit.getWorldContainer(), worldName).exists()) {
                createWorld(worldName, type, null, null);
            }
        }
    }

    /**
     * Clone a world based on an existing world
     *
     * @param newWorld The name of the cloned world.
     */
    @Override
    public GWorldAPI clone(String newWorld) {
        if (!worldName.contains(".") & !worldName.contains("/") & !newWorld.contains(".") & !newWorld.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (Main.loadedWorlds.contains(worldName) && !Main.loadedWorlds.contains(newWorld) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && !Bukkit.getWorlds().contains(Bukkit.getWorld(newWorld)) && new File(Bukkit.getWorldContainer(), worldName).exists() && !new File(Bukkit.getWorldContainer(), newWorld).exists()) {
                Bukkit.getWorld(worldName).save();
                try {
                    FileUtils.copyDirectory(new File(Bukkit.getWorldContainer(), worldName), new File(Bukkit.getWorldContainer(), newWorld));
                    new File(newWorld, "uid.dat").delete();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                MapInformation.copyMapInfos(worldName, newWorld);
                WorldCreator w = WorldCreator.name(newWorld);
                String type = Main.getMapinfos().get(worldName).getType();
                if (type.equalsIgnoreCase("normal")) {
                    w.type(org.bukkit.WorldType.NORMAL);
                } else if (type.equalsIgnoreCase("end")) {
                    w.environment(World.Environment.THE_END);
                } else if (type.equalsIgnoreCase("amplified")) {
                    w.type(org.bukkit.WorldType.AMPLIFIED);
                } else if (type.equalsIgnoreCase("nether")) {
                    w.environment(World.Environment.NETHER);
                } else if (type.equalsIgnoreCase("flat")) {
                    w.type(org.bukkit.WorldType.FLAT);
                } else if (type.equalsIgnoreCase("large_biomes")) {
                    w.type(org.bukkit.WorldType.LARGE_BIOMES);
                }
                w.generator(Bukkit.getWorld(worldName).getGenerator());
                Bukkit.createWorld(w);
                Bukkit.getWorlds().add(Bukkit.getWorld(newWorld));
                Main.loadedWorlds.add(newWorld);
                Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                try {
                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                MapInformation.setMapValues(newWorld);
            }
        }
        return new GWorldAPI(newWorld);
    }

    /**
     * Delete and unload a world
     */
    @Override
    public void delete() {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && Main.loadedWorlds.contains(worldName) && new File(Bukkit.getWorldContainer(), worldName).exists()) {
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
                File world = new File(Bukkit.getWorldContainer() + "//" + worldName);
                Main.getConfigs().get("worlds").getConfigurationSection("Worlds").set(worldName, null);
                try {
                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                    FileUtils.deleteDirectory(world);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Load and unload an existing world.
     */
    @Override
    public void load() {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (!Main.loadedWorlds.contains(worldName) && !Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)) && new File(Bukkit.getWorldContainer(), worldName).exists() && Main.getConfigs().get("worlds").get("Worlds." + worldName) != null) {
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
                    w.type(org.bukkit.WorldType.NORMAL);
                } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("end")) {
                    w.environment(World.Environment.THE_END);
                } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("amplified")) {
                    w.type(org.bukkit.WorldType.AMPLIFIED);
                } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("nether")) {
                    w.environment(World.Environment.NETHER);
                } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("flat")) {
                    w.type(org.bukkit.WorldType.FLAT);
                } else if (Main.getMapinfos().get(worldName).getType().equalsIgnoreCase("large_biomes")) {
                    w.type(org.bukkit.WorldType.LARGE_BIOMES);
                } else w.type(org.bukkit.WorldType.NORMAL);
                if (!Main.getMapinfos().get(worldName).getGenerator().equalsIgnoreCase("null")) {
                    w.generator(Main.getMapinfos().get(worldName).getGenerator());
                }
                Bukkit.createWorld(w);
                Main.loadedWorlds.add(worldName);
                try {
                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                MapInformation.setMapValues(worldName);
            }
        }
    }

    @Override
    public void unload() {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (Bukkit.getWorld(worldName).getEntities().contains(all)) {
                        all.teleport(Bukkit.getWorld(Main.getConfigs().get("config").getString("MainWorld")).getSpawnLocation());
                        all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.teleport_players").replaceAll("%world%", worldName));
                    }
                }
                Bukkit.getWorlds().remove(Bukkit.getWorld(worldName));
                Bukkit.unloadWorld(Bukkit.getWorld(worldName), true);
                Main.loadedWorlds.remove(worldName);
                Main.getMapinfos().remove(worldName);
                Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                try {
                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Recreate a world based on an existing world.
     *
     * @param saveOldWorld Decide, if the should be deleted or not: true (save), false (delete)
     **/
    @Override
    public void reCreate(boolean saveOldWorld) {
        if (!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
            if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                long seed = Bukkit.getWorld(worldName).getSeed();
                ChunkGenerator generator = Bukkit.getWorld(worldName).getGenerator();
                if (saveOldWorld) {
                    Bukkit.unloadWorld(worldName, true);
                    File dir = new File("old_Maps//");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File target = new File("old_Maps", worldName + "-" + new SimpleDateFormat("dd.MM.yyyy - HH-mm-ss").format(new Date()));
                    System.out.println(target.getAbsolutePath());
                    try {
                        FileUtils.moveDirectory(new File(worldName), target);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Bukkit.unloadWorld(worldName, true);
                    try {
                        FileUtils.deleteDirectory(new File(worldName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    String type = Main.getMapinfos().get(worldName).getType();
                    WorldCreator worldCreator = WorldCreator.name(worldName);
                    worldCreator.generator(generator);
                    org.bukkit.WorldType worldType;
                    switch (type) {
                        case "normal":
                            worldType = org.bukkit.WorldType.NORMAL;
                            worldCreator.type(worldType);
                            break;
                        case "amplified":
                            worldType = org.bukkit.WorldType.AMPLIFIED;
                            worldCreator.type(worldType);
                            break;
                        case "large_biomes":
                            worldType = org.bukkit.WorldType.LARGE_BIOMES;
                            worldCreator.type(worldType);
                            break;
                        case "flat":
                            worldType = org.bukkit.WorldType.FLAT;
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
                    Bukkit.createWorld(worldCreator);
                    MapInformation.setMapValues(worldName);
                });
            }
        }
    }

    /**
     * Save the changes to file.
     */
    @Override
    public void save() {
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".timeCycle", isDayNightCycle());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".time", getDefaultTime());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weatherCycle", isWeatherCycle());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weather", getDefaultWeather().getWeatherType());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".pvp", isPvp());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".mobs", isMobSpawning());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".animals", isAnimalSpawning());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".forcedGamemode", isForceGameMode());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".defaultGamemode", getDefaultGameMode().getGamemode());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".difficulty", getDifficulty().getDifficulty());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".randomTickSpeed", getRandomTickSpeed());
        Main.getConfigs().get("worlds").set("Worlds." + worldName + ".announceAdvancements", isAnnounceAdvancement());
        try {
            Main.getConfigs().get("worlds").save(Main.getWorlds());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isAnnounceAdvancement() {
        return Main.getMapinfos().get(worldName).isAnnounceAdvancements();
    }

    @Override
    public void setAnnounceAdvancement(boolean announceAdvancement) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setAnnounceAdvancements(announceAdvancement);
            Bukkit.getWorld(worldName).setGameRuleValue("announceAdvancements", String.valueOf(announceAdvancement));
        }
    }

    @Override
    public boolean isLoaded() {
        return Bukkit.getWorlds().contains(Bukkit.getWorld(worldName));
    }

    @Override
    public boolean isImported() {
        return (Main.loadedWorlds.contains(worldName));
    }

    @Override
    public boolean isAutoLoad() {
        return (Main.getConfigs().get("worlds").getStringList("LoadWorlds").contains(worldName));
    }

    @Override
    public void setAutoLoad(boolean autoLoad) {
        List<String> tempList = Main.loadedWorlds;
        if (autoLoad) {
            if (Main.getConfigs().get("worlds").get("Worlds." + worldName) != null) {
                if (!tempList.contains(worldName)) {
                    tempList.add(worldName);
                }
            }
        } else {
            if (tempList.contains(worldName)) {
                tempList.remove(worldName);
            }
        }
        Main.getConfigs().get("worlds").set("LoadWorlds", tempList);
        try {
            Main.getConfigs().get("worlds").save(Main.getWorlds());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return worldName;
    }

    @Override
    public long getSeed() {
        return getBukkitWorld().getSeed();
    }

    @Override
    public String getGenerator() {
        String generator = null;
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            ChunkGenerator plC = pl.getDefaultWorldGenerator("", "");
            if (plC != null && pl.isEnabled() && Bukkit.getWorld(worldName).getGenerator() != null) {
                if (plC.getClass().equals(Bukkit.getWorld(worldName).getGenerator().getClass())) {
                    generator = pl.getName();
                }
            }
        }
        if (generator != null) {
            return generator;
        }
        return "Bukkit";
    }

    @Override
    public WorldType getType() {
        switch (Main.getMapinfos().get(worldName).getType()) {
            case "normal":
                return WorldType.NORMAL;
            case "end":
                return WorldType.END;
            case "nether":
                return WorldType.NETHER;
            case "amplified":
                return WorldType.AMPLIFIED;
            case "large_biomes":
                return WorldType.LARGE_BIOMES;
            case "flat":
                return WorldType.FLAT;
        }
        return null;
    }

    /**
     * Get the Bukkit-World
     */
    @Override
    public World getBukkitWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public int getRandomTickSpeed() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            return Main.getMapinfos().get(worldName).getRandomTickSpeed();
        }
        return -1;
    }

    @Override
    public void setRandomTickSpeed(int randomTickSpeed) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setRandomTickSpeed(randomTickSpeed);
            Bukkit.getWorld(worldName).setGameRuleValue("randomTickSpeed", String.valueOf(randomTickSpeed));
        }
    }

    @Override
    public boolean isPvp() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isEnablePVP();
        return false;
    }

    @Override
    public void setPvp(boolean enablePvp) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setEnablePVP(enablePvp);
            Bukkit.getWorld(worldName).setPVP(enablePvp);
        }
    }

    @Override
    public boolean isMobSpawning() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isMobSpawning();
        return false;
    }

    @Override
    public void setMobSpawning(boolean enableMobSpawning) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setMobSpawning(enableMobSpawning);
            Bukkit.getWorld(worldName).setGameRuleValue("doMobSpawning", String.valueOf(enableMobSpawning));
            for (Entity mobs : Bukkit.getWorld(worldName).getEntities()) {
                switch (Main.getServerversion()) {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof EnderDragon) {
                            if (!Main.getMapinfos().get(worldName).isMobSpawning()) {
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
                        if (mobs instanceof Monster || mobs instanceof IronGolem || mobs instanceof Slime || mobs instanceof MagmaCube || mobs instanceof Shulker || mobs instanceof EnderDragon) {
                            if (!Main.getMapinfos().get(worldName).isMobSpawning()) {
                                mobs.remove();
                            }
                        }
                        break;
                    default:
                        Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                        throw new UnsupportedClassVersionError("§4Unsupported Version: §e" + Main.getFullServerversion());
                }
            }
        }
    }

    @Override
    public boolean isAnimalSpawning() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isAnimalSpawning();
        return false;
    }

    @Override
    public void setAnimalSpawning(boolean enableAnimalSpawning) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setAnimalSpawning(enableAnimalSpawning);
            if (!enableAnimalSpawning) {
                for (Entity mobs : Bukkit.getWorld(worldName).getEntities()) {
                    switch (Main.getServerversion()) {
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Villager) {
                                if (!Main.getMapinfos().get(worldName).isAnimalSpawning()) {
                                    mobs.remove();
                                }
                            }
                            break;
                        case 13:
                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                    || mobs instanceof Dolphin || mobs instanceof Villager) {
                                if (!Main.getMapinfos().get(worldName).isAnimalSpawning()) {
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
                                if (!Main.getMapinfos().get(worldName).isAnimalSpawning()) {
                                    mobs.remove();
                                }
                            }
                            break;
                        case 19:
                            if (mobs instanceof Animals || mobs instanceof Squid || mobs instanceof Bat || mobs instanceof Fish
                                    || mobs instanceof Dolphin || mobs instanceof Villager
                                    || mobs instanceof WanderingTrader || mobs instanceof Allay) {
                                if (!Main.getMapinfos().get(worldName).isAnimalSpawning()) {
                                    mobs.remove();
                                }
                            }
                            break;
                        default:
                            Bukkit.getServer().getConsoleSender().sendMessage("§4Unsupported Version: §e" + Main.getFullServerversion());
                            throw new UnsupportedClassVersionError("§4Unsupported Version: §e" + Main.getFullServerversion());
                    }
                }
            }
        }
    }

    @Override
    public boolean isWeatherCycle() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isWeatherCycle();
        return false;
    }

    @Override
    public void setWeatherCycle(boolean enableWeatherCycle) {
        Main.getMapinfos().get(worldName).setWeatherCycle(enableWeatherCycle);
        if (enableWeatherCycle) {
            Bukkit.getWorld(worldName).setGameRuleValue("doWeatherCycle", "true");
        } else {
            Bukkit.getWorld(worldName).setGameRuleValue("doWeatherCycle", "false");
        }
    }

    @Override
    public boolean isForceGameMode() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isForcedGamemode();
        return false;
    }

    @Override
    public void setForceGameMode(boolean enableForceGameMode) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setForceGamemode(enableForceGameMode);
            if (enableForceGameMode) {
                for (Player all : Bukkit.getWorld(worldName).getPlayers()) {
                    try {
                        if (Main.getMapinfos().get(worldName).getDefaultGamemode().equalsIgnoreCase("survival")) {
                            all.setGameMode(GameMode.SURVIVAL);
                        } else if (Main.getMapinfos().get(worldName).getDefaultGamemode().equalsIgnoreCase("creative")) {
                            all.setGameMode(GameMode.CREATIVE);
                        } else if (Main.getMapinfos().get(worldName).getDefaultGamemode().equalsIgnoreCase("spectator")) {
                            all.setGameMode(GameMode.SPECTATOR);
                        } else if (Main.getMapinfos().get(worldName).getDefaultGamemode().equalsIgnoreCase("adventure")) {
                            all.setGameMode(GameMode.ADVENTURE);
                        }
                    } catch (Exception ex) {
                        Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cThe set forcedGamemode of the world §e" + worldName + " §cis not right. Please use creative, survival, adventure or spectator");
                    }
                }
            }
        }
    }

    @Override
    public boolean isDayNightCycle() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).isDayNight();
        return false;
    }

    @Override
    public void setDayNightCycle(boolean enableDayNightCycle) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setDayNight(enableDayNightCycle);
            Bukkit.getWorld(worldName).setGameRuleValue("doDaylightCycle", String.valueOf(enableDayNightCycle));
        }
    }

    @Override
    public WeatherType getDefaultWeather() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            switch (Main.getMapinfos().get(worldName).getDefaultWeather()) {
                case "sun":
                    return WeatherType.SUN;
                case "rain":
                    return WeatherType.RAIN;
                case "storm":
                    return WeatherType.STORM;
            }
        return null;
    }

    @Override
    public void setDefaultWeather(WeatherType weatherType) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setDefaultWeather(weatherType.getWeatherType());
            if (weatherType == WeatherType.SUN) {
                if (!Main.getMapinfos().get(worldName).isWeatherCycle()) {
                    Bukkit.getWorld(worldName).setStorm(false);
                    Bukkit.getWorld(worldName).setThundering(false);
                }
            } else if (weatherType == WeatherType.STORM) {
                if (!Main.getMapinfos().get(worldName).isWeatherCycle()) {
                    Bukkit.getWorld(worldName).setStorm(true);
                    Bukkit.getWorld(worldName).setThundering(true);
                }
            } else if (weatherType == WeatherType.RAIN) {
                if (!Main.getMapinfos().get(worldName).isWeatherCycle()) {
                    Bukkit.getWorld(worldName).setStorm(true);
                    Bukkit.getWorld(worldName).setThundering(false);
                }
            }
        }
    }

    @Override
    public long getDefaultTime() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            return Main.getMapinfos().get(worldName).getDefaultTime();
        return 0L;
    }

    @Override
    public void setDefaultTime(long defaultTime) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setDefaultTime(defaultTime);
            if (!Main.getMapinfos().get(worldName).isDayNight())
                Bukkit.getWorld(worldName).setTime(defaultTime);
        }
    }

    @Override
    public Gamemode getDefaultGameMode() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            switch (Main.getMapinfos().get(worldName).getDefaultGamemode().toLowerCase(Locale.ROOT)) {
                case "creative":
                    return Gamemode.CREATIVE;
                case "survival":
                    return Gamemode.SURVIVAL;
                case "spectator":
                    return Gamemode.SPECTATOR;
                case "adventure":
                    return Gamemode.ADVENTURE;
            }
        return null;
    }

    @Override
    public void setDefaultGameMode(Gamemode gameMode) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setDefaultGamemode(gameMode.getGamemode());
            if (Main.getMapinfos().get(worldName).isForcedGamemode()) {
                for (Player all : Bukkit.getWorld(worldName).getPlayers()) {
                    try {
                        all.setGameMode(gameMode.getGameMode());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Difficulty getDifficulty() {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
            switch (Main.getMapinfos().get(worldName).getDifficulty().toLowerCase(Locale.ROOT)) {
                case "peaceful":
                    return Difficulty.PEACEFUL;
                case "easy":
                    return Difficulty.EASY;
                case "normal":
                    return Difficulty.NORMAL;
                case "hard":
                    return Difficulty.HARD;
            }
        return null;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        if (Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Main.getMapinfos().get(worldName).setDifficulty(difficulty.getDifficulty());
            Bukkit.getWorld(worldName).setDifficulty(difficulty.getDifficultMode());
        }
    }

    private void createWorld(String worldName, WorldType type, Long seed, String generator) {
        MapInformation.createMapInfos(worldName, type.getType(), generator);
        WorldCreator w = WorldCreator.name(worldName);
        switch (type) {
            case NORMAL:
                w.type(org.bukkit.WorldType.NORMAL);
                break;
            case LARGE_BIOMES:
                w.type(org.bukkit.WorldType.LARGE_BIOMES);
                break;
            case END:
                w.environment(World.Environment.THE_END);
                break;
            case FLAT:
                w.type(org.bukkit.WorldType.FLAT);
                break;
            case NETHER:
                w.environment(World.Environment.NETHER);
                break;
            case AMPLIFIED:
                w.type(org.bukkit.WorldType.AMPLIFIED);
                break;
        }
        if (seed != null) {
            w.seed(seed);
        }
        if (generator != null) {
            w.generator(generator);
        }
        File target = new File(Bukkit.getWorldContainer(), worldName);
        try {
            new File(target, "uid.dat").delete();
        } catch (Exception ignored) {
        }
        Main.loadedWorlds.add(worldName);
        if (!Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Bukkit.createWorld(w);
            Bukkit.getWorlds().add(Bukkit.getWorld(worldName));
        }
        Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
        try {
            Main.getConfigs().get("worlds").save(Main.getWorlds());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

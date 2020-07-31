package de.gilljan.gworld.utils;

import de.gilljan.gworld.Main;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.*;

import java.io.IOException;

public class MapInformation {
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

    public MapInformation(String type, boolean mobSpawning, boolean animalSpawning, boolean weatherCycle, String defaultWeather, boolean dayNightCycle, long defaultTime, boolean enablePVP, boolean forcedGamemode, String defaultGamemode, String difficulty) {
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
    }

    public String getType() {
        return type;
    }

    public long getDefaultTime() {
        return defaultTime;
    }

    public String getDefaultGamemode() {
        return defaultGamemode;
    }

    public String getDefaultWeather() {
        return defaultWeather;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public boolean isAnimalSpawning() {
        return animalSpawning;
    }

    public boolean isDayNight() {
        return dayNightCycle;
    }

    public boolean isEnablePVP() {
        return enablePVP;
    }

    public boolean isForcedGamemode() {
        return forcedGamemode;
    }

    public boolean isMobSpawning() {
        return mobSpawning;
    }

    public boolean isWeatherCycle() {
        return weatherCycle;
    }

    public void setAnimalSpawning(boolean animalSpawning) {
        this.animalSpawning = animalSpawning;
    }

    public void setDayNight(boolean dayNightCycle) {
        this.dayNightCycle = dayNightCycle;
    }

    public void setDefaultGamemode(String defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
    }

    public void setDefaultTime(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    public void setDefaultWeather(String defaultWeather) {
        this.defaultWeather = defaultWeather;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setEnablePVP(boolean enablePVP) {
        this.enablePVP = enablePVP;
    }

    public void setForceGamemode(boolean forcedGamemode) {
        this.forcedGamemode = forcedGamemode;
    }

    public void setMobSpawning(boolean mobSpawning) {
        this.mobSpawning = mobSpawning;
    }

    public void setWeatherCycle(boolean weatherCycle) {
        this.weatherCycle = weatherCycle;
    }

    public static void createMapInfos(String worldName, String type) {
        if (Main.getConfigs().get("worlds").get("Worlds." + worldName) == null) {
            Main.getConfigs().get("worlds").set("Worlds." + worldName, null);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".type", type);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".timeCycle", true);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".time", 6000);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weatherCycle", true);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".weather", "sun");
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".pvp", true);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".mobs", true);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".animals", true);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".forcedGamemode", false);
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".defaultGamemode", GameMode.SURVIVAL.toString());
            Main.getConfigs().get("worlds").set("Worlds." + worldName + ".difficulty", Difficulty.NORMAL.toString());
            try {
                Main.getConfigs().get("worlds").save(Main.getWorlds());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
    }

    public static void copyMapInfos(String world, String targetWorld) {
        if (Main.getConfigs().get("worlds").get("Worlds." + targetWorld) == null) {
            Main.getConfigs().get("worlds").set("Worlds." + targetWorld, null);
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
            try {
                Main.getConfigs().get("worlds").save(Main.getWorlds());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Main.getMapinfos().put(targetWorld, new MapInformation(
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
                Main.getConfigs().get("worlds").getString("Worlds." + targetWorld + ".difficulty")
        ));
    }

    public static void setMapValues(String name) {
        if (!Main.getMapinfos().get(name).isMobSpawning()) {
            Bukkit.getWorld(name).setGameRuleValue("doMobSpawning", "false");
            for (Entity mobs : Bukkit.getWorld(name).getEntities()) {
                if (mobs instanceof Monster || mobs instanceof Ghast || mobs instanceof Slime || mobs instanceof MagmaCube) {
                    mobs.remove();
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
                if (mobs instanceof Animals || mobs instanceof Squid) {
                    mobs.remove();
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
    }
}

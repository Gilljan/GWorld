package de.gilljan.gworld;

import de.gilljan.gworld.commands.*;
import de.gilljan.gworld.listener.AnimalSpawning_listener;
import de.gilljan.gworld.listener.PlayerJoin_listener;
import de.gilljan.gworld.listener.WorldChange_listener;
import de.gilljan.gworld.utils.MapInformation;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
    private static String prefix;
    private static Main instance;

    private static File worlds;
    private static File config;
    public static List<String> loadedWorlds = new ArrayList<>();

    @Override
    public void onEnable() {
        try {
            init();
            worlds = new File(getInstance().getDataFolder().getPath() + "//worlds.yml");
            config = new File(getInstance().getDataFolder().getPath() + "//config.yml");
            loadConfigs();
            loadWorlds();
        } catch (Exception ex) {
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            unload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        instance = this;
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();
        if (!(new File(getDataFolder().getPath() + "//de_DE.yml").exists()))
            saveResource("de_DE.yml", false);
        if (!(new File(getDataFolder().getPath() + "//en_EN.yml").exists()))
            saveResource("en_EN.yml", false);
        if (!(new File(getDataFolder().getPath() + "//worlds.yml").exists()))
            saveResource("worlds.yml", false);

        prefix = getConfig().getString("Prefix")
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
                .replaceAll("&k", "§k");

        if (isEnabled()) {
            getCommand("gcreate").setExecutor(new GCreate_cmd());
            getCommand("gimport").setExecutor(new GImport_cmd());
            getCommand("gtp").setExecutor(new GTp_cmd());
            getCommand("gclone").setExecutor(new GClone_cmd());
            getCommand("gdelete").setExecutor(new GDelete_cmd());
            getCommand("gworlds").setExecutor(new GWorlds_cmd());
            getCommand("gwreload").setExecutor(new GWReload_cmd());
            getCommand("gset").setExecutor(new GSet_cmd());
            getCommand("ginfo").setExecutor(new GInfo_cmd());
            getCommand("grecreate").setExecutor(new GReCreate_cmd());

            PluginManager pm = Bukkit.getPluginManager();

            pm.registerEvents(new AnimalSpawning_listener(), this);
            pm.registerEvents(new WorldChange_listener(), this);
            pm.registerEvents(new PlayerJoin_listener(), this);


        }
    }

    private void unload() {


    }

    private void loadConfigs() {
        if (getConfig().getString("Language").equalsIgnoreCase("de")) {
            getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(getDataFolder().getPath() + "//de_DE.yml")));
        } else if (getConfig().getString("Language").equalsIgnoreCase("en")) {
            getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(getDataFolder().getPath() + "//en_EN.yml")));
        } else if (getConfig().getString("Language").equalsIgnoreCase("fr")) {
            getConfigs().put("language", YamlConfiguration.loadConfiguration(new File(getDataFolder().getPath() + "//fr_FR.yml")));
        }
        getConfigs().put("config", YamlConfiguration.loadConfiguration(config));
        getConfigs().put("worlds", YamlConfiguration.loadConfiguration(worlds));
        for (int i = 0; i < getConfigs().get("worlds").getStringList("LoadWorlds").size(); i++) {
            loadedWorlds.add(getConfigs().get("worlds").getStringList("LoadWorlds").get(i));
        }
    }

    private void loadWorlds() {
        for (int i = 0; i < loadedWorlds.size(); i++) {
            getMapinfos().put(loadedWorlds.get(i), new MapInformation(
                    getConfigs().get("worlds").getString("Worlds." + loadedWorlds.get(i) + ".type"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".mobs"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".animals"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".weatherCycle"),
                    getConfigs().get("worlds").getString("Worlds." + loadedWorlds.get(i) + ".weather"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".timeCycle"),
                    getConfigs().get("worlds").getLong("Worlds." + loadedWorlds.get(i) + ".time"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".pvp"),
                    getConfigs().get("worlds").getBoolean("Worlds." + loadedWorlds.get(i) + ".forcedGamemode"),
                    getConfigs().get("worlds").getString("Worlds." + loadedWorlds.get(i) + ".defaultGamemode"),
                    getConfigs().get("worlds").getString("Worlds." + loadedWorlds.get(i) + ".difficulty")
            ));
            WorldCreator w = WorldCreator.name(loadedWorlds.get(i));
            if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("normal")) {
                w.type(WorldType.NORMAL);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("end")) {
                w.environment(World.Environment.THE_END);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("amplified")) {
                w.type(WorldType.AMPLIFIED);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("nether")) {
                w.environment(World.Environment.NETHER);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("flat")) {
                w.type(WorldType.FLAT);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getType().equalsIgnoreCase("large_biomes")) {
                w.type(WorldType.LARGE_BIOMES);
            } else w.type(WorldType.NORMAL);
            Bukkit.createWorld(w);
            if (!getMapinfos().get(loadedWorlds.get(i)).isMobSpawning()) {
                Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doMobSpawning", "false");
                for (Entity mobs : Bukkit.getWorld(loadedWorlds.get(i)).getEntities()) {
                    if (mobs instanceof Monster || mobs instanceof Ghast || mobs instanceof Slime || mobs instanceof MagmaCube) {
                        mobs.remove();
                    }
                }
            } else {
                Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doMobSpawning", "true");
            }
            if (!getMapinfos().get(loadedWorlds.get(i)).isDayNight()) {
                Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doDaylightCycle", "false");
                Bukkit.getWorld(loadedWorlds.get(i)).setTime(Main.getMapinfos().get(loadedWorlds.get(i)).getDefaultTime());
            } else {
                Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doDaylightCycle", "true");
            }
            Bukkit.getWorld(loadedWorlds.get(i)).setPVP(getMapinfos().get(loadedWorlds.get(i)).isEnablePVP());
            if (getMapinfos().get(loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("peaceful")) {
                Bukkit.getWorld(loadedWorlds.get(i)).setDifficulty(Difficulty.PEACEFUL);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("easy")) {
                Bukkit.getWorld(loadedWorlds.get(i)).setDifficulty(Difficulty.EASY);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("normal")) {
                Bukkit.getWorld(loadedWorlds.get(i)).setDifficulty(Difficulty.NORMAL);
            } else if (getMapinfos().get(loadedWorlds.get(i)).getDifficulty().equalsIgnoreCase("hard")) {
                Bukkit.getWorld(loadedWorlds.get(i)).setDifficulty(Difficulty.HARD);
            }
            if (!getMapinfos().get(loadedWorlds.get(i)).isAnimalSpawning()) {
                for (Entity mobs : Bukkit.getWorld(loadedWorlds.get(i)).getEntities()) {
                    if (mobs instanceof Animals || mobs instanceof Squid) {
                        mobs.remove();
                    }
                }
            }

            try {
                if (!getMapinfos().get(loadedWorlds.get(i)).isWeatherCycle()) {
                    Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doWeatherCycle", "false");
                    if (getMapinfos().get(loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("sun")) {
                        Bukkit.getWorld(loadedWorlds.get(i)).setStorm(false);
                        Bukkit.getWorld(loadedWorlds.get(i)).setThundering(false);

                    }
                    if (getMapinfos().get(loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("rain")) {

                        Bukkit.getWorld(loadedWorlds.get(i)).setStorm(true);
                        Bukkit.getWorld(loadedWorlds.get(i)).setThundering(false);
                    } else if (getMapinfos().get(loadedWorlds.get(i)).getDefaultWeather().equalsIgnoreCase("storm")) {
                        Bukkit.getWorld(loadedWorlds.get(i)).setStorm(true);
                        Bukkit.getWorld(loadedWorlds.get(i)).setThundering(true);
                    }
                } else {
                    Bukkit.getWorld(loadedWorlds.get(i)).setGameRuleValue("doWeatherCycle", "true");
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }

    public static String getPrefix() {
        return prefix;
    }

    public static Main getInstance() {
        return instance;
    }

    private static final HashMap<String, YamlConfiguration> configs = new HashMap<>();
    private static final HashMap<String, MapInformation> mapinfos = new HashMap<>();

    public static HashMap<String, YamlConfiguration> getConfigs() {
        return configs;
    }

    public static HashMap<String, MapInformation> getMapinfos() {
        return mapinfos;
    }

    public static File getWorlds() {
        return worlds;
    }

    public static File getConfiguration() {
        return config;
    }

    public static void setPrefix(String prefixString) {
        prefix = prefixString;
    }
}

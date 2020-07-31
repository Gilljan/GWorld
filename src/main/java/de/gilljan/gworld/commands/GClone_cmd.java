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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GClone_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gclone")) {
            if (sender.hasPermission("Gworld.clone")) {
                if (args.length == 3) {
                    String worldName = args[0];
                    String targetName = args[1];
                    String type = args[2];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gclone " + worldName + " " + targetName + " " + type + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.confirm_console"));
                } else if (args.length == 4) {
                    String worldName = args[0];
                    String targetName = args[1];
                    String type = args[2];
                    String confirm = args[3];
                    File world = new File(Bukkit.getWorldContainer(), worldName);
                    File target = new File(Bukkit.getWorldContainer(), targetName);
                    if (confirm.equalsIgnoreCase("confirm")) {
                        if (!worldName.equalsIgnoreCase(targetName)) {
                            if (world.exists() && !target.exists() && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                if (!Main.loadedWorlds.contains(targetName) && !Bukkit.getWorlds().contains(Bukkit.getWorld(targetName))) {
                                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.creating").replaceAll("%world%", worldName).replaceAll("%targetworld%", targetName));
                                        Bukkit.getWorld(worldName).save();
                                        try {
                                            FileUtils.copyDirectory(world, target);
                                            new File(target, "uid.dat").delete();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        if (type.equalsIgnoreCase("normal")) {
                                            createWorld(sender, worldName, targetName, WorldType.NORMAL, null);
                                        } else if (type.equalsIgnoreCase("end")) {
                                            createWorld(sender, worldName, targetName, null, World.Environment.THE_END);
                                        } else if (type.equalsIgnoreCase("amplified")) {
                                            createWorld(sender, worldName, targetName, WorldType.AMPLIFIED, null);
                                        } else if (type.equalsIgnoreCase("nether")) {
                                            createWorld(sender, worldName, targetName, null, World.Environment.NETHER);
                                        } else if (type.equalsIgnoreCase("flat")) {
                                            createWorld(sender, worldName, targetName, WorldType.FLAT, null);
                                        } else if (type.equalsIgnoreCase("large_biomes")) {
                                            createWorld(sender, worldName, targetName, WorldType.LARGE_BIOMES, null);
                                        } else
                                            sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.failed").replaceAll("%world%", worldName));
                                    });
                                } else
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.failed"));
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.failed"));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.failed"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.use"));
            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("NoPerm"));
        }
        return false;
    }

    public static void createWorld(CommandSender sender, String worldName, String targetName, WorldType worldType, World.Environment environment) {
        MapInformation.copyMapInfos(worldName, targetName);
        WorldCreator w = WorldCreator.name(targetName);
        if (worldType == null) {
            w.environment(environment);
        } else if (environment == null) {
            w.type(worldType);
        }
        Bukkit.createWorld(w);
        Bukkit.getWorlds().add(Bukkit.getWorld(targetName));
        Main.loadedWorlds.add(targetName);
        Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
        try {
            Main.getConfigs().get("worlds").save(Main.getWorlds());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        MapInformation.setMapValues(worldName);
        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Clone.success").replaceAll("%world%", worldName).replaceAll("%targetworld%", targetName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gclone")) || (args.length == 0) || (!(sender.hasPermission("Gworld.clone"))))
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
        } else if (args.length == 3) {
            options.add("normal");
            options.add("end");
            options.add("nether");
            options.add("flat");
            options.add("amplified");
            options.add("large_biomes");

            String search = args[2].toLowerCase();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).toLowerCase().startsWith(search))
                    tab.add(options.get(i));
            }
        }
        return tab;
    }
}

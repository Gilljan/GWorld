/*
 * Copyright (c) Gilljan 2020-2021. All rights reserved.
 */

package de.gilljan.gworld.commands;

import de.gilljan.gworld.Main;
import de.gilljan.gworld.utils.SendMessage_util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GUnload_cmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("gunload")) {
            if(sender.hasPermission("Gworld.unload")) {
                if(args.length == 1) {
                    String worldName = args[0];
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.confirm_player"));
                        TextComponent tc = new TextComponent();
                        tc.setText("§a[Confirm]");
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gunload " + worldName + " confirm"));
                        p.spigot().sendMessage(tc);
                    } else
                        sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.confirm_console"));
                } else if(args.length == 2) {
                    String worldName = args[0];
                    String confirm = args[1];
                    if(confirm.equalsIgnoreCase("confirm")) {
                        if(!worldName.contains(".") & !worldName.contains("/") & !worldName.equalsIgnoreCase("plugins") & !worldName.equalsIgnoreCase("logs") & !worldName.equalsIgnoreCase("old_maps")) {
                            if(Main.loadedWorlds.contains(worldName) && Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
                                try {
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.unloading").replaceAll("%world%", worldName));
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        if (Bukkit.getWorld(worldName).getEntities().contains(all)) {
                                            all.teleport(Bukkit.getWorld(Main.getConfigs().get("config").getString("MainWorld")).getSpawnLocation());
                                            all.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.teleport_players").replaceAll("%world%", worldName));
                                        }
                                    }
                                    Bukkit.unloadWorld(Bukkit.getWorld(worldName), true);
                                    Bukkit.getWorlds().remove(Bukkit.getWorld(worldName));
                                    Main.loadedWorlds.remove(worldName);
                                    Main.getMapinfos().remove(worldName);
                                    Main.getConfigs().get("worlds").set("LoadWorlds", Main.loadedWorlds);
                                    Main.getConfigs().get("worlds").save(Main.getWorlds());
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.success").replaceAll("%world%", worldName));
                                } catch (Exception ex) {
                                    sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.failed").replaceAll("%world%", worldName));
                                    ex.printStackTrace();
                                }
                            } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.alreadyUnloaded").replaceAll("%world%", worldName));
                        } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("SecurityMessage"));
                    } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.use"));
                } else sender.sendMessage(Main.getPrefix() + SendMessage_util.sendMessage("Unload.use"));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("gunload")) || (!(sender instanceof Player)) || (args.length == 0) || (!(sender.hasPermission("Gworld.unload"))))
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

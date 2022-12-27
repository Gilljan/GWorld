package de.gilljan.gworld.commands;

import de.gilljan.gworld.utils.SendMessage_util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GHelp_cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("ghelp")) {
            return false;
        }

        if(!sender.hasPermission("Gworld.help")) {
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(SendMessage_util.sendMessage("Help.header").replaceAll("%site%", "1").replaceAll("%maxsite%", "3"));
            sender.sendMessage(SendMessage_util.sendMessage("Help.gclone"));
            sender.sendMessage(SendMessage_util.sendMessage("Help.gcreate"));
            sender.sendMessage(SendMessage_util.sendMessage("Help.gdelete"));
            sender.sendMessage(SendMessage_util.sendMessage("Help.gimport"));
        } else if(args.length == 1) {
            switch (args[0]) {
                case "1":
                    sender.sendMessage(SendMessage_util.sendMessage("Help.header").replaceAll("%site%", "1").replaceAll("%maxsite%", "3"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gclone"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gcreate"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gdelete"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gimport"));
                    break;
                case "2":
                    sender.sendMessage(SendMessage_util.sendMessage("Help.header").replaceAll("%site%", "2").replaceAll("%maxsite%", "3"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.ginfo"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gload"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.grecreate"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gset"));
                    break;
                case "3":
                    sender.sendMessage(SendMessage_util.sendMessage("Help.header").replaceAll("%site%", "3").replaceAll("%maxsite%", "3"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gtp"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gunload"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gworlds"));
                    sender.sendMessage(SendMessage_util.sendMessage("Help.gwreload"));
                    break;
                default:
                    sender.sendMessage("Help.use");
                    break;
            }
        }
            return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!cmd.getName().equalsIgnoreCase("ghelp")) || (args.length == 0))
            return null;
        List<String> tab = new ArrayList<>();

        if(args.length == 1) {
            tab.add("1");
            tab.add("2");
            tab.add("3");
        }
        return tab;
    }
}

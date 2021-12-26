package me.cheesybones.afknotifier;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Hashtable;

public class TimeAfkCommand implements CommandExecutor {

    private Main plugin;

    public TimeAfkCommand(Main afknotifier){
        this.plugin = afknotifier;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Hashtable<String, PlayerInfo> playerMoveTimes = plugin.playerWatcher.playerMoveTimes;
        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "You must provide a name of a user for this command.");
            return true;
        }
        if(playerMoveTimes.containsKey(args[0])){
            PlayerInfo playerInfo = playerMoveTimes.get(args[0]);
            if(playerInfo.isAfk){
                sender.sendMessage(ChatColor.AQUA + playerInfo.username + " has been AFK for " + convertMSToReadable(playerInfo.timeSinceLastMove) + ".");
            }else{
                sender.sendMessage(ChatColor.DARK_AQUA + playerInfo.username + " is not AFK.");
            }
        }else{
            sender.sendMessage(ChatColor.RED + args[0] + " does not exist.");
        }
        return true;
    }

    private String convertMSToReadable(long milliseconds){
        long seconds = milliseconds / 1000;
        if(seconds >= 3600){
            long hours = seconds / 3600;
            return hours + " hours";
        }else if(seconds >= 60){
            long minutes = seconds / 60;
            return minutes + " minutes";
        }else{
            return seconds + " seconds";
        }
    }
}

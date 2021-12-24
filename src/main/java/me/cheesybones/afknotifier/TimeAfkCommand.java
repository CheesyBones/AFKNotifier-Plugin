package me.cheesybones.afknotifier;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                int timeAfk = LocalTime.now().toSecondOfDay() - playerInfo.lastMoveTime;
                sender.sendMessage(ChatColor.AQUA + playerInfo.username + " has been AFK for " + timeAfk + " seconds.");
            }else{
                sender.sendMessage(ChatColor.DARK_AQUA + playerInfo.username + " is not AFK.");
            }
        }else{
            sender.sendMessage(ChatColor.RED + args[0] + " does not exist.");
        }
        return true;
    }
}

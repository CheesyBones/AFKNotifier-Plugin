package me.cheesybones.afknotifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Hashtable;
import java.util.List;

public class AfklistCommand implements CommandExecutor {

    private Afknotifier plugin;

    public AfklistCommand(Afknotifier afkNotifier){
        this.plugin = afkNotifier;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Hashtable<String,PlayerInfo> playerMoveTimes = plugin.playerMoveListener.playerMoveTimes;
        List<PlayerInfo> afkPlayers = plugin.playerMoveListener.afkPlayers;
        if(args.length == 0){
            sender.sendMessage(buildAfkPlayerString(afkPlayers));
        }else{
            if(args.length >= plugin.playerMoveListener.playerMoveTimes.size()){
                sender.sendMessage(buildAfkPlayerString(afkPlayers));
            }else{
                StringBuilder sb = new StringBuilder();
                for(String username : args) {
                    if (playerMoveTimes.containsKey(username)) {
                        if (playerMoveTimes.get(username).isAfk) {
                            sb.append("\n" + username + " is AFK");
                        } else {
                            sb.append("\n" + username + " is not AFK");
                        }
                    }
                }
                sender.sendMessage(ChatColor.DARK_AQUA + sb.toString());
            }
        }
        return true;
    }

    private String buildAfkPlayerString(List<PlayerInfo> afkPlayers){
        if (afkPlayers.size() <= 0) {
            return (ChatColor.GREEN + "No players are currently AFK.");
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            for(PlayerInfo playerInfo : afkPlayers){
                stringBuilder.append("\n" + playerInfo.username);
            }
            return (ChatColor.AQUA + "AFK players: " + stringBuilder.toString());
        }

    }
}

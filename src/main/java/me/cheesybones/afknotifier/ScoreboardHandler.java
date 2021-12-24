package me.cheesybones.afknotifier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Enumeration;
import java.util.Hashtable;

public class ScoreboardHandler {
    private Main plugin;
    private PlayerWatcher playerWatcher;
    private FileConfiguration config;

    public ScoreboardHandler(Main main, PlayerWatcher playerWatcher){
        this.plugin = main;
        this.playerWatcher = playerWatcher;
        this.config = plugin.getConfigFile();
    }

    public void startRunnable(){
        if(!config.getBoolean("update-scoreboard-afk")) { return; }
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();

                Team afkTeam = scoreboard.getTeam("AFK");
                if(afkTeam == null){
                    afkTeam = scoreboard.registerNewTeam("AFK");
                }

                afkTeam.setPrefix(ChatColor.BOLD + "" + ChatColor.AQUA + "AFK ");

                Hashtable<String, PlayerInfo> playerMoveTimes = playerWatcher.playerMoveTimes;

                Enumeration<String> e = playerMoveTimes.keys();

                while(e.hasMoreElements()){
                    String playerName = e.nextElement();
                    if(playerMoveTimes.get(playerName).isAfk){
                        afkTeam.addEntry(playerName);
                    }else{
                        afkTeam.removeEntry(playerName);
                    }
                }
            }
        },0,40);
    }

}

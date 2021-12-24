package me.cheesybones.afknotifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class PlayerWatcher {
    private Main plugin;
    private FileConfiguration config;

    public Hashtable<String, PlayerInfo> playerMoveTimes = new Hashtable<String, PlayerInfo>();
    public List<PlayerInfo> afkPlayers = new ArrayList<PlayerInfo>();

    public PlayerWatcher(Main main){
        this.plugin = main;
        this.config = plugin.getConfigFile();
    }

    public PlayerInfo moveUpdatePlayerInfo(PlayerMoveEvent event) {
        int currentTimeSeconds = LocalTime.now().toSecondOfDay();
        String username = event.getPlayer().getName();

        PlayerInfo playerInfo = playerMoveTimes.get(username);
        playerInfo.lastMoveTime = currentTimeSeconds;
        playerInfo.lastAfkCheck = playerInfo.isAfk;
        playerInfo.isAfk = false;

        return playerInfo;
    }

    public void onPlayerReturn(PlayerInfo playerInfo){
        String playerName = playerInfo.username;
        afkPlayers.remove(playerInfo);

        if (config.getBoolean("send-chat-message-return")) {
            plugin.getServer().broadcastMessage(playerName + " has returned.");
        }
    }

    public void onPlayerAfk(PlayerInfo playerInfo){
        String playerName = playerInfo.username;
        afkPlayers.add(playerInfo);

        if (config.getBoolean("send-chat-message-afk")) {
            plugin.getServer().broadcastMessage(playerName + " Is AFK.");
        }
    }

    public void remoteAfk(String playerName,boolean isQuiet){
        PlayerInfo playerInfo = playerMoveTimes.get(playerName);
        playerInfo.isAfk = true;
        playerMoveTimes.put(playerName,playerInfo);
        afkPlayers.add(playerInfo);
        if(!isQuiet){
            plugin.getServer().broadcastMessage(playerName + " Is AFK.");
        }
    }

    public void startPlayerWatcher() {
        FileConfiguration config = plugin.getConfigFile();

        int tickPeriod = config.getInt("check-period") * 20;
        int afkSeconds = config.getInt("afk-seconds");

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {

                Enumeration<String> enumeration = playerMoveTimes.keys();

                while (enumeration.hasMoreElements()) {
                    String playerName = enumeration.nextElement();
                    PlayerInfo playerInfo = playerMoveTimes.get(playerName);
                    playerInfo.timeSinceLastMove = LocalTime.now().toSecondOfDay() - playerInfo.lastMoveTime;
                    playerInfo.lastAfkCheck = playerInfo.isAfk;

                    if (!playerInfo.lastAfkCheck && !playerInfo.isAfk && playerInfo.timeSinceLastMove > afkSeconds) {
                        playerInfo.isAfk = true;
                        playerMoveTimes.put(playerName, playerInfo);
                        System.out.println(playerName + " Is AFK.");
                        onPlayerAfk(playerInfo);

                    } else if (playerInfo.isAfk) {
                        playerMoveTimes.put(playerName, playerInfo);
                    }


                }
            }
        }, 20, tickPeriod);
    }
}

package me.cheesybones.afknotifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class PlayerMoveListener implements Listener {
    public Afknotifier plugin;
    public FileConfiguration config;

    public Hashtable<String, PlayerInfo> playerMoveTimes = new Hashtable<String, PlayerInfo>();
    public List<PlayerInfo> afkPlayers = new ArrayList<PlayerInfo>();

    public PlayerMoveListener(Afknotifier afkNotifier) {
        this.plugin = afkNotifier; this.config = plugin.getConfigFile();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        String playerName = event.getPlayer().getName();
        PlayerInfo playerInfo = moveUpdatePlayerInfo(event);

        if (playerInfo.lastAfkCheck == true) {
            System.out.println(playerName + " has returned.");
            onPlayerReturn(playerInfo);
        }

        playerMoveTimes.put(playerName, playerInfo);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerInfo playerInfo = buildPlayerInfo(event);
        playerMoveTimes.put(playerInfo.username, playerInfo);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        playerMoveTimes.remove(playerName);
    }

    private PlayerInfo buildPlayerInfo(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        int currentTimeSeconds = LocalTime.now().toSecondOfDay();

        PlayerInfo playerInfo = new PlayerInfo(playerName, currentTimeSeconds, false);

        return playerInfo;
    }

    private PlayerInfo moveUpdatePlayerInfo(PlayerMoveEvent event) {
        int currentTimeSeconds = LocalTime.now().toSecondOfDay();
        String username = event.getPlayer().getName();

        PlayerInfo playerInfo = playerMoveTimes.get(username);
        playerInfo.lastMoveTime = currentTimeSeconds;
        playerInfo.lastAfkCheck = playerInfo.isAfk;
        playerInfo.isAfk = false;

        return playerInfo;
    }

    private void onPlayerReturn(PlayerInfo playerInfo){
        String playerName = playerInfo.username;
        afkPlayers.remove(playerInfo);

        if (config.getBoolean("send-chat-message-return") == true) {
            plugin.getServer().broadcastMessage(playerName + " has returned.");
        }
    }

    private void onPlayerAfk(PlayerInfo playerInfo){
        String playerName = playerInfo.username;
        afkPlayers.add(playerInfo);

        if (config.getBoolean("send-chat-message-afk")) {
            plugin.getServer().broadcastMessage(playerName + " Is AFK.");
        }
    }

    public void remoteAfk(String playerName,boolean isQuiet){
        PlayerInfo playerInfo = plugin.playerMoveListener.playerMoveTimes.get(playerName);
        playerInfo.isAfk = true;
        plugin.playerMoveListener.playerMoveTimes.put(playerName,playerInfo);
        afkPlayers.add(playerInfo);
        if(!isQuiet){
            plugin.getServer().broadcastMessage(playerName + " Is AFK.");
        }
    }

    public void startPlayerWatcher() {
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

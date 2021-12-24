package me.cheesybones.afknotifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PlayerListener implements Listener {
    private Main plugin;
    public FileConfiguration config;
    private PlayerWatcher playerWatcher;

    public PlayerListener(Main afkNotifier, PlayerWatcher playerWatcher) {
        this.plugin = afkNotifier; this.config = plugin.getConfigFile();
        this.playerWatcher = playerWatcher;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        String playerName = event.getPlayer().getName();
        PlayerInfo playerInfo = playerWatcher.moveUpdatePlayerInfo(event);

        if (playerInfo.lastAfkCheck == true) {
            System.out.println(playerName + " has returned.");
            playerWatcher.onPlayerReturn(playerInfo);
        }

        playerWatcher.playerMoveTimes.put(playerName, playerInfo);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerInfo playerInfo = buildPlayerInfo(event);
        playerWatcher.playerMoveTimes.put(playerInfo.username, playerInfo);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        playerWatcher.playerMoveTimes.remove(playerName);
    }

    private PlayerInfo buildPlayerInfo(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        int currentTimeSeconds = LocalTime.now().toSecondOfDay();

        PlayerInfo playerInfo = new PlayerInfo(playerName, currentTimeSeconds, false);

        return playerInfo;
    }







}

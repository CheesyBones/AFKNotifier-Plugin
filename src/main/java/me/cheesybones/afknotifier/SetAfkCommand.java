package me.cheesybones.afknotifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAfkCommand implements CommandExecutor {
    private Main plugin;
    private boolean isQuiet;

    public SetAfkCommand(Main plugin, boolean isQuiet){
        this.plugin = plugin;
        this.isQuiet = isQuiet;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getLogger().info(ChatColor.RED + "You must be a player to use that!");
            return false;
        }

        String playerName = sender.getName();

        plugin.playerWatcher.remoteAfk(playerName,isQuiet);

        return true;
    }
}

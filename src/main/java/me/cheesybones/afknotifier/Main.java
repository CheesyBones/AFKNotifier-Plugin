package me.cheesybones.afknotifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private FileConfiguration config = getConfig();
    PlayerWatcher playerWatcher = new PlayerWatcher(this);
    PlayerListener playerListener = new PlayerListener(this,playerWatcher);
    ScoreboardHandler scoreboardHandler = new ScoreboardHandler(this,playerWatcher);

    @Override
    public void onEnable() {
        handleConfig();

        registerEvents();
        registerCommands();

        playerWatcher.startPlayerWatcher();
        scoreboardHandler.startRunnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(playerListener, this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("afklist").setExecutor(new AfklistCommand(this));
        getServer().getPluginCommand("timeafk").setExecutor(new TimeAfkCommand(this));
        getServer().getPluginCommand("setafk").setExecutor(new SetAfkCommand(this,false));
        getServer().getPluginCommand("silentafk").setExecutor(new SetAfkCommand(this,true));
    }

    private void handleConfig() {
        config.options().copyDefaults(true);
        saveConfig();
    }


}

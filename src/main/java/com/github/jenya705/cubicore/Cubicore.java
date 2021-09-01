package com.github.jenya705.cubicore;

import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cubicore extends JavaPlugin {

    @Getter
    private static Cubicore plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        setCommand("worlds", new WorldsCommand());
        setCommand("color", new ColorCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public void setCommand(String command, Object executor) {
        PluginCommand pluginCommand = getCommand(command);
        if (pluginCommand == null) return;
        if (executor instanceof CommandExecutor) pluginCommand.setExecutor((CommandExecutor) executor);
        if (executor instanceof TabExecutor) pluginCommand.setTabCompleter((TabExecutor) executor);
    }

}

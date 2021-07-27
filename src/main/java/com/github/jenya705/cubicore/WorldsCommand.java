package com.github.jenya705.cubicore;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Jenya705
 */
public class WorldsCommand implements CommandExecutor, TabExecutor {

    public static final String[] DEFAULT_ARGS = {"minecraft:overworld", "minecraft:the_nether", "minecraft:the_end"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Cubicore.getPlugin(), () -> {
            AtomicReference<Component> message = new AtomicReference<>(Component.empty());
            Arrays.stream(args.length == 0 ? DEFAULT_ARGS : args).forEach(identifier -> {
                if (identifier.startsWith("minecraft:")) {
                    message.set(message.get().append(
                            world(Bukkit.getWorld(NamespacedKey.fromString(identifier)), identifier).append(Component.newline())
                    ));
                }
                else {
                    message.set(message.get().append(
                            player(Bukkit.getPlayer(identifier), identifier).append(Component.newline())
                    ));
                }
            });
            sender.sendMessage(message.get());
        });
        return true;
    }

    protected Component world(World world, String worldName) {
        if (world != null) return Component.text(
                config().getString("worldLayout")
                        .replaceAll("%world%", config().getString("worlds." + world.getKey()))
                        .replaceAll("%players%", world.getPlayers().stream().map(Player::getName).collect(Collectors.joining(", ")))
                        .replaceAll("&", Character.toString(ChatColor.COLOR_CHAR))
        );
        return Component.text(
                config().getString("worldNotExist")
                        .replaceAll("%world%", worldName)
                        .replaceAll("&", Character.toString(ChatColor.COLOR_CHAR))
        );
    }

    protected Component player(Player player, String name) {
        if (player != null) return Component.text(
                config().getString("playerLayout")
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%world%", config().getString("worlds." + player.getWorld().getKey()))
                        .replaceAll("&", Character.toString(ChatColor.COLOR_CHAR))
        );
        return Component.text(
                config().getString("playerNotExist")
                        .replaceAll("%player%", name)
                        .replaceAll("&", Character.toString(ChatColor.COLOR_CHAR))
        );
    }

    protected Configuration config() {
        return Cubicore.getPlugin().getConfig();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tab = Bukkit
                .getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        tab.addAll(Bukkit
                .getWorlds()
                .stream()
                .map(world -> world.getKey().toString())
                .collect(Collectors.toList()));
        return tab;
    }
}

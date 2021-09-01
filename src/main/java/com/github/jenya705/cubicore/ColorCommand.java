package com.github.jenya705.cubicore;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Jenya705
 */
@AllArgsConstructor
public class ColorCommand implements CommandExecutor, TabExecutor {

    private final static Map<String, String> colors = new HashMap<>();
    static {
        for (ChatColor color: ChatColor.values()) {
            if (color.isColor())
                colors.put(color.name().toLowerCase(Locale.ROOT), "&" + color.getChar());
        }
    }

    private final static Pattern hexColorPattern = Pattern.compile("#[0-9a-f]{6}", Pattern.CASE_INSENSITIVE);

    private final Cubicore cubicore;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length < 1) return true;
        Player player = (Player) sender;
        String color = args[0].toLowerCase(Locale.ROOT) ;
        String endColor = colors.getOrDefault(color, null);
        if (endColor == null) {
            if (color.length() == 7 && hexColorPattern.matcher(color).matches()) {
                color = color.replace("#", "x");
                StringBuilder bukkitColor = new StringBuilder();
                for (char c: color.toCharArray()) {
                    bukkitColor.append("&").append(c);
                }
                player.getPersistentDataContainer()
                        .set(
                                NamespacedKey.fromString("chat_color", cubicore),
                                PersistentDataType.STRING,
                                bukkitColor.toString()
                        );
            }
            else {
                player.sendMessage(cubicore.getConfig().getString("color.notValid"));
            }
        }
        else {
            player.getPersistentDataContainer()
                    .set(
                            NamespacedKey.fromString("chat_color", cubicore),
                            PersistentDataType.STRING,
                            endColor
                    );
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(colors.keySet());
    }
}

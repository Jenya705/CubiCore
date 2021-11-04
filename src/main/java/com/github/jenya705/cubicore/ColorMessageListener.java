package com.github.jenya705.cubicore;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Jenya705
 */
@AllArgsConstructor
public class ColorMessageListener implements PluginMessageListener {

    private final Cubicore cubicore;

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("cubicore:color")) return;
        String color = new String(message);
        StringBuilder bukkitColor = new StringBuilder()
                .append("&")
                .append("x");
        for (char c: color.toCharArray()) {
            bukkitColor.append("&").append(c);
        }
        cubicore.getColors().put(player.getUniqueId(), bukkitColor.toString());
    }

}

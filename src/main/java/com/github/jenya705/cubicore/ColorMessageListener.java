package com.github.jenya705.cubicore;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.format.TextColor;
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
        cubicore.getColors().put(player.getUniqueId(), TextColor.fromHexString("#" + color));
    }

}

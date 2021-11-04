package com.github.jenya705.cubicore;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jenya705
 */
@AllArgsConstructor
public class ChatHandler implements Listener {

    private final Cubicore cubicore;

    @EventHandler
    public void chat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String playerColor = cubicore.getColors().getOrDefault(player.getUniqueId(), "");
        String legacyMessage = LegacyComponentSerializer
                .legacySection()
                .serialize(event.message())
                .replaceAll("\\{\\d}", "")
                .replaceAll("&", "");
        boolean isLocal = !legacyMessage.startsWith("!");
        if (isLocal) {
            int radius = cubicore.getConfig().getInt("chat.radius");
            Set<Audience> viewers = event
                    .viewers()
                    .stream()
                    .filter(it -> {
                        if (!(it instanceof Player anotherPlayer)) return true;
                        return anotherPlayer.getWorld() == player.getWorld() &&
                                Math.abs(anotherPlayer.getLocation().getX() - player.getLocation().getX()) < radius &&
                                Math.abs(anotherPlayer.getLocation().getZ() - player.getLocation().getZ()) < radius;
                    })
                    .collect(Collectors.toSet());
            event.viewers().clear();
            event.viewers().addAll(viewers);
        }
        else {
            legacyMessage = legacyMessage.substring(1);
        }
        Component endMessage = LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(MessageFormat.format(
                        cubicore.getConfig().getString(isLocal ? "chat.local" : "chat.global"),
                        player.getName(),
                        legacyMessage,
                        playerColor
                ));
        event.renderer((source, sourceDisplayName, message, viewer) -> endMessage);
    }
}

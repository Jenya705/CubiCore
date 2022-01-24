package com.github.jenya705.cubicore;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AllArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;
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
        String legacyMessage = LegacyComponentSerializer
                .legacySection()
                .serialize(event.message());
        if (legacyMessage.isEmpty()) {
            event.setCancelled(true);
            return;
        }
        boolean isLocal = !legacyMessage.startsWith("!") ||
                cubicore.getConfig().getBoolean("chat.disableGlobal");
        if (cubicore.getConfig().getInt("chat.radius") > 0) {
            if (isLocal) {
                int radius = cubicore.getConfig().getInt("chat.radius");
                Set<Audience> viewers = event
                        .viewers()
                        .stream()
                        .filter(it -> {
                            if (!(it instanceof Player anotherPlayer)) return true;
                            return anotherPlayer.getWorld().equals(player.getWorld()) &&
                                    Math.abs(anotherPlayer.getLocation().getX() - player.getLocation().getX()) < radius &&
                                    Math.abs(anotherPlayer.getLocation().getZ() - player.getLocation().getZ()) < radius;
                        })
                        .collect(Collectors.toSet());
                event.viewers().clear();
                event.viewers().addAll(viewers);
            }
            else {
                legacyMessage = legacyMessage.substring(1);
                int index = 0;
                for (char c: legacyMessage.toCharArray()) {
                    if (c != ' ') break;
                    index++;
                }
                if (index == legacyMessage.length()) {
                    event.setCancelled(true);
                    return;
                }
                legacyMessage = legacyMessage.substring(index);
            }
        }
        else {
            isLocal = false;
        }
        Component endMessage = Component.empty()
                .append(Component
                        .text(isLocal ? "L " : "")
                        .color(NamedTextColor.GRAY)
                )
                .append(Cubicore.buildPlayerComponent(player))
                .append(Component
                        .text(" > ")
                        .decorate(TextDecoration.BOLD)
                        .color(Objects.requireNonNullElse(
                                cubicore.getColors().get(player.getUniqueId()),
                                NamedTextColor.GRAY
                        ))
                )
                .append(Component.text(legacyMessage));
        event.renderer((source, sourceDisplayName, message, viewer) -> endMessage);
    }
}

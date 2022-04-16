package com.github.jenya705.cubicore;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

/**
 * @author Jenya705
 */
@AllArgsConstructor
public class PlayerHandler implements Listener {

    private final Cubicore cubicore;

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        event.quitMessage(Component.empty());
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
    }

}

package com.github.jenya705.cubicore;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Jenya705
 */
@AllArgsConstructor
public class PlayerHandler implements Listener {

    private final Cubicore cubicore;

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        cubicore.getColors().remove(event.getPlayer().getUniqueId());
    }

}

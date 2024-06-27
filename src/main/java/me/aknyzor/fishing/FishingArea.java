package me.aknyzor.fishing;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingArea implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (!(location.getWorld().getName().contains("world"))) {
            player.sendMessage("Nije moguće pecati u ovoj oblasti");
            event.setCancelled(true);
        }
    }
}

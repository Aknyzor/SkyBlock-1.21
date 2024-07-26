package me.aknyzor.fishing;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

public class FishingAFK implements Listener {

    @EventHandler
    public void onFishing(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();

        if (event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (inv.getItemInMainHand().getType() == Material.FISHING_ROD ||
                    inv.getItemInOffHand().getType() == Material.FISHING_ROD) {
                Block b = player.getTargetBlock(null, 5);

                if (b.getType() != Material.AIR) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
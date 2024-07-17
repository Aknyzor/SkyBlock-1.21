package me.aknyzor.customitems.dracula;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Axe implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isDraculaAxe(item)) {
            applyDraculaAxeEffects(player);
        } else {
            removeDraculaAxeEffects(player);
        }
    }

    private void applyDraculaAxeEffects(Player player) {
        long time = player.getWorld().getTime();
        if (time >= 13000 && time <= 23000) {
            if (!(player.hasPotionEffect(PotionEffectType.SPEED) && player.hasPotionEffect(PotionEffectType.HASTE))) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, PotionEffect.INFINITE_DURATION, 1, true, false));
            }
        } else {
            removeDraculaAxeEffects(player);
        }
    }

    private void removeDraculaAxeEffects(Player player) {
        player.removePotionEffect(PotionEffectType.HASTE);
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    private boolean isDraculaAxe(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_AXE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1;
    }
}

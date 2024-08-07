package me.aknyzor.customitems.attor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Attor implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isAttor(item)) {
            applyAttorEffects(player);
        }
    }

    private void applyAttorEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);
        PotionEffect hasteEffect = player.getPotionEffect(PotionEffectType.HASTE);

        if (time >= 13000 && time <= 23000) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED) || (speedEffect != null && speedEffect.getAmplifier() <= 1)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1, true, false));
            }
            if (!player.hasPotionEffect(PotionEffectType.HASTE) || (hasteEffect != null && hasteEffect.getAmplifier() <= 1)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 400, 1, true, false));
            }
        }
    }

    private boolean isAttor(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_AXE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1;
    }
}
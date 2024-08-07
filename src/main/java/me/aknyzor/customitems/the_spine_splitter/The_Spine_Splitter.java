package me.aknyzor.customitems.the_spine_splitter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class The_Spine_Splitter implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isTheSpineSplitter(item)) {
            applyTheSpineSplitterEffects(player);
        }
    }

    private void applyTheSpineSplitterEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);
        PotionEffect hasteEffect = player.getPotionEffect(PotionEffectType.HASTE);

        if (time >= 0 && time <= 13900) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED) || (speedEffect != null && speedEffect.getAmplifier() <= 0)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 0, true, false));
            }
            if (!player.hasPotionEffect(PotionEffectType.HASTE) || (hasteEffect != null && hasteEffect.getAmplifier() <= 0)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 400, 0, true, false));
            }
        }
    }

    private boolean isTheSpineSplitter(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_AXE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 24;
    }
}

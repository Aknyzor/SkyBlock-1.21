package me.aknyzor.customitems.rhys_wings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Rhys_Wings implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();

        if (isRhysWings(item)) {
            applyRhysWingsEffects(player);
        }
    }

    private void applyRhysWingsEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect nightEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        PotionEffect regEffect = player.getPotionEffect(PotionEffectType.REGENERATION);

        if (time >= 13000 && time <= 23000) {
            if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION) || (nightEffect != null && nightEffect.getAmplifier() <= 0)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0, true, false));
            }
            if (!player.hasPotionEffect(PotionEffectType.REGENERATION) || (regEffect != null && regEffect.getAmplifier() <= 0)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 0, true, false));
            }
        }
    }

    private boolean isRhysWings(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 13;
    }
}

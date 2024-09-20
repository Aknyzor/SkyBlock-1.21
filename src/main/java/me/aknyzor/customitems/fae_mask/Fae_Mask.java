package me.aknyzor.customitems.fae_mask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fae_Mask implements Listener {

    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 0;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getHelmet();

        if (isFaeMask(item)) {
            applyFaeMaskEffects(player);
        }
    }

    private void applyFaeMaskEffects(Player player) {
        applyEffectIfAbsentOrLower(player, PotionEffectType.GLOWING);
        applyEffectIfAbsentOrLower(player, PotionEffectType.FIRE_RESISTANCE);
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isFaeMask(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 23;
    }
}

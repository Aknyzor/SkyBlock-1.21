package me.aknyzor.customitems.shadowsinger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Shadowsinger implements Listener {

    private static final int EFFECT_DURATION = 400;
    private static final int SLOWNESS_AMPLIFIER = 3;
    private static final int MINING_FATIGUE_AMPLIFIER = 0;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isShadowsinger(item)) {
            applyShadowsingerEffects(player);
        }
    }

    private void applyShadowsingerEffects(Player player) {
        applyEffectIfAbsentOrLower(player, PotionEffectType.SLOWNESS, SLOWNESS_AMPLIFIER);
        applyEffectIfAbsentOrLower(player, PotionEffectType.MINING_FATIGUE, MINING_FATIGUE_AMPLIFIER);
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType, int requiredAmplifier) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);
        if (currentEffect == null || currentEffect.getAmplifier() <= requiredAmplifier) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, requiredAmplifier, AMBIENT, PARTICLES));
        }
    }

    private boolean isShadowsinger(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 11;
    }
}

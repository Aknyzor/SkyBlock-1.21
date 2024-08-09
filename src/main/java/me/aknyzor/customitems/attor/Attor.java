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

    private static final int START_TIME = 13000;
    private static final int END_TIME = 23000;
    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 1;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isAttor(item) && isNightTime(player.getWorld().getTime())) {
            applyAttorEffects(player);
        }
    }

    private void applyAttorEffects(Player player) {
        applyEffectIfAbsentOrLower(player, PotionEffectType.SPEED);
        applyEffectIfAbsentOrLower(player, PotionEffectType.HASTE);
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isAttor(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_AXE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1;
    }

    private boolean isNightTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }
}
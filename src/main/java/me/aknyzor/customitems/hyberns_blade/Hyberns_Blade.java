package me.aknyzor.customitems.hyberns_blade;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Hyberns_Blade implements Listener {

    private static final int START_TIME = 0;
    private static final int END_TIME = 13900;
    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 1;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isHybernsBlade(item) && isDayTime(player.getWorld().getTime())) {
            applyHybernsBladeEffects(player);
        }
    }

    private void applyHybernsBladeEffects(Player player) {
        applyEffectIfAbsentOrLower(player, PotionEffectType.STRENGTH);
        applyEffectIfAbsentOrLower(player, PotionEffectType.RESISTANCE);
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isHybernsBlade(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 31;
    }

    private boolean isDayTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }
}

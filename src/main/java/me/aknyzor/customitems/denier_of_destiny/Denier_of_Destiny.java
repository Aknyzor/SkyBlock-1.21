package me.aknyzor.customitems.denier_of_destiny;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Denier_of_Destiny implements Listener {

    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 1;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();

        if (isDenierofDestinyCarver(item)) {
            applyDenierofDestinyEffects(player);
        }
    }

    private void applyDenierofDestinyEffects(Player player) {
        applyEffectIfAbsentOrLower(player);
    }

    private void applyEffectIfAbsentOrLower(Player player) {
        PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.RESISTANCE);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isDenierofDestinyCarver(ItemStack item) {
        if (item == null || item.getType() != Material.SHIELD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 29;
    }
}

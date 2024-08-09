package me.aknyzor.customitems.archeron_wings;

import me.aknyzor.customitems.Hearts;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archeron_Wings implements Listener {

    private static final int START_TIME = 0;
    private static final int END_TIME = 13900;
    private static final int EFFECT_DURATION = 400;
    private static final int LUCK_AMPLIFIER = 1;
    private static final int REGENERATION_AMPLIFIER = 0;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;
    private static final String ATTRIBUTE_KEY = "skyblock:archeron_wings";

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();

        boolean hasAttribute = Hearts.hasAttributes(player);

        if (isArcheronWings(item)) {
            applyArcheronWingsEffects(player, hasAttribute);
        } else if (hasAttribute) {
            Hearts.removeAttribute(player, ATTRIBUTE_KEY);
        }
    }

    private void applyArcheronWingsEffects(Player player, boolean hasAttribute) {
        long time = player.getWorld().getTime();

        if (isDayTime(time)) {
            applyEffectIfAbsentOrLower(player, PotionEffectType.REGENERATION, REGENERATION_AMPLIFIER);
            applyEffectIfAbsentOrLower(player, PotionEffectType.LUCK, LUCK_AMPLIFIER);

            if (!hasAttribute) {
                Hearts.addAttribute(player, 8.0, ATTRIBUTE_KEY);
            }
        } else if (hasAttribute) {
            Hearts.removeAttribute(player, ATTRIBUTE_KEY);
        }
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType, int requiredAmplifier) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);
        if (currentEffect == null || currentEffect.getAmplifier() <= requiredAmplifier) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, requiredAmplifier, AMBIENT, PARTICLES));
        }
    }

    private boolean isArcheronWings(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 32;
    }

    private boolean isDayTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }
}

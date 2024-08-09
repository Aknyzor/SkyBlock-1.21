package me.aknyzor.customitems.illyrian_wings;

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

public class Illyrian_Wings implements Listener {

    private static final int START_TIME = 13000;
    private static final int END_TIME = 23000;
    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 0;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;
    private static final String ATTRIBUTE_KEY = "skyblock:illyrian_wings";

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();

        boolean hasAttribute = Hearts.hasAttributes(player);

        if (isRhysWings(item)) {
            applyRhysWingsEffects(player, hasAttribute);
        } else if (hasAttribute) {
            Hearts.removeAttribute(player, ATTRIBUTE_KEY);
        }
    }

    private void applyRhysWingsEffects(Player player, boolean hasAttribute) {
        long time = player.getWorld().getTime();

        if (isNightTime(time)) {
            applyEffectIfAbsentOrLower(player, PotionEffectType.NIGHT_VISION);
            applyEffectIfAbsentOrLower(player, PotionEffectType.REGENERATION);

            if (!hasAttribute) {
                Hearts.addAttribute(player, 8.0, ATTRIBUTE_KEY);
            }
        } else if (hasAttribute) {
            Hearts.removeAttribute(player, ATTRIBUTE_KEY);
        }
    }

    private void applyEffectIfAbsentOrLower(Player player, PotionEffectType effectType) {
        PotionEffect currentEffect = player.getPotionEffect(effectType);
        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(effectType, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isRhysWings(ItemStack item) {
        if (item == null || item.getType() != Material.PAPER) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 13;
    }

    private boolean isNightTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }
}
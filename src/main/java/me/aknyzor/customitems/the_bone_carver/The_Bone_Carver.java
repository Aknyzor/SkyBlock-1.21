package me.aknyzor.customitems.the_bone_carver;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class The_Bone_Carver implements Listener {

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

        if (isTheBoneCarver(item) && isNightTime(player.getWorld().getTime())) {
            applyTheBoneCarverEffects(player);
        }
    }

    private void applyTheBoneCarverEffects(Player player) {
        applyEffectIfAbsentOrLower(player);
    }

    private void applyEffectIfAbsentOrLower(Player player) {
        PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.STRENGTH);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    private boolean isTheBoneCarver(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 10;
    }

    private boolean isNightTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }
}

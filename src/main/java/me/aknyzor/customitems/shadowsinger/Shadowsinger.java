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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isShadowsinger(item)) {
            applyShadowsinger(player);
        }
    }

    private void applyShadowsinger(Player player) {
        PotionEffect slownessEffect = player.getPotionEffect(PotionEffectType.SLOWNESS);
        PotionEffect miningEffect = player.getPotionEffect(PotionEffectType.MINING_FATIGUE);

        if (!player.hasPotionEffect(PotionEffectType.SLOWNESS) || (slownessEffect != null && slownessEffect.getAmplifier() <= 3)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 3, true, false));
        }
        if (!player.hasPotionEffect(PotionEffectType.MINING_FATIGUE) || (miningEffect != null && miningEffect.getAmplifier() <= 0)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 400, 0, true, false));
        }
    }

    private boolean isShadowsinger(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 11;
    }
}

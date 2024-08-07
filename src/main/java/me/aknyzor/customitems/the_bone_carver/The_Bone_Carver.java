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

import java.util.Objects;

public class The_Bone_Carver implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isTheBoneCarver(item)) {
            applyTheBoneCarverEffects(player);
        }
    }

    private void applyTheBoneCarverEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect strEffect = player.getPotionEffect(PotionEffectType.STRENGTH);

        if (time >= 13000 && time <= 23000) {
            if (!player.hasPotionEffect(PotionEffectType.STRENGTH) || (strEffect != null && strEffect.getAmplifier() <= 1)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1, true, false));
            }
        }
    }

    private boolean isTheBoneCarver(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 10;
    }
}

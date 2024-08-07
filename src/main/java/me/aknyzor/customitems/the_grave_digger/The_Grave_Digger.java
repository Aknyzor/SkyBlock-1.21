package me.aknyzor.customitems.the_grave_digger;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;


public class The_Grave_Digger implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isTheGraveDigger(item)) {
            applyTheGraveDiggerEffects(player);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (isTheGraveDigger(item) && player.getGameMode() != GameMode.CREATIVE) {
            if (event.getBlock().getType() == Material.DIRT) {
                event.setDropItems(false);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.COARSE_DIRT));
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GRAVEL));
            }
        }
    }

    private void applyTheGraveDiggerEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);

        if (time >= 13000 && time <= 23000) {
            if (!player.hasPotionEffect(PotionEffectType.SPEED) || (speedEffect != null && speedEffect.getAmplifier() <= 1)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1, true, false));
            }
        }
    }

    private boolean isTheGraveDigger(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SHOVEL) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 3;
    }
}

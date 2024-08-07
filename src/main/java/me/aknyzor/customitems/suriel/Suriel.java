package me.aknyzor.customitems.suriel;

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

import java.util.Collection;
import java.util.Objects;

public class Suriel implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isSuriel(item)) {
            applySurielEffects(player);
        }
    }

    private void applySurielEffects(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect nightEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);

        if (time >= 13000 && time <= 23000) {
            if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION) || (nightEffect != null && nightEffect.getAmplifier() <= 1)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 1, true, false));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getBlock().getType().toString().endsWith("_ORE")) {
            if (isSuriel(item) && player.hasPermission("dracula.pickaxe")) {
                Collection<ItemStack> drops = event.getBlock().getDrops(item);
                event.setDropItems(false);
                if (!drops.isEmpty()) {
                    for (ItemStack drop : drops) {
                        int newAmount = (int) Math.ceil(drop.getAmount() * 1.5);
                        ItemStack newDrop = new ItemStack(drop.getType(), newAmount);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), newDrop);
                    }
                }
            }
        }
    }

    private boolean isSuriel(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_PICKAXE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 2;
    }
}

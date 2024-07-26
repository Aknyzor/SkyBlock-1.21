package me.aknyzor.customitems.suriel;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class Suriel implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isSuriel(item)) {
            applySurielEffects(player);
        } else {
            removeSurielEffects(player);
        }
    }

    private void applySurielEffects(Player player) {
        long time = player.getWorld().getTime();
        if (time >= 13000 && time <= 23000) {
            if (!(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1, true, false));
            }
        } else {
            removeSurielEffects(player);
        }
    }

    private void removeSurielEffects(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @EventHandler
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

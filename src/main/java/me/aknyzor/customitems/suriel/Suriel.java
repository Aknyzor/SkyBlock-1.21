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
import java.util.EnumSet;
import java.util.Set;

public class Suriel implements Listener {

    private static final int START_TIME = 13000;
    private static final int END_TIME = 23000;
    private static final int EFFECT_DURATION = 400;
    private static final int EFFECT_AMPLIFIER = 0;
    private static final boolean AMBIENT = true;
    private static final boolean PARTICLES = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isSuriel(item) && isNightTime(player.getWorld().getTime())) {
            applySurielEffects(player);
        }
    }

    private void applySurielEffects(Player player) {
        applyEffectIfAbsentOrLower(player);
    }

    private void applyEffectIfAbsentOrLower(Player player) {
        PotionEffect currentEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);

        if (currentEffect == null || currentEffect.getAmplifier() <= EFFECT_AMPLIFIER) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, EFFECT_DURATION, EFFECT_AMPLIFIER, AMBIENT, PARTICLES));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (ORES.contains(event.getBlock().getType())) {
            if (isSuriel(item) && player.hasPermission("dracula.pickaxe")) {
                Collection<ItemStack> drops = event.getBlock().getDrops(item);
                event.setDropItems(false);
                if (!drops.isEmpty()) {
                    for (ItemStack drop : drops) {
                        int originalAmount = drop.getAmount();
                        int newAmount = (int) Math.ceil(originalAmount * 1.5);
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

    private boolean isNightTime(long time) {
        return time >= START_TIME && time <= END_TIME;
    }

    private static final Set<Material> ORES = EnumSet.of(
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.NETHER_QUARTZ_ORE,
            Material.NETHER_GOLD_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE
    );
}

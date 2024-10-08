package me.aknyzor.zabrane;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Zabrane implements Listener {

    @EventHandler
    public void onArrow(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof ArmorStand || event.getHitEntity() instanceof GlowItemFrame ||
                event.getHitEntity() instanceof ItemFrame) {
            if (event.getEntity() instanceof Arrow) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            if (event.getBlock().getWorld().getName().equals("SuperiorWorld") ||
                    event.getBlock().getWorld().getName().equals("world")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Material item = event.getRecipe().getResult().getType();

        if (isItems(item)) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("§cNije moguće napraviti ovaj item/block!");
        }
    }

    @EventHandler
    public void onCrafter(CrafterCraftEvent event) {
        Material item = event.getRecipe().getResult().getType();

        if (isItems(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            if (event.getEntity().getLocation().getWorld().getName().equals("SuperiorWorld")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFeed(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();

        if (event.getRightClicked() instanceof Dolphin) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (hand.getType() == Material.COD || hand.getType() == Material.SALMON) {
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }else if (event.getHand() == EquipmentSlot.HAND) {
                if (offhand.getType() == Material.COD || offhand.getType() == Material.SALMON) {
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    private static final ImmutableSet<Material> items = Sets.immutableEnumSet(
            Material.COARSE_DIRT,
            Material.TNT_MINECART
    );

    private boolean isItems(final Material mat) {
        return items.contains(mat);
    }
}

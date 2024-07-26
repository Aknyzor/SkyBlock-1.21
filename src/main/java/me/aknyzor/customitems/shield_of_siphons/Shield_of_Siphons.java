package me.aknyzor.customitems.shield_of_siphons;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Shield_of_Siphons implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();
            ItemStack item = player.getInventory().getItemInOffHand();

            if (isShieldofSiphons(item) && entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                PotionEffect effect = new PotionEffect(PotionEffectType.WITHER, 60, 0);
                livingEntity.addPotionEffect(effect);
            }
        }
    }

    private boolean isShieldofSiphons(ItemStack item) {
        if (item == null || item.getType() != Material.SHIELD) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 5;
    }
}

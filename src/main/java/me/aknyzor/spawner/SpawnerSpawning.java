package me.aknyzor.spawner;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SpawnerSpawning implements Listener {

    private static final Random RAND = new Random();
    private static final int DROP_CHANCE = 35;
    private static final int RARE_DROP_CHANCE = 2;

    @EventHandler
    public void onMobSpawn(SpawnerSpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).setHealth(0);

            if (shouldDropItems()) {
                switch (entity.getType()) {
                    case ZOMBIE -> dropZombieItems(entity);
                    case SKELETON -> dropSkeletonItems(entity);
                    case CREEPER -> dropCreeperItems(entity);
                    case SPIDER, CAVE_SPIDER -> dropSpiderItems(entity);
                    case ENDERMAN -> dropEndermanItems(entity);
                    case IRON_GOLEM -> dropIronGolemItems(entity);
                    case BLAZE -> dropBlazeItems(entity);
                    case MAGMA_CUBE -> dropMagmaCubeItems(entity);
                    case WITHER_SKELETON -> dropWitherSkeletonItems(entity);
                    case WITCH -> dropWitchItems(entity);
                }
            }
        }
    }

    private boolean shouldDropItems() {
        return RAND.nextInt(100) < DROP_CHANCE;
    }

    private void dropZombieItems(Entity entity) {
        dropItem(entity, Material.ROTTEN_FLESH, 3);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropSkeletonItems(Entity entity) {
        dropItem(entity, Material.BONE, 3);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
        dropItem(entity, Material.ARROW, 2);
    }

    private void dropCreeperItems(Entity entity) {
        dropItem(entity, Material.GUNPOWDER, 3);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropSpiderItems(Entity entity) {
        dropItem(entity, Material.STRING, 3);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
        dropItem(entity, Material.SPIDER_EYE, 1);
    }

    private void dropEndermanItems(Entity entity) {
        dropItem(entity, Material.ENDER_PEARL, 1);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropIronGolemItems(Entity entity) {
        dropItem(entity, Material.IRON_INGOT, 3);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
        dropItem(entity, Material.POPPY, 1);
    }

    private void dropBlazeItems(Entity entity) {
        dropItem(entity, Material.BLAZE_ROD, 2);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropMagmaCubeItems(Entity entity) {
        dropItem(entity, Material.MAGMA_CREAM, 2);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropWitherSkeletonItems(Entity entity) {
        dropItem(entity, Material.BONE, 3);
        dropItem(entity, Material.COAL, 2);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);

        if (RAND.nextInt(100) < RARE_DROP_CHANCE) {
            dropItem(entity, Material.WITHER_SKELETON_SKULL, 1);
        }
    }

    private void dropWitchItems(Entity entity) {
        List<ItemStack> possibleDrops = Arrays.asList(
                new ItemStack(Material.GLOWSTONE_DUST, 2),
                new ItemStack(Material.REDSTONE, 2),
                new ItemStack(Material.GUNPOWDER, 2),
                new ItemStack(Material.SPIDER_EYE, 1)
        );
        ItemStack randomDrop = possibleDrops.get(RAND.nextInt(possibleDrops.size()));
        dropItem(entity, randomDrop);
        dropItem(entity, Material.EXPERIENCE_BOTTLE, 1);
    }

    private void dropItem(Entity entity, Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);
        dropItem(entity, item);
    }

    private void dropItem(Entity entity, ItemStack item) {
        entity.getLocation().getWorld().dropItem(entity.getLocation(), item);
    }
}
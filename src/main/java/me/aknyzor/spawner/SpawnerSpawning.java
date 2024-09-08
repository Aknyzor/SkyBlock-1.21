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

    @EventHandler
    public void onMobSpawn(SpawnerSpawnEvent event) {
       Random rand = new Random();
       int number = rand.nextInt(100);

        EntityType entityType = event.getEntity().getType();

        switch (entityType) {
            case ZOMBIE -> {
                ((Zombie) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack rotten = new ItemStack(Material.ROTTEN_FLESH, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), rotten);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
            case SKELETON -> {
                ((Skeleton) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack bone = new ItemStack(Material.BONE, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), bone);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                    ItemStack arrow = new ItemStack(Material.ARROW, 2);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), arrow);
                }
            }
            case CREEPER -> {
                ((Creeper) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack gunpowder = new ItemStack(Material.GUNPOWDER, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), gunpowder);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
            case SPIDER, CAVE_SPIDER -> {
                ((Spider) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack string = new ItemStack(Material.STRING, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), string);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                    ItemStack eye = new ItemStack(Material.SPIDER_EYE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), eye);
                }
            }
            case ENDERMAN -> {
                ((Enderman) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), enderPearl);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
            case IRON_GOLEM -> {
                ((IronGolem) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack iron = new ItemStack(Material.IRON_INGOT, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), iron);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                    ItemStack poppy = new ItemStack(Material.POPPY, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), poppy);
                }
            }
            case BLAZE -> {
                ((Blaze) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD, 2);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), blazeRod);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
            case MAGMA_CUBE -> {
                ((MagmaCube) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack magmaCream = new ItemStack(Material.MAGMA_CREAM, 2);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), magmaCream);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
            case WITHER_SKELETON -> {
                ((WitherSkeleton) event.getEntity()).setHealth(0);
                if (number < 35) {
                    ItemStack bone = new ItemStack(Material.BONE, 3);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), bone);
                    ItemStack coal = new ItemStack(Material.COAL, 2);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), coal);
                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
                if (number < 2.5) {
                    ItemStack skull = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), skull);
                }
            }
            case WITCH -> {
                ((Witch) event.getEntity()).setHealth(0);
                if (number < 35) {
                    List<ItemStack> possibleDrops = Arrays.asList(
                            new ItemStack(Material.GLOWSTONE_DUST, 2),
                            new ItemStack(Material.REDSTONE, 2),
                            new ItemStack(Material.GUNPOWDER, 2),
                            new ItemStack(Material.SPIDER_EYE, 1)
                    );

                    Random randDrop = new Random();
                    ItemStack randomDrop = possibleDrops.get(randDrop.nextInt(possibleDrops.size()));
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), randomDrop);

                    ItemStack exp = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
                    event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), exp);
                }
            }
        }
    }
}

package me.aknyzor.fishing;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomCaught implements Listener {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     *
     */

    private final Random random = new Random();
    private final Map<Category, List<LootItem>> lootTable = new HashMap<>();
    private final Map<Category, Double> categoryChances = new HashMap<>();

    public CustomCaught() {
        registerLoot();
        registerCategoryChances();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (event.getCaught() instanceof Item) {
                Item caught = (Item) event.getCaught();
                Category category = selectCategoryBasedOnChance();
                if (category != null) {
                    List<LootItem> items = lootTable.get(category);
                    if (items != null && !items.isEmpty()) {
                        LootItem selected = selectRandomItem(items);
                        if (selected != null) {
                            ItemStack item = new ItemStack(selected.getMaterial());

                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                meta.displayName(Component.text(selected.getName()));

                                List<Component> loreLines = new ArrayList<>();
                                loreLines.add(Component.text(selected.getLore()));
                                meta.lore(loreLines);

                                item.setItemMeta(meta);
                            }

                            caught.setItemStack(item);

                            Player player = event.getPlayer();
                            player.sendMessage(Component.text("Upecao si: ").append(Component.text(selected.getName())));
                        }
                    }
                }
            }
        }
    }

    private void registerLoot() {
        lootTable.put(Category.TREASURE, Arrays.asList(
                new LootItem(Material.DIAMOND, "Shiny Diamond", "A precious diamond"),
                new LootItem(Material.GOLD_INGOT, "Golden Ingot", "A valuable gold ingot")
        ));

        lootTable.put(Category.FISH, Arrays.asList(
                new LootItem(Material.COD, "Cod", "A fresh catch of cod"),
                new LootItem(Material.SALMON, "Salmon", "A tasty salmon fish")
        ));

        lootTable.put(Category.JUNK, Arrays.asList(
                new LootItem(Material.BOWL, "Bowl", "An empty bowl"),
                new LootItem(Material.STICK, "Stick", "Just a stick")
        ));

        lootTable.put(Category.SPECIAL, Arrays.asList(
                new LootItem(Material.EMERALD, "Emerald", "A rare emerald"),
                new LootItem(Material.NAME_TAG, "Name Tag", "A useful name tag")
        ));
    }

    private void registerCategoryChances() {
        categoryChances.put(Category.TREASURE, 0.15);
        categoryChances.put(Category.FISH, 0.7); // 70% šansa
        categoryChances.put(Category.JUNK, 0.1);
        categoryChances.put(Category.SPECIAL, 0.05);
    }

    private Category selectCategoryBasedOnChance() {
        double totalWeight = categoryChances.values().stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (Map.Entry<Category, Double> entry : categoryChances.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                return entry.getKey();
            }
        }

        return null;
    }

    private LootItem selectRandomItem(List<LootItem> items) {
        int randomIndex = random.nextInt(items.size());
        return items.get(randomIndex);
    }

    public enum Category {
        TREASURE,
        FISH,
        JUNK,
        SPECIAL
    }

    public static class LootItem {
        private final Material material;
        private final String name;
        private final String lore;

        public LootItem(Material material, String name, String lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }

        public Material getMaterial() {
            return material;
        }

        public String getName() {
            return name;
        }

        public String getLore() {
            return lore;
        }
    }
}
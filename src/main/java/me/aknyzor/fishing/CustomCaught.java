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
     * I dalje u izradi!!!!
     *
     */
    private final Random random = new Random();

    private final Map<Category, List<LootItem>> lootTable = new HashMap<>();

    public CustomCaught() {
        registerLoot();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (event.getCaught() instanceof Item) {
                Item caught = (Item) event.getCaught();
                CustomCaught.Category category = getCategoryFromFishingEvent(event);
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
                new LootItem(Material.DIAMOND, 0.5, "Shiny Diamond", "A precious diamond"), // 50% šanse
                new LootItem(Material.GOLD_INGOT, 0.10, "Golden Ingot", "A valuable gold ingot")
        ));

        lootTable.put(Category.FISH, Arrays.asList(
                new LootItem(Material.COD, 0.15, "Cod", "A fresh catch of cod"),
                new LootItem(Material.SALMON, 0.15, "Salmon", "A tasty salmon fish")
        ));

        lootTable.put(Category.JUNK, Arrays.asList(
                new LootItem(Material.BOWL, 0.05, "Bowl", "An empty bowl"),
                new LootItem(Material.STICK, 0.10, "Stick", "Just a stick")
        ));

        lootTable.put(Category.SPECIAL, Arrays.asList(
                new LootItem(Material.EMERALD, 0.08, "Emerald", "A rare emerald"),
                new LootItem(Material.NAME_TAG, 0.03, "Name Tag", "A useful name tag")
        ));
    }

    private CustomCaught.Category getCategoryFromFishingEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (event.getCaught() instanceof Item) {
                Item caught = (Item) event.getCaught();

                switch (caught.getItemStack().getType()) {
                    case COD:
                    case SALMON:
                        return CustomCaught.Category.FISH;
                    case DIAMOND:
                    case GOLD_INGOT:
                        return CustomCaught.Category.TREASURE;
                    case BOWL:
                    case STICK:
                        return CustomCaught.Category.JUNK;
                    case EMERALD:
                    case NAME_TAG:
                        return CustomCaught.Category.SPECIAL;
                    default:
                        return null;
                }
            }
        }
        return null;
    }

    private LootItem selectRandomItem(List<LootItem> items) {
        double totalWeight = items.stream().mapToDouble(LootItem::getChance).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (LootItem item : items) {
            cumulativeWeight += item.getChance();
            if (randomValue < cumulativeWeight) {
                return item;
            }
        }

        return null;
    }

    public enum Category {
        TREASURE,
        FISH,
        JUNK,
        SPECIAL
    }

    public static class LootItem {
        private final Material material;
        private final double chance;
        private final String name;
        private final String lore;

        public LootItem(Material material, double chance, String name, String lore) {
            this.material = material;
            this.chance = chance;
            this.name = name;
            this.lore = lore;
        }

        public Material getMaterial() {
            return material;
        }

        public double getChance() {
            return chance;
        }

        public String getName() {
            return name;
        }

        public String getLore() {
            return lore;
        }
    }
}
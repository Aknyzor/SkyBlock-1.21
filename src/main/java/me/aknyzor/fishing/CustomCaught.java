package me.aknyzor.fishing;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomCaught implements Listener {

    /**
     *
     * I dalje je u izradi (Aknyzor)
     * 29.6.2024. Podesiti special fish
     * 29.6.2024. Podesiti šanse
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

                                if (selected.getName() != null) {
                                    meta.displayName(Component.text(selected.getName()));
                                }

                                if (category == Category.SPECIAL) {
                                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                }

                                if (selected.getLore() != null) {
                                    List<Component> loreLines = new ArrayList<>();
                                    loreLines.add(Component.text(selected.getLore()));
                                    meta.lore(loreLines);
                                }

                                item.setItemMeta(meta);
                            }

                            caught.setItemStack(item);

                            if (category == Category.SPECIAL) {
                                Player ribolovac = event.getPlayer();
                                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                    player.sendMessage(ribolovac.getName() + " caught a " + selected.getName() + "! " + selected.getLore().replaceAll("\n", "").replace("§8Found In: §9WATER", ""));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void registerLoot() {
        lootTable.put(Category.FISH, Arrays.asList(
                new LootItem(Material.TROPICAL_FISH, null, null),
                new LootItem(Material.COD, null, null),
                new LootItem(Material.PUFFERFISH, null, null),
                new LootItem(Material.SALMON, null, null)
        ));

        lootTable.put(Category.TREASURE, Arrays.asList(
                new LootItem(Material.COMPASS, null, null),
                new LootItem(Material.EMERALD, null, null),
                new LootItem(Material.GOLDEN_PICKAXE, null, null),
                new LootItem(Material.SADDLE, null, null),
                new LootItem(Material.NAME_TAG, null, null),
                new LootItem(Material.BLAZE_ROD, null, null),
                new LootItem(Material.MAGMA_CREAM, null, null),
                new LootItem(Material.BONE, null, null),
                new LootItem(Material.IRON_SWORD, null, null)
        ));

        lootTable.put(Category.JUNK, Arrays.asList(
                new LootItem(Material.BOWL, null, null),
                new LootItem(Material.INK_SAC, null, null),
                new LootItem(Material.LEATHER, null, null),
                new LootItem(Material.LILY_PAD, null, null),
                new LootItem(Material.RABBIT_HIDE, null, null),
                new LootItem(Material.FEATHER, null, null),
                new LootItem(Material.ROTTEN_FLESH, null, null),
                new LootItem(Material.STRING, null, null),
                new LootItem(Material.COAL, null, null)
        ));

        lootTable.put(Category.SPECIAL, Arrays.asList(
                new LootItem(Material.TROPICAL_FISH, "§e§lNemo", "§8Found In: §9WATER\n\n§7Maybe he's lost again?"),
                new LootItem(Material.SLIME_BALL, "§e§lKnockback Slimeball", "§8Found In: §9WATER\n\n§7So this is where\nit's been all this time?"),
                new LootItem(Material.BAKED_POTATO, "§e§lHot Potato", "§8Found In: §9WATER\n\n§7Would you look at that?"),
                new LootItem(Material.QUARTZ, "§e§lBarnacle", "§8Found In: §9WATER\n\n§7It really grows on you.")
        ));
    }

    private void registerCategoryChances() {
        categoryChances.put(Category.FISH, 0.7); // 70% šansa
        categoryChances.put(Category.TREASURE, 0.15);
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
        FISH,
        TREASURE,
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
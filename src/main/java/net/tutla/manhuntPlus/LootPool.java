package net.tutla.manhuntPlus;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LootPool {
    private final List<LootEntry> entries = new ArrayList<>();
    private final Random random = new Random();
    private static final Random staticRandom = new Random();

    public void addLoot(ItemStack item, double weight) {
        entries.add(new LootEntry(item, weight));
    }

    public ItemStack getRandomLoot() {
        double totalWeight = entries.stream().mapToDouble(e -> e.weight).sum();
        double r = random.nextDouble() * totalWeight;
        double count = 0;

        for (LootEntry entry : entries) {
            count += entry.weight;
            if (r <= count) {
                return entry.item.clone(); // clone to avoid modifying originals
            }
        }
        return null;
    }

    private static class LootEntry {
        ItemStack item;
        double weight;

        LootEntry(ItemStack item, double weight) {
            this.item = item;
            this.weight = weight;
        }
    }

    public static LootPool createDefault() {
        LootPool pool = new LootPool();

        pool.addLoot(new ItemStack(Material.TOTEM_OF_UNDYING), 0.1);
        pool.addLoot(enchant(new ItemStack(Material.DIAMOND_SWORD)), 0.3);
        pool.addLoot(enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), 0.3);
        pool.addLoot(enchant(new ItemStack(Material.NETHERITE_AXE)), 0.2);
        pool.addLoot(enchant(new ItemStack(Material.NETHERITE_HELMET)), 0.2);
        pool.addLoot(new ItemStack(Material.GOLDEN_APPLE, 4), 0.4);
        pool.addLoot(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 0.1);
        pool.addLoot(new ItemStack(Material.NETHERITE_INGOT, 2), 0.3);
        pool.addLoot(new ItemStack(Material.DIAMOND_BLOCK), 0.2);
        pool.addLoot(new ItemStack(Material.GOLD_BLOCK, 2), 0.3);
        pool.addLoot(new ItemStack(Material.NETHERITE_BLOCK), 0.05);
        pool.addLoot(new ItemStack(Material.EXPERIENCE_BOTTLE, 32), 0.5);
        pool.addLoot(new ItemStack(Material.EMERALD, 12), 0.4);
        pool.addLoot(new ItemStack(Material.ENDER_PEARL, 8), 0.3);
        pool.addLoot(new ItemStack(Material.OBSIDIAN, 16), 0.4);
        pool.addLoot(new ItemStack(Material.TRIDENT), 0.1);
        pool.addLoot(enchant(new ItemStack(Material.SHIELD)), 0.25);
        pool.addLoot(new ItemStack(Material.FIREWORK_ROCKET, 32), 0.3);
        pool.addLoot(new ItemStack(Material.GOLDEN_CARROT, 16), 0.5);
        pool.addLoot(new ItemStack(Material.ELYTRA), 0.05);

        return pool;
    }

    private static ItemStack enchant(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.PROTECTION, staticRandom.nextInt(4) + 1, true);
            meta.addEnchant(Enchantment.UNBREAKING, staticRandom.nextInt(3) + 1, true);
            if (staticRandom.nextBoolean()) {
                meta.addEnchant(Enchantment.MENDING, 1, true);
            }
            if (item.getType().toString().contains("SWORD") || item.getType().toString().contains("AXE")) {
                meta.addEnchant(Enchantment.SHARPNESS, staticRandom.nextInt(6) + 1, true);
            }
            if (item.getType() == Material.SHIELD) {
                meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

}

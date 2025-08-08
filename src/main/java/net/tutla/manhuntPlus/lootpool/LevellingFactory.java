package net.tutla.manhuntPlus.lootpool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LevellingFactory {
    public static List<LootTier> createAllTiers() {
        List<LootTier> tiers = new ArrayList<>();

        tiers.add(new LootTier("basic", createBasicPool()));
        tiers.add(new LootTier("iron", createIronPool()));
        tiers.add(new LootTier("gold", createGoldPool()));
        tiers.add(new LootTier("diamond", createDiamondPool()));
        tiers.add(new LootTier("enchanted", createEnchantedPool()));
        tiers.add(new LootTier("netherite", createNetheritePool()));
        tiers.add(new LootTier("god", createGodPool()));

        return tiers;
    }

    private static LootPool createBasicPool() {
        LootPool pool = new LootPool();
        pool.addLoot(new ItemStack(Material.WOODEN_SWORD), 0.4);
        pool.addLoot(new ItemStack(Material.STONE_SWORD), 0.3);
        pool.addLoot(new ItemStack(Material.WOODEN_AXE), 0.2);
        pool.addLoot(new ItemStack(Material.STONE_AXE), 0.2);
        pool.addLoot(new ItemStack(Material.BREAD, 3), 0.6);
        pool.addLoot(new ItemStack(Material.COOKED_BEEF, 3), 0.5);
        pool.addLoot(new ItemStack(Material.BIRCH_BOAT, 1), 0.1);
        return pool;
    }

    private static LootPool createIronPool() {
        LootPool pool = new LootPool();
        pool.addLoot(new ItemStack(Material.IRON_SWORD), 0.4);
        pool.addLoot(new ItemStack(Material.IRON_PICKAXE), 0.2);
        pool.addLoot(new ItemStack(Material.IRON_CHESTPLATE), 0.2);
        pool.addLoot(new ItemStack(Material.IRON_LEGGINGS), 0.2);
        pool.addLoot(new ItemStack(Material.IRON_HELMET), 0.2);
        pool.addLoot(new ItemStack(Material.IRON_BOOTS), 0.2);
        pool.addLoot(new ItemStack(Material.IRON_ORE, 4), 0.2);
        pool.addLoot(new ItemStack(Material.FISHING_ROD), 0.3);
        pool.addLoot(new ItemStack(Material.SNOWBALL, 16), 0.5);
        pool.addLoot(new ItemStack(Material.COOKED_PORKCHOP, 4), 0.5);
        pool.addLoot(new ItemStack(Material.FLINT_AND_STEEL), 0.3);
        pool.addLoot(new ItemStack(Material.CROSSBOW), 0.2);
        return pool;
    }

    private static LootPool createGoldPool() {
        LootPool pool = new LootPool();
        pool.addLoot(new ItemStack(Material.GOLDEN_SWORD), 0.4);
        pool.addLoot(new ItemStack(Material.GOLDEN_APPLE), 0.3);
        return pool;
    }

    private static LootPool createDiamondPool() {
        LootPool pool = new LootPool();
        pool.addLoot(new ItemStack(Material.DIAMOND_SWORD), 0.3);
        pool.addLoot(new ItemStack(Material.DIAMOND_CHESTPLATE), 0.2);
        return pool;
    }

    private static LootPool createEnchantedPool() {
        LootPool pool = new LootPool();
        pool.addLoot(LootPool.enchant(new ItemStack(Material.DIAMOND_SWORD)), 0.5);
        pool.addLoot(LootPool.enchant(new ItemStack(Material.DIAMOND_BOOTS)), 0.4);
        return pool;
    }

    private static LootPool createNetheritePool() {
        LootPool pool = new LootPool();
        pool.addLoot(new ItemStack(Material.NETHERITE_SWORD), 0.3);
        pool.addLoot(new ItemStack(Material.NETHERITE_HELMET), 0.3);
        return pool;
    }

    private static LootPool createGodPool() {
        LootPool pool = new LootPool();
        pool.addLoot(LootPool.enchant(new ItemStack(Material.NETHERITE_CHESTPLATE)), 0.5);
        pool.addLoot(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 0.2);
        return pool;
    }
}

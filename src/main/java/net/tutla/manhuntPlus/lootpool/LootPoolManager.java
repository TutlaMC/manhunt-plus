package net.tutla.manhuntPlus.lootpool;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class LootPoolManager {
    private static LootPoolMapping mapping = new LootPoolMapping();
    private static UninitalisedLootPool uninitalisedLootPool;

    private static LootPool basicLootPool;

    public static void init(){
        basicLootPool = LootPool.createDefault();
    }


    // unintialised
    public static void setUninitialisedLootPool(UninitalisedLootPool uninitalisedLootPool){
        LootPoolManager.uninitalisedLootPool = uninitalisedLootPool;
    }

    public static void cancelUninitalisedLootPool(){
        uninitalisedLootPool = null;
    }

    public static UninitalisedLootPool getUninitialisedLootPool(){
        return uninitalisedLootPool;
    }

    // intialised ones
    public static List<String> getAllNames(){
        return mapping.getAllNames();
    }

    public static void addLootPool(LootPoolLevelling levelling){
        mapping.put(levelling.getId(), levelling);
    }

    public static void removeLootPool(String id){
        mapping.removeLevelling(id);
    }

    public static LootPoolLevelling getLevelling(String id){
        return mapping.get(id);
    }

    public static LootPoolMapping getMapping(){
        return mapping;
    }

    public static LootPool getDefaultLoot() {
        return basicLootPool;
    }

    public static LootPoolLevelling addPlayerLevellingLootPool(Player player) {
        LootPoolLevelling levelling = new LootPoolLevelling("default", LevellingFactory.createAllTiers(), 1.25);
        playerLootPoolLevels.put(player.getUniqueId(), levelling);
        return levelling;
    }
    public static void giveLootToLeveller(Player player) {
        LootPoolLevelling pool = playerLootPoolLevels.get(player.getUniqueId());
        if (pool == null) {
            pool = addPlayerLevellingLootPool(player);
        }
        ItemStack loot = pool.getLoot();
        player.give(loot);
    }
}

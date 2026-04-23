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

    public static void addLootPool(UninitalisedLootPool levelling){
        mapping.put(levelling.getId(), levelling);
    }

    public static UninitalisedLootPool getLevelling(String id){
        return mapping.get(id);
    }

    public static LootPoolMapping getMapping(){
        return mapping;
    }

    public static LootPool getDefaultLoot() {
        return basicLootPool;
    }

}

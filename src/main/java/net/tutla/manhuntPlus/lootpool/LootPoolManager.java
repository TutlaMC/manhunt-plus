package net.tutla.manhuntPlus.lootpool;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class LootPoolManager {
    public static LootPoolMapping playerLootPoolLevels = new LootPoolMapping();

    private static LootPool basicLootPool;

    public static void init(){
        basicLootPool = LootPool.createDefault();
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

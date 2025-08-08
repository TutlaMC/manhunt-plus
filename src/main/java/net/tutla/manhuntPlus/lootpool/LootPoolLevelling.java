package net.tutla.manhuntPlus.lootpool;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LootPoolLevelling {
    private final List<LootTier> tiers;
    private int level = 0;
    private int usageCount = 0;
    private final double difficultyMultiplier;

    public LootPoolLevelling(List<LootTier> tiers, double difficultyMultiplier) {
        this.tiers = tiers;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public ItemStack getLoot() {
        usageCount++;
        int threshold = (int) Math.ceil((level + 1) * difficultyMultiplier * 5);

        if (usageCount >= threshold && level < tiers.size() - 1) {
            level++;
            usageCount = 0;
        }

        return tiers.get(level).getPool().getRandomLoot();
    }

    public int getLevel() {
        return level;
    }

    public String getTierName() {
        return tiers.get(level).getName();
    }

    public void setLevel(int newLevel) {
        if (newLevel >= 0 && newLevel < tiers.size()) {
            this.level = newLevel;
        }
    }

    public void reset() {
        level = 0;
        usageCount = 0;
    }
}

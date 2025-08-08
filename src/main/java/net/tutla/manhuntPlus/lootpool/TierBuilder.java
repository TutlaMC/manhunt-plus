package net.tutla.manhuntPlus.lootpool;

import java.util.ArrayList;
import java.util.List;

public class TierBuilder {
    private final List<LootTier> tiers = new ArrayList<>();

    public TierBuilder addTier(String name, LootPool pool) {
        tiers.add(new LootTier(name, pool));
        return this;
    }

    public TierBuilder removeTier(String name) {
        tiers.removeIf(t -> t.getName().equalsIgnoreCase(name));
        return this;
    }

    public TierBuilder replaceTier(String name, LootPool newPool) {
        for (int i = 0; i < tiers.size(); i++) {
            if (tiers.get(i).getName().equalsIgnoreCase(name)) {
                tiers.set(i, new LootTier(name, newPool));
                break;
            }
        }
        return this;
    }

    public LootPoolLevelling build(double difficultyMultiplier) {
        return new LootPoolLevelling(tiers, difficultyMultiplier);
    }
}

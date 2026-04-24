package net.tutla.manhuntPlus.lootpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UninitalisedLootPool {
    public final String id;
    public String getId(){
        return id;
    }


    private UninitalisedTier uninitalisedTier;

    private double difficultyMultiplier = 1.5;

    private final TierBuilder tierBuilder = new TierBuilder();

    public LootPoolLevelling generate(){
        return tierBuilder.build(id, difficultyMultiplier);
    }

    public UninitalisedLootPool(String id){
        this.id = id;
    }

    public TierBuilder getTierBuilder(){
        return tierBuilder;
    }

    public void setUninitalisedTier(UninitalisedTier tier){
       uninitalisedTier = tier;
    }

    public UninitalisedTier getUninitialisedTier(){
        return uninitalisedTier;
    }

    public List<LootTier> getTiers(){
        return tierBuilder.getTiers();
    }

    public List<String> getAllTierNames(){
        return tierBuilder.getTiers().stream().map(LootTier::getName).toList();
    }

    public void removeTierByName(String name){
        tierBuilder.getTiers().removeIf(t -> t.getName().equals(name));
    }

    public double getDifficultyMultiplier(){
        return difficultyMultiplier;
    }

    public void setDifficultyMultiplier(double multiplier){
        difficultyMultiplier = multiplier;
    }
}

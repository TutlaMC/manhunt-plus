package net.tutla.manhuntPlus.lootpool;

public class LootTier {
    private final String name;
    private final LootPool pool;

    public LootTier(String name, LootPool pool) {
        this.name = name;
        this.pool = pool;
    }

    public String getName() {
        return name;
    }

    public LootPool getPool() {
        return pool;
    }
}

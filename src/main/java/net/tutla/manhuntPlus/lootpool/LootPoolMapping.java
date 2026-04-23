package net.tutla.manhuntPlus.lootpool;

import java.util.LinkedHashMap;
import java.util.UUID;

public class LootPoolMapping {
    private final LinkedHashMap<UUID, LootPoolLevelling> mapping = new LinkedHashMap<>();
    public LootPoolLevelling get(UUID e){
        return mapping.get(e);
    }
    public void put(UUID id, LootPoolLevelling levelling){
        mapping.put(id, levelling);
    }
}

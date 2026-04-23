package net.tutla.manhuntPlus.lootpool;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class LootPoolMapping {
    private final LinkedHashMap<String, LootPoolLevelling> mapping = new LinkedHashMap<>();
    public LootPoolLevelling get(String e){
        return mapping.get(e);
    }
    public void put(String id, LootPoolLevelling levelling){
        mapping.put(id, levelling);
    }

    public boolean levellingExists(String id){
        LootPoolLevelling e = mapping.get(id);
        return e != null;
    }

    public void removeLevelling(String id){
        if (levellingExists(id)){
            mapping.remove(id);
        }
    }

    public List<String> getAllNames(){
        return mapping.sequencedKeySet().stream().toList();
    }
}

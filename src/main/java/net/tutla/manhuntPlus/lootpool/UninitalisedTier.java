package net.tutla.manhuntPlus.lootpool;

import java.util.ArrayList;
import java.util.List;

public class UninitalisedTier {
    public final String id;
    private final LootPool lootpool = new LootPool();


    public UninitalisedTier(String id){
        this.id = id;
    }

    public LootPool getLootpool(){
        return lootpool;
    }

    public String getId() {
        return id;
    }
}

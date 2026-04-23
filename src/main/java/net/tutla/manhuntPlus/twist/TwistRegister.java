package net.tutla.manhuntPlus.twist;

import net.tutla.manhuntPlus.twist.def.PigOpLoot;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.function.Consumer;

public class TwistRegister{
    // Source of truth - identifier -> twist
    private static final Map<String, Twist> twists = new LinkedHashMap<>();
    private static final Map<TwistAction, Consumer<TwistContext>> twistActionConsumerMapping = new HashMap<>();

    // Trigger indexes
    private static final Map<BlockType, List<Twist>> blockTriggers = new HashMap<>();
    private static final Map<EntityType, List<Twist>> entityTriggers = new HashMap<>();
    private static final List<Twist> noTriggerTwists = new ArrayList<>();


    public static void init(){
        register(new PigOpLoot());
    }

    public static void runAllTwists(TwistTrigger trigger, TwistContext ctx){
        List<Twist> twists1 = getTwistsForTrigger(trigger);
        twists1.forEach((twist -> {twist.executeTwist(ctx);}));
    }


    public static List<Twist> getTwistsForTrigger(TwistTrigger trigger){
        return twists.values().stream()
                .filter(twist -> twist.isActive() && twist.trigger.equals(trigger))
                .toList();
    }

    public static Consumer<TwistContext> getTwistActionConsumer(TwistAction act){
        return twistActionConsumerMapping.get(act);
    }

    public static void register(Twist twist) {
        if (twists.containsKey(twist.identifier)) return;
        twists.put(twist.identifier, twist);
        index(twist);
    }

    public static void unregister(String identifier) {
        Twist twist = twists.remove(identifier);
        if (twist == null) return;
        deindex(twist);
    }

    public static boolean isRegistered(String identifier) {
        return twists.containsKey(identifier);
    }

    public static Collection<Twist> getAll() {
        return Collections.unmodifiableCollection(twists.values());
    }

    public static void clearAll() {
        twists.clear();
        blockTriggers.clear();
        entityTriggers.clear();
        noTriggerTwists.clear();
    }

    // --- Trigger lookups ---

    public static List<Twist> getBlockTwists(BlockType block) {
        return blockTriggers.getOrDefault(block, Collections.emptyList());
    }

    public static List<Twist> getEntityTwists(EntityType entity) {
        return entityTriggers.getOrDefault(entity, Collections.emptyList());
    }

    public static List<Twist> getPassiveTwists() {
        return Collections.unmodifiableList(noTriggerTwists);
    }

    // --- Internal ---

    private static void index(Twist twist) {
        switch (twist.trigger) {
            case BLOCK_BREAK -> blockTriggers
                    .computeIfAbsent(twist.triggerBlock, k -> new ArrayList<>())
                    .add(twist);
            case ENTITY_KILL -> entityTriggers
                    .computeIfAbsent(twist.triggerEntity, k -> new ArrayList<>())
                    .add(twist);
            case NONE -> noTriggerTwists.add(twist);
        }
    }

    private static void deindex(Twist twist) {
        switch (twist.trigger) {
            case BLOCK_BREAK -> {
                List<Twist> list = blockTriggers.get(twist.triggerBlock);
                if (list != null) list.remove(twist);
            }
            case ENTITY_KILL -> {
                List<Twist> list = entityTriggers.get(twist.triggerEntity);
                if (list != null) list.remove(twist);
            }
            case NONE -> noTriggerTwists.remove(twist);
        }
    }
}

package net.tutla.manhuntPlus.twist;

import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.twist.def.DefaultTwist;
import net.tutla.manhuntPlus.twist.def.MilkHunter;
import net.tutla.manhuntPlus.twist.def.PigOpLoot;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
        register(new DefaultTwist());
        register(new PigOpLoot());
        register(new MilkHunter());

        getAll();

        twistActionConsumerMapping.put(TwistAction.TORTURE_HUNTER, (ctx) -> {
            Player target;
            if (ctx.target == null){ // pick random
                Random random = new Random();
                UUID targ = ManhuntContext.getHunters().get(random.nextInt(ManhuntContext.getHunters().size()));
                target = Bukkit.getPlayer(targ);
            } else {
                target = ctx.target;
            }

            new TwistsHelper().tortureHunter(target);
        });
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

    public static boolean twistExists(String identifier){
        Twist twist = getTwist(identifier);
        return twist != null;
    }

    public static void register(Twist twist) {
        if (twists.containsKey(twist.identifier)) return;
        twists.put(twist.identifier, twist);
        index(twist);
    }

    public static void unregister(String identifier) {
        Twist twist = twists.remove(identifier);
        if (twist == null) return;
        if(twist.defaultTwist) return;
        deindex(twist);
    }


    public static Twist getTwist(String identifier){
        return twists.get(identifier);
    }

    public static List<String> getAllTwistNames(){
        return getAll().stream()
                .map(Twist::getIdentifier)
                .toList();
    }

    public static List<String> searchTwists(String arg){
        return getAll().stream()
                .map(Twist::getIdentifier)
                .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                .toList();
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

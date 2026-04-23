package net.tutla.manhuntPlus.twist;

import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.twist.def.DefaultTwist;
import net.tutla.manhuntPlus.twist.def.MilkHunter;
import net.tutla.manhuntPlus.twist.def.PigOpLoot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class TwistRegister{
    // Source of truth - identifier -> twist
    private static final Map<String, Twist> twists = new LinkedHashMap<>();
    private static final Map<TwistAction, Consumer<TwistContext>> twistActionConsumerMapping = new HashMap<>();


    public static void init(){
        register(new DefaultTwist());
        register(new PigOpLoot());
        register(new MilkHunter());

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

        twistActionConsumerMapping.put(TwistAction.GIVE_LOOT, (ctx) -> {
            Player cause;
            if (ctx.cause == null) return;
            cause = ctx.cause;

            List<ItemStack> items = LootPoolManager.getDefaultLoot().getSomeLoot(64);// TODO: make count within lootpool
            cause.give(items);
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
    }

    public static void unregister(String identifier) {
        Twist twist = twists.remove(identifier);
        if (twist == null) return;
        if(twist.defaultTwist) return;
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


}

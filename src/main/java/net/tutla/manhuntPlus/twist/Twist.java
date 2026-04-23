package net.tutla.manhuntPlus.twist;

import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Twist {
    public String identifier;
    public String label;
    public String description;
    public boolean isActive;


    public boolean configurable = true;
    public boolean defaultTwist = false;

    public TwistTrigger trigger = TwistTrigger.NONE;
    public Material triggerBlock;
    public EntityType triggerEntity;

    public TwistAction responseAction;
    public TwistActionResponseTo responseTo;

    public String lootpool = "default";
    private HashMap<UUID, LootPoolLevelling> playerLootPoolLevellerMapping = new HashMap<>();

    public TwistAppliesTo settings = TwistAppliesTo.BOTH;

    public Twist(String identifier, String label, String description){
        this.label = label;
        this.description = description;
        this.identifier = identifier;
    }

    // lootpool

    public void setLootpool(String lootpool){
        this.lootpool = lootpool;
    }

    public LootPoolLevelling generateLootPool(){
        return LootPoolManager.getLevelling(lootpool).generate();
    }

    public HashMap<UUID, LootPoolLevelling> getPlayerLootPoolLevellerMapping(){
        return playerLootPoolLevellerMapping;
    }

    public void resetLootPool(){
        this.playerLootPoolLevellerMapping = new HashMap<>();
    }

    public void lootForPlayer(Player player, int count){
        if (lootpool.equals("default")){
            LootPool pool = LootPoolManager.getDefaultLoot();
            player.give(pool.getSomeLoot(count));
        } else {
            player.give(lootPoolLevellingForPlayer(player.getUniqueId()).getLoot());
        }
    }

    public LootPoolLevelling lootPoolLevellingForPlayer(UUID uuid){
        LootPoolLevelling lvlr = playerLootPoolLevellerMapping.get(uuid);
        if (lvlr == null) {
            lvlr = generateLootPool();
            playerLootPoolLevellerMapping.put(uuid, lvlr);
        }
        return lvlr;
    }

    //not lootpool

    public void setSettings(TwistAppliesTo settings){
        this.settings = settings;
    }

    public void executeTwist(TwistContext ctx){
        // set twist context
        ctx.twist = this;
        if (ctx.twist.identifier.equals("default")) return;

        if (triggerBlock != null && ctx.causingBlock != null){
            if (ctx.causingBlock.getBlockData().getMaterial() == triggerBlock){
                ctx.startResponding();
                doResponse(ctx);
            }
        }
    }

    private void doResponse(TwistContext ctx){
        if (doesContextViolateSettings(ctx)) return;
        // Player doTsTo = findOutWhoTheFuckIsSupposedToRespondTo(ctx);

        TwistRegister.getTwistActionConsumer(responseAction).accept(ctx);
    }

    private boolean doesContextViolateSettings(TwistContext ctx){
        if (settings == TwistAppliesTo.BOTH) return false;

        if (settings.equals(TwistAppliesTo.HUNTER) && !ManhuntContext.isHunter(ctx.cause)) return true;
        return settings.equals(TwistAppliesTo.SPEEDRUNNER) && !ManhuntContext.isSpeedrunner(ctx.cause);
    }

    private Player findOutWhoTheFuckIsSupposedToRespondTo(TwistContext ctx){
        if (responseTo.equals(TwistActionResponseTo.TARGET) && ctx.target != null){
            return ctx.target;
        } else {
            return ctx.cause;
        }
    }

    public boolean isActive(){
        return isActive;
    }

    public void setIsActive(boolean val){
        isActive = val;
    }

    public String getLabel(){
        return label;
    }

    public String getIdentifier(){
        return identifier;
    }

    public String getDescription(){
        return description;
    }
}

package net.tutla.manhuntPlus.twist;

import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Twist {
    // TODO: fix private/public naming
    public String identifier;
    public String label;
    public String description;
    public boolean isActive;


    public boolean configurable = true;
    public boolean defaultTwist = false;

    public TwistTrigger trigger = TwistTrigger.NONE;
    public BlockType triggerBlock;
    public EntityType triggerEntity;
    public boolean triggerTargetIsHunter;
    public boolean isTriggerTargetIsSpeedrunner;

    public TwistAction responseAction;
    public TwistActionResponseTo responseTo;

    public LootPoolLevelling lootpool; // TODO: work on lootpool

    public TwistSettings settings;

    public Twist(String identifier, String label, String description){
        this.label = label;
        this.description = description;
        this.identifier = identifier;
    }

    public void setSettings(TwistSettings settings){
        this.settings = settings;
    }

    public void executeTwist(TwistContext ctx){
        if (triggerBlock != null && ctx.causingBlock != null){
            if (ctx.causingBlock.getBlockData().getMaterial().asBlockType() == triggerBlock){
                ctx.startResponding();
                doResponse(ctx);
            }
        }
    }

    private void doResponse(TwistContext ctx){
        if (doesContextViolateSettings(ctx)) return;
        // Player doTsTo = findOutWhoTheFuckIsSupposedToRespondTo(ctx);

        // set twist context
        ctx.twist = this;
        TwistRegister.getTwistActionConsumer(responseAction).accept(ctx);
    }

    private boolean doesContextViolateSettings(TwistContext ctx){
        if (settings.appliesToBoth) return false;

        if (settings.appliesOnlyToHunter && !ManhuntContext.isHunter(ctx.cause)) return true;
        return settings.appliesOnlyToRunner && !ManhuntContext.isSpeedrunner(ctx.cause);
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

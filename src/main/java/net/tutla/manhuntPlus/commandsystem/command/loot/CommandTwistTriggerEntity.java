package net.tutla.manhuntPlus.commandsystem.command.loot;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.TwistTrigger;
import net.tutla.manhuntPlus.util.TextUtil;
import org.bukkit.entity.EntityType;

import java.util.List;

public class CommandTwistTriggerEntity extends TutlaCommand  {
    private static final String usage = "/twist triggerentity <entity>";
    public CommandTwistTriggerEntity(){
        super("triggerentity", usage, "Set a twist's trigger entity", CommandSection.TWIST,
                new CommandTabAutoComplete("triggerentity", List.of(
                        new CommandTabAutoComplete(null, null, "<enum>").setEnum(EntityType.class)
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                try {
                    TwistRegister.getTwist(ctx.args[1]).triggerEntity = EntityType.valueOf(ctx.args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    ctx.player.sendMessage(TextUtil.parse("<red>Entity doesn't exist!"));
                    return false;
                }
            } else {
                ctx.player.sendMessage(TextUtil.parse("<red>Twist doesn't exist!"));
                return false;
            }
            ctx.player.sendMessage(TextUtil.parse("<green>Twist:</green> Twist updated!"));
            return true;
        }
        return false;
    }
}

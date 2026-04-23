package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistAppliesTo;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistAppliesTo extends TutlaCommand  {
    private static final String usage = "/twist appliesto <group>";
    public CommandTwistAppliesTo(){
        super("appliesto", usage, "Set who can trigger the twist", CommandSection.TWIST,
                new CommandTabAutoComplete("appliesto", List.of(
                        new CommandTabAutoComplete(null, null, "<enum>").setEnum(TwistAppliesTo.class)
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                try {
                    TwistRegister.getTwist(ctx.args[1]).setSettings(TwistAppliesTo.valueOf(ctx.args[2].toUpperCase()));
                } catch (IllegalArgumentException e) {
                    ctx.player.sendMessage(TextUtil.parse("<red>You cannot apply to that group (that group doesn't exist)!"));
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

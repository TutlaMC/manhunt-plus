package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.TwistTrigger;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistTrigger extends TutlaCommand  {
    private static final String usage = "/twist trigger <twist_name> <trigger>";
    public CommandTwistTrigger(){
        super("trigger", usage, "Set a twist's trigger", CommandSection.TWIST,
                new CommandTabAutoComplete("trigger", List.of(
                        new CommandTabAutoComplete(null, null, "<twisttrigger>")
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                try {
                    TwistRegister.getTwist(ctx.args[1]).trigger = TwistTrigger.valueOf(ctx.args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    ctx.player.sendMessage(TextUtil.parse("<red>Trigger doesn't exist!"));
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

package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistAction;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.Arrays;
import java.util.List;

public class CommandTwistDescription extends TutlaCommand  {
    private static final String usage = "/twist description <twist_name> <description>";
    public CommandTwistDescription(){
        super("description", usage, "Set a twist's output action", CommandSection.TWIST,
                new CommandTabAutoComplete("description", List.of(
                        new CommandTabAutoComplete(null, null, "")
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                TwistRegister.getTwist(ctx.args[1]).description =  String.join(" ", Arrays.copyOfRange(ctx.args, 2, ctx.args.length));
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

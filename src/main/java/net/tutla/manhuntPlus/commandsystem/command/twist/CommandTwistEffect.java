package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistAction;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistEffect extends TutlaCommand  {
    private static final String usage = "/twist effect <twist_name> <effect>";
    public CommandTwistEffect(){
        super("effect", usage, "Set a twist's output action", CommandSection.TWIST,
                new CommandTabAutoComplete("effect", List.of(
                        new CommandTabAutoComplete(null, null, "<twistaction>")
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                try {
                    TwistRegister.getTwist(ctx.args[1]).responseAction = TwistAction.valueOf(ctx.args[2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    ctx.player.sendMessage(TextUtil.parse("<red>Action doesn't exist!"));
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

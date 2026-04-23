package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistAction;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistDelete extends TutlaCommand  {
    private static final String usage = "/twist delete <twist_name>";
    public CommandTwistDelete(){
        super("delete", usage, "Delete a twist", CommandSection.TWIST,
                new CommandTabAutoComplete("delete", List.of(), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 2) {
            if (TwistRegister.twistExists(ctx.args[1])){
                TwistRegister.unregister(ctx.args[1]);
            } else {
                ctx.player.sendMessage(TextUtil.parse("<red>Twist doesn't exist!"));
                return false;
            }
            ctx.player.sendMessage(TextUtil.parse("<green>Twist:</green> Twist removed!"));
            return true;
        }
        return false;
    }
}

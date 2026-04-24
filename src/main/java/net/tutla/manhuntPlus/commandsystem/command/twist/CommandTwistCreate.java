package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.Twist;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import static net.tutla.manhuntPlus.commandsystem.command.twist.CommandTwistHelp.helpString;


public class CommandTwistCreate extends TutlaCommand  {
    private static final String usage = "/twist create <name:one_word>";
    public CommandTwistCreate(){
        super("create", usage, "Create a new twist", CommandSection.TWIST,  null);
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 2) {
            Twist twist = new Twist(ctx.args[1], ctx.args[1], "You haven't set a description yet! Add one using /twist description <twist> <description>");
            TwistRegister.register(twist);
            ctx.player.sendMessage(TextUtil.parse("<green>Twist:</green> Twist "+ctx.args[1]+" created"));
            ctx.player.sendMessage(TextUtil.parse(helpString));
            return true;
        }
        return false;
    }
}

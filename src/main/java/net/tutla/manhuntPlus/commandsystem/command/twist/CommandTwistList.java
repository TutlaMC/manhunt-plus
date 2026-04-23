package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandTwistList extends TutlaCommand  {
    private static final String usage = "/twist list";
    public CommandTwistList(){
        super("list", usage, "List all twists", CommandSection.TWIST, null);
    }

    @Override
    public boolean run(CommandContext ctx){
        StringBuilder builder = new StringBuilder();

        builder.append("<gold><bold>── Twists ──</bold></gold>\n");
        TwistRegister.getAll().forEach((twist)->{
            builder.append("<aqua><bold>");
            builder.append(twist.label);
            builder.append("</aqua> - ");
            builder.append("<gray>");
            builder.append(twist.description);
            builder.append("</gray>\n");
        });
        ctx.player.sendMessage(TextUtil.parse(builder.toString()));

        return true;
    }
}

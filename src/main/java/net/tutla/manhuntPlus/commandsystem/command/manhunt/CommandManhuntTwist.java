package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.Twist;

import java.util.Arrays;

public class CommandManhuntTwist extends TutlaCommand {
    public CommandManhuntTwist() {
        super("twist", "/manhunt twist <twist>","Apply a twist (default: DEFAULT)", CommandSection.SETTINGS, new CommandTabAutoComplete("twist", null, "<values>")
                .setValues(Arrays.stream(Twist.values())
                        .map(Enum::name)
                        .map(String::toLowerCase)
                        .toList()
                )
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 2) {
            try {
                Twist selected = Twist.valueOf(ctx.args[1].toUpperCase());
                Manhunt.setTwist(selected);
                ctx.player.sendMessage("§aTwist set to: " + selected.name());
            } catch (IllegalArgumentException e) {
                ctx.player.sendMessage("§cUnknown twist: " + ctx.args[1]);
                ctx.player.sendMessage("§eAvailable twists: " + Arrays.toString(Twist.values()));
            }
        } else {
            ctx.player.sendMessage("§eCurrent Twist is "+Manhunt.getTwist());
            ctx.player.sendMessage("§eUsage: /manhunt twist <twistName>");
        }
        return true;
    }
}

package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.kyori.adventure.text.Component;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.DefaultTwist;
import net.tutla.manhuntPlus.twist.Twist;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.Arrays;
public class CommandManhuntTwist extends TutlaCommand {
    public CommandManhuntTwist() {
        super("twist", "/manhunt twist <twist>", "Apply a twist to the manhunt", CommandSection.SETTINGS,
                new CommandTabAutoComplete("twist", null, "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (ctx.args.length < 2) {
            showCurrentTwist(ctx);
            return true;
        }

        if (TwistRegister.twistExists(ctx.args[1])) {
            Twist selected = TwistRegister.getTwist(ctx.args[1]);
            Manhunt.setTwist(selected);
            ctx.player.sendMessage(TextUtil.parse(
                    "<green>Twist set to: <bold>" + selected.label + "</bold></green> - " +
                            "<gray>" + selected.description + "</gray>"
            ));
        } else {
            ctx.player.sendMessage(TextUtil.parse(
                    "<red>Unknown twist: <bold>" + ctx.args[1] + "</bold></red>"
            ));
        }

        return true;
    }

    private void showCurrentTwist(CommandContext ctx) {
        Twist current = Manhunt.getTwist();
        ctx.player.sendMessage(TextUtil.parse(
                "<yellow>Current twist: <bold>" + current.label + "</bold></yellow>\n" +
                        "<gray>" + current.description + "</gray>\n" +
                        "<gray>Use <white>/manhunt twist <twist></white> to change it.</gray>"
        ));
    }

}

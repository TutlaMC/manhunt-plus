package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.kyori.adventure.text.Component;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.Twist;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.Arrays;
public class CommandManhuntTwist extends TutlaCommand {
    public CommandManhuntTwist() {
        super("twist", "/manhunt twist <twist>", "Apply a twist to the manhunt", CommandSection.SETTINGS,
                new CommandTabAutoComplete("twist", null, "<values>")
                        .setValues(Arrays.stream(Twist.values())
                                .map(t -> t.name().toLowerCase())
                                .toList()
                        )
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (ctx.args.length < 2) {
            showCurrentTwist(ctx);
            return true;
        }

        try {
            Twist selected = Twist.valueOf(ctx.args[1].toUpperCase());
            Manhunt.setTwist(selected);
            ctx.player.sendMessage(TextUtil.parse(
                    "<green>Twist set to: <bold>" + selected.label + "</bold></green>\n" +
                            "<gray>" + selected.description + "</gray>"
            ));
        } catch (IllegalArgumentException e) {
            ctx.player.sendMessage(TextUtil.parse(
                    "<red>Unknown twist: <bold>" + ctx.args[1] + "</bold></red>"
            ));
            showTwistList(ctx);
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
        showTwistList(ctx);
    }

    private void showTwistList(CommandContext ctx) {
        Component list = TextUtil.parse("<yellow><bold>── Available Twists ──</bold></yellow>\n");
        for (Twist twist : Twist.values()) {
            list = list.append(Component.empty()
                    .append(TextUtil.parse(
                            "<green><click:run_command:'/manhunt twist " + twist.name().toLowerCase() + "'>" +
                                    "<hover:show_text:'" + twist.description + "'>" +
                                    twist.name().toLowerCase() +
                                    "</hover></click></green> <gray>– " + twist.label + "</gray>\n"
                    ))
            );
        }
        ctx.player.sendMessage(list);
    }
}

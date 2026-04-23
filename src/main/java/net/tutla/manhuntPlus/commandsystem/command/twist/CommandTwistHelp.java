package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.DefaultTwist;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistHelp extends TutlaCommand {
    public static Component helpString;

    public CommandTwistHelp() {
        super("help", "/twist help", "Show the help menu", CommandSection.TWIST, null);
    }

    @Override
    public boolean run(CommandContext ctx) {
        ctx.player.sendMessage(helpString);
        return true;
    }

    public static void generateHelpString(List<TutlaCommand> commands) {
        Component body = Component.empty();

        for (CommandSection section : CommandSection.values()) {
            if (section == CommandSection.NONE) continue;

            List<TutlaCommand> sectionCmds = commands.stream()
                    .filter(cmd -> cmd.getSection() == section)
                    .filter(cmd -> cmd.getHelpString() != null && cmd.getDescription() != null)
                    .toList();

            if (sectionCmds.isEmpty()) continue;

            body = body.append(TextUtil.parse("\n<yellow><bold>── " + section.name().charAt(0) + section.name().substring(1).toLowerCase() + " ──</bold></yellow>\n"));

            for (TutlaCommand cmd : sectionCmds) {
                body = body.append(generateHelpStringForCommand(cmd));
            }
        }

        Component header = TextUtil.parse(
                "<gold><bold>🧭 Creating twists</bold></gold>\n" +
                        "<gray>A powerful manhunt plugin with compass tracking, twists & full hunt control.</gray>"
        );

        Component twists = TextUtil.parse("""

<yellow><bold>── Twists ──</bold></yellow>

""");
        for (DefaultTwist twist : DefaultTwist.values()){
            twists = twists.append(TextUtil.parse("<aqua>"+twist.label+"</aqua> <gray>– "+twist.description+"</gray>\n"));
        }
        Component footer = TextUtil.parse("""

<yellow><bold>── Links ──</bold></yellow>
<click:open_url:'https://modrinth.com/plugin/manhunt+'><aqua><bold>📦 Modrinth</bold></aqua></click> <gray>|</gray> \
<click:open_url:'https://discord.tutla.net'><aqua><bold>💬 Discord</bold></aqua></click> <gray>|</gray> \
<click:open_url:'https://github.com/TutlaMC/manhunt-plus'><aqua><bold>⭐ GitHub</bold></aqua></click> <gray>|</gray> \
<click:open_url:'https://wiki.tutla.net/manhunt+'><aqua><bold>🌐 Wiki</bold></aqua></click>
""");

        helpString = header.append(body).append(twists).append(footer);
    }

    private static Component generateHelpStringForCommand(TutlaCommand cmd) {
        Component description = TextUtil.parse(cmd.getDescription());
        Component helpText = TextUtil.parse(cmd.getHelpString());

        return Component.empty()
                .append(
                        helpText
                                .color(NamedTextColor.GREEN)
                                .clickEvent(ClickEvent.suggestCommand(cmd.getHelpString()))
                                .hoverEvent(HoverEvent.showText(description))
                )
                .append(TextUtil.parse(" <gray>– </gray>"))
                .append(description)
                .append(Component.newline());
    }
}

package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandManhuntDonate extends TutlaCommand {

    public CommandManhuntDonate() {
        super("donate", "/manhunt donate", "Contribute to the project!", CommandSection.NONE, null);
    }

    @Override
    public boolean run(CommandContext ctx) {
        ctx.player.sendMessage(TextUtil.parse("<click:open_url:'https://donatr.ee/tutlamc?utm_source=copy&utm_medium=share'><aqua><bold>\uD83D\uDC99 Donate</bold></aqua></click>"));
        return true;
    }
}

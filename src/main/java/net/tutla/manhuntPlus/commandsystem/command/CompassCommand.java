package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntCompass;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.util.TextUtil;
import net.tutla.manhuntPlus.util.TutlaUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CompassCommand extends TutlaCommand {
    public CompassCommand() {
        super("compass", "/compass <speedrunner>","Track a speedrunner", CommandSection.CONTROLS,
                new CommandTabAutoComplete("compass", null, "<speedrunner>"));
    }

    public boolean run(CommandContext ctx) {
        if (ManhuntContext.getSpeedrunners().isEmpty()) {
            ctx.player.sendMessage(TextUtil.parse("<bold><red>There are no speedrunners!</red> Add them with /manhunt speedrunner add <player></bold>"));
            return true;
        }

        Player target;
        if (ctx.args.length >= 1){
            target = Bukkit.getPlayer(ctx.args[0]);
        } else {
            target = Bukkit.getPlayer(ManhuntContext.getSpeedrunners().getFirst());
        }

        if (TutlaUtil.isTargetValid(target)){
            ManhuntCompass.giveCompass(target, ctx.player);
        } else {
            ctx.player.sendMessage(TextUtil.parse("<red>Player not found</red>"));
        }
        return true;
    }
}

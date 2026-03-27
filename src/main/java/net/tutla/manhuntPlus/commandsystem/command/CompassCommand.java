package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntCompass;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CompassCommand extends TutlaCommand {
    public CompassCommand() {
        super("compass", "/compass <speedrunner>\n - <speedrunner> must be a speedrunner (add one via /manhunt speedrunner add)",
                new CommandTabAutoComplete("compass", null, "<speedrunner>"));
    }

    public boolean run(CommandContext ctx) {
        if (ManhuntContext.getSpeedrunners().isEmpty()) {
            ctx.player.sendMessage("§cThere are no speedrunners! Add them with /manhunt speedrunner add <player>");
            return true;
        }

        Player target;
        if (ctx.args.length >= 1){
            target = Bukkit.getPlayer(ctx.args[0]);
        } else {
            target = Bukkit.getPlayer(ManhuntContext.getSpeedrunners().getFirst());
        }

        if (target != null){
            ManhuntCompass.giveCompass(target, ctx.player);
        } else {
            ctx.player.sendMessage("§cPlayer not found");
        }
        return true;
    }
}

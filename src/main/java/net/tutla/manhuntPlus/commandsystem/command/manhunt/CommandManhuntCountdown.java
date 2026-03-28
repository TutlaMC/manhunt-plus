package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntTimer;

import java.util.List;

public class CommandManhuntCountdown extends TutlaCommand  {
    private static String usage = "/manhunt cooldown <minutes>";
    public CommandManhuntCountdown(){
        super("cooldown", usage, "Set time limit (0 to disable)", CommandSection.SETTINGS,
                new CommandTabAutoComplete("countdown", null, "<values>")
                        .setValues(List.of("0", "5", "10", "15", "30"))
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 2) {
            try {
                int mins = Integer.parseInt(ctx.args[1]);
                ManhuntTimer.setCountdownLimitMinutes(mins);
                if (mins > 0) {
                    ctx.player.sendMessage("§aCountdown set to " + mins + " minute(s).");
                } else {
                    ctx.player.sendMessage("§eCountdown disabled.");
                }
            } catch (NumberFormatException e) {
                ctx.player.sendMessage("§cInvalid number.");
            }
        } else {
            if (ManhuntTimer.getCountdownLimitMinutes() > 0)
                ctx.player.sendMessage("§eCurrent countdown limit: " + ManhuntTimer.getCountdownLimitMinutes() + " minute(s).");
            else {
                ctx.player.sendMessage("§eCountdown is disabled.");
            }
            ctx.player.sendMessage("§eUse "+usage+", set minutes to 0 to disable");
        }
        return true;
    }
}

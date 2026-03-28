package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;

public class CommandManhuntStop extends TutlaCommand {
    public CommandManhuntStop(){
        super("stop", "/manhunt stop", "Stop the manhunt", CommandSection.CONTROLS, null);
    }

    @Override
    public boolean run(CommandContext ctx){
        if (!Manhunt.stopManhunt()){
            ctx.player.sendMessage("§cManhunt is not running.");
        }
        return true;
    }
}

package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;

public class CommandManhuntStart extends TutlaCommand {
    public CommandManhuntStart(){
        super("start", "/manhunt start", "Start the manhunt", CommandSection.CONTROLS, null);
    }

    @Override
    public boolean run(CommandContext ctx){
        if (!Manhunt.startManhunt()){
            ctx.player.sendMessage("§cManhunt already running.");
        }
        return true;
    }
}

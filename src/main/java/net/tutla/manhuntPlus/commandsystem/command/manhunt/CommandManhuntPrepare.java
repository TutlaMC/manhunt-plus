package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import org.bukkit.Bukkit;

public class CommandManhuntPrepare extends TutlaCommand {
    public CommandManhuntPrepare(){
        super("prepare", "/manhunt prepare", "Start when speedrunner hits a hunter", CommandSection.SETUP, null);
    }

    @Override
    public boolean run(CommandContext ctx){
        Manhunt.setWaitingForStart(true);
        Bukkit.broadcastMessage("Manhunt will now start when speedrunner hits a hunter!");
        return true;
    }
}

package net.tutla.manhuntPlus.commandsystem.command;

import net.kyori.adventure.text.Component;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.command.manhunt.*;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntTimer;
import net.tutla.manhuntPlus.manhunt.Twist;
import net.tutla.manhuntPlus.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManhunt extends TutlaCommand {
    private static final List<TutlaCommand> subcommands = List.of(
        new CommandManhuntSpeedrunner(),
        new CommandManhuntHunter(),
        new CommandManhuntTwist(),
        new CommandManhuntCountdown(),
        new CommandManhuntList(),
        new CommandManhuntStart(),
        new CommandManhuntStop(),
        new CommandManhuntPrepare(),
        new CommandManhuntHelp()
    );
    private static final List<CommandTabAutoComplete> subcommandsAutoCompletes = subcommands.stream().map(cmd -> cmd.autocomplete).toList();
    private static final List<String> subcommandNames = subcommands.stream().map(TutlaCommand::name).toList();


    public CommandManhunt(){
        super("manhunt", String.valueOf(CommandManhuntHelp.helpString),
                "Run manhunt commands", CommandSection.NONE,
                new CommandTabAutoComplete("manhunt", subcommandsAutoCompletes, "<values>")
                        .setValues(subcommandNames)
        );

        CommandManhuntHelp.generateHelpString(subcommands);
    }

    @Override
    public boolean run(CommandContext ctx) {
        System.out.println(Arrays.toString(ctx.args));
        if (ctx.args.length == 0) {
            return false;
        }
        for (TutlaCommand cmd : subcommands){
            if (ctx.args[0].equalsIgnoreCase(cmd.name())){
                boolean out = cmd.run(ctx);
                if (!out){
                    cmd.help(ctx.player);
                }
                return true;
            }
        }
        return false;
    }


}

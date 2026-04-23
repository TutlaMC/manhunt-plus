package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.command.manhunt.*;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;

import java.util.*;

public class CommandManhunt extends TutlaCommand {
    private static final List<TutlaCommand> subcommands = List.of(
            new CommandManhuntStart(),
            new CommandManhuntStop(),
            new CommandManhuntSpeedrunner(),
            new CommandManhuntHunter(),
            new CommandManhuntList(),
            new CommandManhuntHelp(),
            new CommandManhuntTwist(),
            new CommandManhuntCountdown(),
            new CommandManhuntPrepare()
    );
    private static final List<CommandTabAutoComplete> subcommandsAutoCompletes = subcommands.stream().map(cmd -> cmd.autocomplete).toList();
    private static final List<String> subcommandNames = subcommands.stream().map(TutlaCommand::name).toList();


    public CommandManhunt(){
        super("manhunt", String.valueOf(CommandManhuntHelp.helpString),
                "Run manhunt commands", CommandSection.NONE,
                new CommandTabAutoComplete("manhunt", subcommandsAutoCompletes, "<values>")
                        .setValues(subcommandNames)
        );

    }

    @Override
    public boolean run(CommandContext ctx) {
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

    public List<TutlaCommand> getSubcommands(){
        return subcommands;
    }
}

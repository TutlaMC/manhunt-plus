package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.commandsystem.command.twist.*;

import java.util.List;

public class CommandTwist extends TutlaCommand {
    private static final List<TutlaCommand> subcommands = List.of(
            new CommandTwistCreate(),
            new CommandTwistHelp(),
            new CommandTwistList(),
            new CommandTwistEffect(),
            new CommandTwistTrigger(),
            new CommandTwistTriggerEntity(),
            new CommandTwistTriggerBlock(),
            new CommandTwistAppliesTo(),
            new CommandLootPool()
    );
    private static final List<CommandTabAutoComplete> subcommandsAutoCompletes = subcommands.stream().map(cmd -> cmd.autocomplete).toList();
    private static final List<String> subcommandNames = subcommands.stream().map(TutlaCommand::name).toList();


    public CommandTwist(){
        super("twist", String.valueOf(CommandTwistHelp.helpString),
                "Run twist commands", CommandSection.NONE,
                new CommandTabAutoComplete("twist", subcommandsAutoCompletes, "<values>")
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

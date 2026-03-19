package net.tutla.manhuntPlus.commandsystem;

import net.tutla.manhuntPlus.commandsystem.command.CommandManhunt;
import net.tutla.manhuntPlus.commandsystem.command.CompassCommand;
import net.tutla.manhuntPlus.commandsystem.command.SurroundCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CommandSystem {
    private final List<TutlaCommand> commands = List.of(
            new CompassCommand(),
            new CommandManhunt(),
            new SurroundCommand()
    );

    public boolean execute(CommandContext cmdParams){
        for (TutlaCommand command : commands){
            if (cmdParams.cmd.getName().equalsIgnoreCase(command.name())){
                if (!command.run(cmdParams)){
                    command.help(cmdParams.player);
                };
            }
        }
        return false;
    }

    public List<String> tabComplete(CommandContext ctx){
        if (ctx.args.length > 0) {
            for (TutlaCommand command : commands) {
                if (command.name().equalsIgnoreCase(ctx.cmd.getName())) {
                    int cidx = 0;
                    CommandTabAutoComplete complete = getAutoComplete(command.autocomplete, ctx, cidx);
                    if (complete != null) {
                        return generateAutoComplete(complete, ctx.args[ctx.args.length - 1]);
                    }
                    return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }

    public CommandTabAutoComplete getAutoComplete(CommandTabAutoComplete complete, CommandContext ctx, int cidx) {
        if (cidx == ctx.args.length - 1) {
            return complete;
        }

        if (complete.childAutoCompletes != null) {
            for (CommandTabAutoComplete child : complete.childAutoCompletes) {
                if (child.name.equalsIgnoreCase(ctx.args[cidx])) {
                    return getAutoComplete(child, ctx, cidx + 1);
                }
            }
        }

        return null;
    }

    public List<String> generateAutoComplete(CommandTabAutoComplete autocomplete, String arg){
        if (autocomplete.value == null || autocomplete.value.isBlank()){
            return Collections.emptyList();
        }

        switch (autocomplete.value) {
            case "<values>" -> {
                return autocomplete.values.stream()
                        .filter(s -> s.startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<player>" -> {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<speedrunner>" -> {
                return ManhuntContext.getSpeedrunners().stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<hunter>" -> {
                return ManhuntContext.getHunters().stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<playingspeedrunner>" -> {
                return ManhuntContext.getPlayingSpeedrunners().stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            default -> {return Collections.emptyList();}
        }
    }
}

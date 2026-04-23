package net.tutla.manhuntPlus.commandsystem;

import net.tutla.manhuntPlus.commandsystem.command.*;
import net.tutla.manhuntPlus.commandsystem.command.manhunt.CommandManhuntHelp;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.twist.Twist;
import net.tutla.manhuntPlus.twist.TwistAction;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.TwistTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

public class CommandSystem {
    private final CommandManhunt manhuntCommand = new CommandManhunt();
    private final List<TutlaCommand> commands = List.of(
            new CompassCommand(),
            manhuntCommand,
            new CommandTwist(),
            new SurroundCommand(),
            new CommandLootPool()
    );

    public void initialise() {
        List<TutlaCommand> toPut = new ArrayList<>(manhuntCommand.getSubcommands());
        toPut.addAll(commands);
        CommandManhuntHelp.generateHelpString(toPut);
    }

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
                if (child == null) continue;
                if (child.name == null || child.name.equalsIgnoreCase(ctx.args[cidx])) {
                    return getAutoComplete(child, ctx, cidx + 1);
                }
            }
        }

        if (complete.value != null && !complete.value.equals("<values>")) {
            return getAutoComplete(complete, ctx, cidx + 1);
        }

        return null;
    }

    public List<String> generateAutoComplete(CommandTabAutoComplete autocomplete, String arg){
        if (autocomplete.value == null || autocomplete.value.isBlank()){
            return Collections.emptyList();
        }

        switch (autocomplete.value) {
            case "<values>" -> {
                if (autocomplete.values == null) return Collections.emptyList();
                return autocomplete.values.stream()
                        .filter(s -> s.startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<enum>" -> {
                if (autocomplete.enum_ == null) return Collections.emptyList();
                return Arrays.stream(autocomplete.enum_.getEnumConstants())
                        .map(Enum::name)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
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
            case "<twist>" -> {
                return TwistRegister.searchTwists(arg);
            }
            case "<twistaction>" -> {
                return Arrays.stream(TwistAction.values())
                        .map(TwistAction::name)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<twisttrigger>" -> {
                return Arrays.stream(TwistTrigger.values())
                        .map(TwistTrigger::name)
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<lootpool>" -> {
                return LootPoolManager.getAllNames().stream()
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            case "<tier>" -> {
                return LootPoolManager.getUninitialisedLootPool()
                        .getAllTierNames().stream()
                        .filter(name -> name.toLowerCase().startsWith(arg.toLowerCase()))
                        .toList();
            }
            default -> {return Collections.emptyList();}
        }
    }
}

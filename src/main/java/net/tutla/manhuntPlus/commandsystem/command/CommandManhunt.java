package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntTimer;
import net.tutla.manhuntPlus.manhunt.Twist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManhunt extends TutlaCommand {
    public CommandManhunt(){
        super("manhunt", "/manhunt <speedrunner|hunter|start|stop|countdown|prepare|list|help>",
                new CommandTabAutoComplete("manhunt", List.of(
                        new CommandTabAutoComplete("speedrunner", List.of(
                                new CommandTabAutoComplete("add", null, "<player>"),
                                new CommandTabAutoComplete("remove", null, "<speedrunner>")
                        ), "<values>")
                                .setValues(List.of("add", "remove")),
                        new CommandTabAutoComplete("hunter", List.of(
                                new CommandTabAutoComplete("add", null, "<player>"),
                                new CommandTabAutoComplete("remove", null, "<hunter>")
                        ), "<values>")
                                .setValues(List.of("add", "remove")),
                        new CommandTabAutoComplete("twist", null, "<values>")
                                .setValues(Arrays.stream(Twist.values())
                                        .map(Enum::name)
                                        .map(String::toLowerCase)
                                        .toList()),
                        new CommandTabAutoComplete("countdown", null, "<values>")
                                .setValues(List.of("0", "5", "10", "15", "30"))
                ), "<values>")
                        .setValues(List.of("speedrunner","hunter", "start", "stop", "twist", "prepare","list","countdown", "help"))
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        System.out.println(Arrays.toString(ctx.args));
        if (ctx.args.length == 0) {
            help(ctx.player);
            return true;
        }
        System.out.println(Arrays.toString(ctx.args));
        switch (ctx.args[0].toLowerCase()) { // first time i actually used a switch case my whole life (if statements worked well so like it wasn't necessary but like worth trying)
            case "twist" -> {
                if (ctx.args.length == 1) {
                    try {
                        Twist selected = Twist.valueOf(ctx.args[0].toUpperCase());
                        Manhunt.setTwist(selected);
                        ctx.player.sendMessage("§aTwist set to: " + selected.name());
                    } catch (IllegalArgumentException e) {
                        ctx.player.sendMessage("§cUnknown twist: " + ctx.args[0]);
                        ctx.player.sendMessage("§eAvailable twists: " + Arrays.toString(Twist.values()));
                    }
                    return true;
                } else {
                    ctx.player.sendMessage("§eCurrent Twist is "+Manhunt.getTwist());
                    ctx.player.sendMessage("§eUsage: /manhunt twist <twistName>");
                }
            }
            case "hunter" -> {
                if (ctx.args.length == 3){
                    if (ctx.args[1].equalsIgnoreCase("add")){
                        Player target = Bukkit.getPlayer(ctx.args[2]);
                        if (ManhuntContext.getSpeedrunners().contains(target.getUniqueId())) {
                            ctx.player.sendMessage("§cPlayer is a speedrunner!");
                            return true;
                        }
                        if (isTargetValid(target)) {
                            if (!ManhuntContext.getHunters().contains(target.getUniqueId())){
                                ManhuntContext.addHunter(target);
                                Bukkit.broadcastMessage("§a"+target.getName() + " is now a hunter!");
                            } else {
                                ctx.player.sendMessage("§cPlayer is already a hunter!");
                            }

                        } else {
                            ctx.player.sendMessage("§cPlayer not found or not online.");
                        }
                        return true;
                    } else if (ctx.args[0].equalsIgnoreCase("remove")){
                        Player target = Bukkit.getPlayer(ctx.args[1]);
                        if (isTargetValid(target)) {
                            if (ManhuntContext.getHunters().contains(target.getUniqueId())){
                                ManhuntContext.removeHunter(target);
                                Bukkit.broadcastMessage("§a"+target.getName() + " is no longer a hunter!");
                            } else {
                                ctx.player.sendMessage("§cPlayer is not a hunter!");
                            }
                        } else {
                            ctx.player.sendMessage("§cPlayer not found or not online.");
                        }
                        return true;
                    }
                }
                return false;
            }
            case "speedrunner" -> {
                if (ctx.args.length == 3){
                    if (ctx.args[1].equalsIgnoreCase("add")){
                        Player target = Bukkit.getPlayer(ctx.args[2]);
                        if (isTargetValid(target)) {
                            if (ManhuntContext.getHunters().contains(target.getUniqueId())) {
                                ctx.player.sendMessage("§cPlayer is a hunter!");
                                return true;
                            }
                            if (!ManhuntContext.getSpeedrunners().contains(target.getUniqueId())){
                                ManhuntContext.addSpeedrunner(target);
                                Bukkit.broadcastMessage("§a"+target.getName() + " is now a speedrunner!");
                            } else {
                                ctx.player.sendMessage("§cPlayer is already a speedrunner!");
                            }

                        } else {
                            ctx.player.sendMessage("§cPlayer not found or not online.");
                        }
                        return true;
                    } else if (ctx.args[1].equalsIgnoreCase("remove")){
                        Player target = Bukkit.getPlayer(ctx.args[2]);
                        if (isTargetValid(target)) {
                            if (ManhuntContext.getSpeedrunners().contains(target.getUniqueId())){
                                ManhuntContext.removeSpeedrunner(target);
                                Bukkit.broadcastMessage("§a"+target.getName() + " is no longer a speedrunner!");
                            } else {
                                ctx.player.sendMessage("§cPlayer is not a speedrunner!");
                            }
                        } else {
                            ctx.player.sendMessage("§cPlayer not found or not online.");
                        }
                        return true;
                    }
                }
                return false;
            }
            case "start" -> {
                if (!Manhunt.startManhunt()){
                    ctx.player.sendMessage("§cManhunt already running.");
                }
            }
            case "stop" -> {
                if (!Manhunt.stopManhunt()){
                    ctx.player.sendMessage("§cManhunt is not running.");
                }
            }
            case "countdown" -> {
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
                    else
                        ctx.player.sendMessage("§eCountdown is disabled.");
                    ctx.player.sendMessage("§eUse /manhunt countdown <minutes>, set minutes to 0 to disable");
                }
            }
            case "help" -> {
                ctx.player.sendMessage("""
To use our plugin start adding speedrunners with: /manhunt speedrunner add, use /manhunt speedrunner remove to remove a speedrunner
You can then add the hunters using /manhunt hunter add & remove them using /manhunt hunter remove
To start the manhunt run §e/manhunt start
Manhunt countdown (max limit a manhunt can last in minutes) can be set using §e/manhunt cooldown
Read full documentation on Modrinth/Discord/Website""");
            }
            case "prepare" -> {
                Manhunt.setWaitingForStart(true);
                Bukkit.broadcastMessage("Manhunt will now start when speedrunner hits a hunter!");
            }
            case "list" -> {
                ctx.player.sendMessage("§eSpeedrunners:");
                for (UUID id : ManhuntContext.getSpeedrunners()){
                    ctx.player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(id)).getName());
                }
                ctx.player.sendMessage("§eHunters:");
                for (UUID id : ManhuntContext.getHunters()){
                    ctx.player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(id)).getName());
                }
                ctx.player.sendMessage("§ePlaying Speedrunners:");
                for (UUID id : ManhuntContext.getPlayingSpeedrunners()){
                    ctx.player.sendMessage(Objects.requireNonNull(Bukkit.getPlayer(id)).getName());
                }
            }
            default -> ctx.player.sendMessage("§cUnknown subcommand. Use: start, stop, countdown");
        }
        return true;
    }

    private boolean isTargetValid(Player target){
        return target != null && target.isOnline();
    }
}

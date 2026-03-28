package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.util.TutlaUtil;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
public class CommandManhuntSpeedrunner extends TutlaCommand {
    public CommandManhuntSpeedrunner(){
        super("speedrunner", "/manhunt speedrunner add|remove <player>","Add/remove a speedrunner", CommandSection.SETUP,
                new CommandTabAutoComplete("speedrunner", List.of(
                        new CommandTabAutoComplete("add", null, "<player>"),
                        new CommandTabAutoComplete("remove", null, "<speedrunner>")
                ), "<values>")
                        .setValues(List.of("add", "remove"))
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (ctx.args.length == 3){
            if (ctx.args[1].equalsIgnoreCase("add")){
                Player target = Bukkit.getPlayer(ctx.args[2]);
                if (TutlaUtil.isTargetValid(target)) {
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
                if (TutlaUtil.isTargetValid(target)) {
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
}

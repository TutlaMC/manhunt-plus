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

public class CommandManhuntHunter extends TutlaCommand {
    public CommandManhuntHunter(){
        super("hunter", "/manhunt hunter add|remove <player>","Add/remove a hunter", CommandSection.SETUP,
                new CommandTabAutoComplete("hunter", List.of(
                        new CommandTabAutoComplete("add", null, "<player>"),
                        new CommandTabAutoComplete("remove", null, "<hunter>")
                ), "<values>")
                        .setValues(List.of("add", "remove"))
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (ctx.args.length == 3){
            if (ctx.args[1].equalsIgnoreCase("add")){
                Player target = Bukkit.getPlayer(ctx.args[2]);
                if (ManhuntContext.getSpeedrunners().contains(target.getUniqueId())) {
                    ctx.player.sendMessage("§cPlayer is a speedrunner!");
                    return true;
                }
                if (TutlaUtil.isTargetValid(target)) {
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
            } else if (ctx.args[1].equalsIgnoreCase("remove")){
                Player target = Bukkit.getPlayer(ctx.args[1]);
                if (TutlaUtil.isTargetValid(target)) {
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
}

package net.tutla.manhuntPlus.commandsystem.command.manhunt;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.util.TextUtil;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CommandManhuntList extends TutlaCommand {
    public CommandManhuntList(){
        super("list", "/manhunt list","List all hunters & speedrunners", CommandSection.SETUP, null);

    }

    @Override
    public boolean run(CommandContext ctx){
        HashMap<String, List<UUID>> toGet = new HashMap<>();
        toGet.put("Speedrunners", ManhuntContext.getSpeedrunners());
        toGet.put("Hunters", ManhuntContext.getHunters());
        toGet.put("Playing Speedrunners", ManhuntContext.getPlayingSpeedrunners());

        toGet.forEach((String name, List<UUID> list) -> {
            ctx.player.sendMessage(TextUtil.parse("<yellow><bold>"+name+":</reset>"));
            for (UUID id : list) {
                ctx.player.sendMessage(" - "+Objects.requireNonNull(Bukkit.getPlayer(id)).getName());
            }
        });
        return true;
    }
}

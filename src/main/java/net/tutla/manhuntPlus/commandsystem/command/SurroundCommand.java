package net.tutla.manhuntPlus.commandsystem.command;

import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SurroundCommand extends TutlaCommand {
    public SurroundCommand(){
        super("surround",
                "/surround <player>","Surround speedrunner with hunters. Add hunters (via /manhunt hunter add) to use", CommandSection.SETUP,
                new CommandTabAutoComplete("surround", null, "<player>")
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (ManhuntContext.getSpeedrunners().isEmpty()) {
            ctx.player.sendMessage(TextUtil.parse("<bold><red>Cannot surround player without there being any speedrunners present.</red> Add one with /manhunt speedrunner add"));
            return true;
        }

        if (ctx.args.length >= 1){
            Player target = Bukkit.getPlayer(ctx.args[0]);
            if (target != null && target.isOnline()) {
                if (!ManhuntContext.getSpeedrunners().contains(target.getUniqueId())) {
                    ctx.player.sendMessage(TextUtil.parse("<red>Player is not a speedrunner!</red>"));
                    return true;
                }

                Location center = target.getLocation();
                double radius = ManhuntPlus.getInstance().getConfig().getDouble("surround-radius");
                int n = ManhuntContext.getHunters().size();
                // chatgpt slop, my ass is too stupid to calculate ts
                for (int i = 0; i < n; i++) {
                    Player p = Bukkit.getPlayer(ManhuntContext.getHunters().get(i));

                    double angle = 2 * Math.PI * i / n;
                    double xOffset = radius * Math.cos(angle);
                    double zOffset = radius * Math.sin(angle);

                    Location newLoc = center.clone().add(xOffset, 0, zOffset);
                    newLoc.setDirection(center.toVector().subtract(newLoc.toVector())); // face center
                    p.teleport(newLoc);
                }
                ctx.player.sendMessage(TextUtil.parse("<green>Surrounded  "+target.getName()+"</green>"));
            } else {
                ctx.player.sendMessage(TextUtil.parse("<red>Player not found or not online</red>"));
            }

            return true;
        }
        return false;
    }
}

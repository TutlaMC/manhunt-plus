package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.TwistTrigger;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandTwistLootpool extends TutlaCommand  {
    private static final String usage = "/twist lootpool <twist_name> <lootpool_name>";
    public CommandTwistLootpool(){
        super("lootpool", usage, "Set a twist's lootpool", CommandSection.TWIST,
                new CommandTabAutoComplete("lootpool", List.of(
                        new CommandTabAutoComplete(null, null, "<lootpool>")
                ), "<twist>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 3) {
            if (TwistRegister.twistExists(ctx.args[1])){
                if (LootPoolManager.getAllNames().contains(ctx.args[2])){
                    TwistRegister.getTwist(ctx.args[1]).setLootpool(ctx.args[2]);
                } else {
                    ctx.player.sendMessage(TextUtil.parse("<red>Trigger doesn't exist!"));
                    return false;
                }
            } else {
                ctx.player.sendMessage(TextUtil.parse("<red>Twist doesn't exist!"));
                return false;
            }
            ctx.player.sendMessage(TextUtil.parse("<green>Twist:</green> Twist updated!"));
            return true;
        }
        return false;
    }
}

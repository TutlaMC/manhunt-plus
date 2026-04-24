package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandLootPoolDelete extends TutlaCommand  {
    private static final String usage = "/lootpool delete <name>";
    public CommandLootPoolDelete(){
        super("delete", usage, "Delete a lootpool", CommandSection.TWIST,
                new CommandTabAutoComplete("delete", List.of(), "<lootpool>")
        );
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length == 2) {
            if (LootPoolManager.getMapping().levellingExists(ctx.args[1])){
                LootPoolManager.getMapping().removeLevelling(ctx.args[1]);
            } else {
                ctx.player.sendMessage(TextUtil.parse("<red>Lootpool doesn't exist!"));
                return true;
            }
            ctx.player.sendMessage(TextUtil.parse("<green>Lootpool:</green> Lootpool removed!"));
            return true;
        }
        return false;
    }
}

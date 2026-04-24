package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

import static net.tutla.manhuntPlus.commandsystem.command.lootpool.CommandLootPoolHelp.helpString;

public class CommandLootPoolCreate extends TutlaCommand  {
    private static final String usage = "/lootpool create <name:one_word>";
    public CommandLootPoolCreate(){
        super("create", usage, "Create a new lootpool", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){
        // TODO: show steps/help
        if (ctx.args.length == 2) {
            LootPoolManager.setUninitialisedLootPool(new UninitalisedLootPool(ctx.args[1]));
            ctx.player.sendMessage(TextUtil.parse("<green>LootPool:</green> Started creating lootpool "+ctx.args[1]));
            ctx.player.sendMessage(TextUtil.parse(helpString));
            return true;
        }
        return false;
    }
}

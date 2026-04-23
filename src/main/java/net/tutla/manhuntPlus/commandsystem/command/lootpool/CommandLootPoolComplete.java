package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandLootPoolComplete extends TutlaCommand  {
    private static final String usage = "/lootpool complete";
    public CommandLootPoolComplete(){
        super("complete", usage, "Complete your lootpool", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){
        UninitalisedLootPool uninitalisedLootPool = LootPoolManager.getUninitialisedLootPool();
        LootPoolManager.addLootPool(new LootPoolLevelling(uninitalisedLootPool.id, uninitalisedLootPool.getTiers(), uninitalisedLootPool.getDifficultyMultiplier()));
        ctx.player.sendMessage(TextUtil.parse("<green>LootPool:</green> Completed Creating Lootpool "+ctx.args[1]));
        return true;
    }
}

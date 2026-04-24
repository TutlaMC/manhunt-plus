package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.util.TextUtil;

public class LootPoolCommandUtil {
    public static boolean existsUninitialised(CommandContext ctx){
        if (LootPoolManager.getUninitialisedLootPool() == null){
            ctx.player.sendMessage(TextUtil.parse("<red>You are not creating a lootpool, use /lootpool to get started"));
            return true;
        }
        return false;
    }

    public static boolean existsUninitialisedTier(CommandContext ctx){
        if (existsUninitialised(ctx)) return true;
        System.out.println(LootPoolManager.getUninitialisedLootPool().getUninitialisedTier());
        if (LootPoolManager.getUninitialisedLootPool().getUninitialisedTier() == null){
            ctx.player.sendMessage(TextUtil.parse("<red>You are not creating a tier, use /lootpool tier create to get started"));
            return true;
        }
        return false;
    }
}

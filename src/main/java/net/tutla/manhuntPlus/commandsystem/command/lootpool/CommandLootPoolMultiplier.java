package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandLootPoolMultiplier extends TutlaCommand  {
    private static final String usage = "/lootpool difficultymultiplier <decimal>";
    public CommandLootPoolMultiplier(){
        super("difficultymultiplier", usage, "Set the lootpool difficulty multiplier", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){
        if (ctx.args.length != 2){
            ctx.player.sendMessage(TextUtil.parse("<green>Your current difficulty multiplier is </reset><bold><gray>"+LootPoolManager.getUninitialisedLootPool().getDifficultyMultiplier()));
        } else {
            double num;
            try {
                num = Double.parseDouble(ctx.args[1]);
            } catch (NumberFormatException e) {
                ctx.player.sendMessage("<red>Invalid Number!");
                return true;
            }
            LootPoolManager.getUninitialisedLootPool().setDifficultyMultiplier(num);
            ctx.player.sendMessage(TextUtil.parse("<green>LootPool:</green> Set difficulty multiplier!" + ctx.args[1]));
        }
        return true;
    }
}

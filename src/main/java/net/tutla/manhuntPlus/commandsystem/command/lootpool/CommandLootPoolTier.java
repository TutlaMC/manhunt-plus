package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedTier;
import net.tutla.manhuntPlus.util.TextUtil;

import java.util.List;

public class CommandLootPoolTier extends TutlaCommand  {
    private static final String usage = "/lootpool tier <create|cancel|complete|add|remove> <name>";
    public CommandLootPoolTier(){
        super("tier", usage, "Create a new lootpool", CommandSection.LOOTPOOL,
                new CommandTabAutoComplete("tier", List.of(
                        new CommandTabAutoComplete("create", null, ""),
                        new CommandTabAutoComplete("cancel", null, ""),
                        new CommandTabAutoComplete("complete", null, ""),
                        new CommandTabAutoComplete("remove", null, "<tier>"),
                        new CommandTabAutoComplete("list", null, "")
                ), "<values>")
                        .setValues(List.of("create","cancel","complete", "remove","list"))
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if(LootPoolCommandUtil.existsUninitialised(ctx)) return true;

        if (ctx.args.length >= 2) {
            String sub = ctx.args[1].toLowerCase();
            switch (sub){
                case "create" -> {
                    if (LootPoolManager.getUninitialisedLootPool().getUninitialisedTier() != null){
                        ctx.player.sendMessage(TextUtil.parse("<red>You are already creating a tier!"));
                        return true;
                    }
                    LootPoolManager.getUninitialisedLootPool().setUninitalisedTier(new UninitalisedTier(ctx.args[2])); // TODO: Complete tier
                    ctx.player.sendMessage(TextUtil.parse("<green>Created tier!"));
                    return true;
                }
                case "cancel" -> {
                    LootPoolManager.getUninitialisedLootPool().setUninitalisedTier(null);
                    ctx.player.sendMessage(TextUtil.parse("<green>Cancelled!"));
                    return true;
                }
                case "complete" -> {
                    if (LootPoolCommandUtil.existsUninitialisedTier(ctx)) return true;
                    UninitalisedTier tier = LootPoolManager.getUninitialisedLootPool().getUninitialisedTier();
                    LootPoolManager.getUninitialisedLootPool().getTierBuilder().addTier(tier.id,tier.getLootpool());
                    LootPoolManager.getUninitialisedLootPool().setUninitalisedTier(null);
                    ctx.player.sendMessage(TextUtil.parse("<green>Completed making tier!"));
                    return true;
                }
                case "remove" -> {
                    if (ctx.args.length == 3) {
                        if (LootPoolManager.getUninitialisedLootPool().getAllTierNames().contains(ctx.args[2])){
                            LootPoolManager.getUninitialisedLootPool().removeTierByName(ctx.args[2]);
                            ctx.player.sendMessage(TextUtil.parse("<green>Tier "+ctx.args[2]+" removed"));
                        } else {
                            ctx.player.sendMessage(TextUtil.parse("<red>Tier "+ctx.args[2]+" doesn't exist!"));
                        }
                    } else {
                        ctx.player.sendMessage("<red>Invalid Usage, correct: /lootpool tier remove <name>");
                    }
                    return true;
                }
                case "list" -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<yellow><bold>");
                    builder.append("Lootpool tiers");
                    builder.append("<reset>\n");
                    LootPoolManager.getUninitialisedLootPool().getTiers().forEach((lootTier -> {
                        builder.append("- <cyan>");
                        builder.append(lootTier.getName());
                        builder.append("<reset>");
                        builder.append("\n");
                    }));
                    ctx.player.sendMessage(TextUtil.parse(builder.toString()));
                    return true;
                }
            }
        }
        return false;
    }
}

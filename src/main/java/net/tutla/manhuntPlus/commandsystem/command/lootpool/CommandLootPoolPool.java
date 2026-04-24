package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.CommandTabAutoComplete;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedTier;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.util.TextUtil;
import net.tutla.manhuntPlus.util.TutlaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandLootPoolPool extends TutlaCommand {
    private static final String usage = "/lootpool pool <add|remove|list> <add:item> <add:number> <add:weight> <remove:index>";
    public CommandLootPoolPool(){
        super("pool", usage, "Create a new lootpool", CommandSection.LOOTPOOL,
                new CommandTabAutoComplete("pool", List.of(
                        new CommandTabAutoComplete("add", List.of(), "<enum>").setEnum(Material.class),
                        new CommandTabAutoComplete("remove", null, ""), // TODO: add unpool items
                        new CommandTabAutoComplete("list", null, "")
                ), "<values>")
                        .setValues(List.of("add", "remove", "list"))
        );
    }

    @Override
    public boolean run(CommandContext ctx) {
        if (LootPoolCommandUtil.existsUninitialisedTier(ctx)) return true;
        if (ctx.args.length >= 2) {
            String sub = ctx.args[1].toLowerCase();
            switch (sub){
                case "add" -> {
                    if (ctx.args.length == 5){
                        Material material = Material.getMaterial(ctx.args[2]);
                        if (material == null){
                            ctx.player.sendMessage("<red>Invalid item!");
                            return true;
                        }
                        int stackNumber;
                        int weight;
                        try {
                            stackNumber = Integer.parseInt(ctx.args[3]);
                            weight = Integer.parseInt(ctx.args[4]);
                            if (stackNumber < 1){
                                ctx.player.sendMessage(TextUtil.parse("<red>Item stack should be more than 0!"));
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            ctx.player.sendMessage("<red>Invalid Number!");
                            return true;
                        }
                        LootPoolManager.getUninitialisedLootPool().getUninitialisedTier().getLootpool().addLoot(new ItemStack(material, stackNumber), weight);
                        ctx.player.sendMessage("<green>Added item stack to lootpool!");
                    } else {
                        ctx.player.sendMessage(TextUtil.parse("<red>Invalid usage, correct: /lootpool pool add <NAME OF ITEM> <HOW MANY YOU WANT> <THE CHANCE OF GETTING IT, BIGGER = HIGHER CHANCE>"));
                    }
                    return true;
                }
                case "remove" -> {
                    if (ctx.args.length == 3){
                        int index;
                        try {
                            index = Integer.parseInt(ctx.args[2]);
                        } catch (NumberFormatException e) {
                            ctx.player.sendMessage("<red>Invalid Number!");
                            return true;
                        }
                        List<LootPool.LootEntry> entries = LootPoolManager.getUninitialisedLootPool().getUninitialisedTier().getLootpool().getEntries();
                        if (index > entries.size() - 1 || index < 0){
                            ctx.player.sendMessage(TextUtil.parse("<red>Invalid index!"));
                            return true;
                        }
                        LootPool.LootEntry removed = entries.remove(index);
                        ctx.player.sendMessage(TextUtil.parse(
                                "<green>Removed: <cyan>" + removed.item.getType()
                        ));
                    } else {
                        ctx.player.sendMessage(TextUtil.parse("<red>Invalid Usage, correct: /lootpool pool remove <NUMBER OF WHICH ONE YOU WANT TO REMOVE, IF YOU DON'T KNOW DO /lootpool pool list>"));
                    }
                    return true;
                }
                case "list" -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("<yellow><bold>");
                    builder.append("Lootpool items");
                    builder.append("</reset></yellow>\n");
                    var entries = LootPoolManager
                            .getUninitialisedLootPool()
                            .getUninitialisedTier()
                            .getLootpool()
                            .getEntries();

                    for (LootPool.LootEntry lootEntry : entries) {
                        builder.append(" <gray>-")
                                .append(lootEntry.item.getAmount())
                                .append("x </gray><cyan>")
                                .append(lootEntry.item.getType())
                                .append("<reset> Weight: ")
                                .append(lootEntry.weight)
                                .append("\n");
                    }
                    ctx.player.sendMessage(TextUtil.parse(builder.toString()));
                    return true;
                }
            }
        }
        return false;
    }
}

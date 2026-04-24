package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandLootPoolList extends TutlaCommand  {
    private static final String usage = "/lootpool list";
    public CommandLootPoolList(){
        super("list", usage, "List all created lootpools", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){
        StringBuilder builder = new StringBuilder();
        builder.append("<yellow><bold>");
        builder.append("All Custom Lootpools ");
        builder.append("<reset>\n");
        int e = 0;
        for (String lootTier : LootPoolManager.getAllNames()) {
            builder.append("- [");
            builder.append(e);
            builder.append("]<reset> <cyan>");
            builder.append(lootTier);
            builder.append("<reset>\n");
            e++;
        }
        ctx.player.sendMessage(TextUtil.parse(builder.toString()));
        return true;
    }
}

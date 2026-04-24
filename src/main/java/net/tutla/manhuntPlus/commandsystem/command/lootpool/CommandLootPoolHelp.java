package net.tutla.manhuntPlus.commandsystem.command.lootpool;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandLootPoolHelp extends TutlaCommand  {
    private static final String usage = "/lootpool help";
    public static final String helpString = "<yellow><bold>── Lootpool Creation ── </bold></yellow>\n" +
            "Step 1: Create a lootpool using <gray>/lootpool create <name> </gray>\n" +
            "Step 2: Then create a tier using <gray>/lootpool tier create <name> \nEvery tier contains a set of loot, every time a lootpool is used, the player levels up. So if you want to add a progression based lootpool then add more tiers with each tier giving more loot </gray>\n" +
            "Step 4: Then add/remove items using <gray>/lootpool pool create add <item> <number of items> <chance of getting it>\n The chance of getting it is a weight, the higher it is compared to other items- the higher chance it gets. </gray>\n" +
            "Step 4: Add as many items as you like, when done with that use <gray>/lootpool tier complete</gray>\n" +
            "Step 5: You can now either complete your lootpool if you want a fixed lootpool, or add more tiers (repeat step 2-4) for a levelling system. If your done use: <gray>/lootpool complete</gray>\n" +
            "<click:open_url:'https://wiki.tutla.net/manhunt+/lootpool'><aqua><bold>🌐 Wiki</bold></aqua></click>\n"+
            "<yellow>────────────────────────────</yellow>";
    public CommandLootPoolHelp(){
        super("help", usage, "Figure out how to use this subset of commands", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){

        ctx.player.sendMessage(TextUtil.parse(helpString));
        return true;
    }
}

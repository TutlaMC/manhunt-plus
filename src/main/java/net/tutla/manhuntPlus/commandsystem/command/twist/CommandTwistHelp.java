package net.tutla.manhuntPlus.commandsystem.command.twist;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSection;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LootPoolManager;
import net.tutla.manhuntPlus.lootpool.UninitalisedLootPool;
import net.tutla.manhuntPlus.util.TextUtil;

public class CommandTwistHelp extends TutlaCommand  {
    private static final String usage = "/twist help";
    public static final String helpString = "<yellow><bold>── Twist Creation ── </bold></yellow>\n" +
            "Step 1: Create a twist using <gray>/twist create <name> </gray>\n" +
            "Step 2: Then add a trigger using <gray>/twist trigger <twist> <trigger> \nRefer the wiki for more info </gray>\n" +
            "Step 4: Then add an effect <gray>/twist effect <twist> <effect> </gray>\n" +
            "Step 4 (Optional, If your using GIVE_LOOT effect): Add a custom lootpool <gray>/twist lootpool <twist> <lootpool> </gray>\n" +
            "Step 5: Once done you can use the twist by: <gray>/manhunt twist <twist></gray>\n" +
            "<click:open_url:'https://wiki.tutla.net/manhunt+/twist'><aqua><bold>🌐 Wiki</bold></aqua></click>"+
            "<yellow>────────────────────────────</yellow>";
    public CommandTwistHelp(){
        super("help", usage, "Figure out how to use this subset of commands", CommandSection.LOOTPOOL,  null);
    }

    @Override
    public boolean run(CommandContext ctx){

        ctx.player.sendMessage(TextUtil.parse(helpString));
        return true;
    }
}

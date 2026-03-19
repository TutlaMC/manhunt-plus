package net.tutla.manhuntPlus.commandsystem;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TutlaCommand {

    private final String name;
    private final String help;
    public final CommandTabAutoComplete autocomplete;

    public TutlaCommand(String name, String help, CommandTabAutoComplete autocomplete) {
        this.name = name;
        this.help = help;
        this.autocomplete = autocomplete;
    }

    public String name() {
        return name;
    }

    public abstract boolean run(CommandContext params);
    public void help(Player player){
        player.sendMessage("§eUsage: "+help);
    }
}
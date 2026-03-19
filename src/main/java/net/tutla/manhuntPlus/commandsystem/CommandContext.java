package net.tutla.manhuntPlus.commandsystem;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandContext {
    public Player player;
    public Command cmd;
    public String label;
    public String[] args;
    public CommandContext(Player player, @NotNull Command cmd, @NotNull String label, String @NotNull [] args){
        this.player = player;
        this.cmd = cmd;
        this.label = label;
        this.args = args;
    }
}

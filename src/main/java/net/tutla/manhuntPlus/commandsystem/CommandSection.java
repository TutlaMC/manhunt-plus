package net.tutla.manhuntPlus.commandsystem;

public enum CommandSection {
    SETUP("Setup"),
    CONTROLS("Controls"),
    SETTINGS("Settings"),
    TWIST("Twist"),
    LOOTPOOL("Loot Pool"),
    OTHER("Other"),
    NONE(null);

    public final String label;
    CommandSection(String label) { this.label = label; }
}
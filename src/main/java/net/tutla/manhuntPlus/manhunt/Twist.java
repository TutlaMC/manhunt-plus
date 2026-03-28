package net.tutla.manhuntPlus.manhunt;

public enum Twist {
    DEFAULT("Default","Uses default Manhunt settings"),
    PIG_OP_LOOT("Pigs drop OP loot", "Killing a pig drops OP loot."),
    MILK_HUNTER_OP_LOOT("Milking hunters gives OP Loot", "Inspired by Boosfer- Milking your hunters with a bucket gives you loot."); // not for now cuz of modrinth restrictions

    public final String label;
    public final String description;
    Twist(String label, String description) {
        this.label = label;
        this.description = description;
    }
}

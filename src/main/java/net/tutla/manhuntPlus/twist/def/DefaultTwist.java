package net.tutla.manhuntPlus.twist.def;

import net.tutla.manhuntPlus.twist.Twist;

public class DefaultTwist extends Twist {

    public DefaultTwist() {
        super("default", "Default Manhunt", "No twist");
        configurable = false;
        defaultTwist = true;
    }
}

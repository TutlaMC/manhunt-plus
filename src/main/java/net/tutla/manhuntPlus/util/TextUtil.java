package net.tutla.manhuntPlus.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextUtil {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Component parse(String input) {
        return MM.deserialize(input);
    }
}
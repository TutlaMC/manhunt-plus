package net.tutla.manhuntPlus.util;

import org.bukkit.entity.Player;

public class TutlaUtil {
    public static boolean isTargetValid(Player target){
        return target != null && target.isOnline();
    }
}

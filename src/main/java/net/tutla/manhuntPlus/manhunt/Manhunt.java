package net.tutla.manhuntPlus.manhunt;

import net.tutla.manhuntPlus.twist.Twist;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.def.DefaultTwist;
import org.bukkit.Bukkit;

public class Manhunt {
    private static Boolean started = false;
    public static Boolean getStatus() {
        return started;
    }
    public static void setStatus(Boolean stat) {
        started = stat;
    }

    public static Boolean waitingForStart = false;
    public static Boolean getWaitingForStart() {
        return waitingForStart;
    }
    public static void setWaitingForStart(Boolean stat) {
        waitingForStart = stat;
    }

    private static Twist twist;
    public static Twist getTwist() {
        return twist;
    }
    public static void setTwist(Twist twist) {
        Manhunt.twist.setIsActive(false);
        twist.setIsActive(true);
        Manhunt.twist = twist;
    }

    public static void init(){
        Manhunt.twist = TwistRegister.getTwist("default");
    }

    public static Boolean startManhunt(){
        waitingForStart = false;
        if (!started) {
            Manhunt.setStatus(true);
            TwistRegister.resetAllTwistLootPools();
            ManhuntTimer.startTimer();
            Bukkit.broadcastMessage("§aManhunt started!");
            ManhuntContext.getPlayingSpeedrunners().addAll(ManhuntContext.getSpeedrunners());
            return true;
        }
        return false;
    }

    public static Boolean stopManhunt(){
        if (started) {
            setStatus(false);
            ManhuntTimer.stopTimer();
            ManhuntContext.getPlayingSpeedrunners().clear();
            ManhuntCompass.clearAllCompassData();
            Bukkit.broadcastMessage("§aManhunt stopped!");
            return true;
        }
        return false;
    }

}

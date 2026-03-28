package net.tutla.manhuntPlus.manhunt;

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

    private static Twist twist = Twist.DEFAULT;
    public static Twist getTwist() {
        return twist;
    }
    public static void setTwist(Twist twist) {
        Manhunt.twist = twist;
    }

    public static Boolean startManhunt(){
        waitingForStart = false;
        if (!started) {
            Manhunt.setStatus(true);
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

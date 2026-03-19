package net.tutla.manhuntPlus.manhunt;

import net.tutla.manhuntPlus.ManhuntPlus;
import org.bukkit.Bukkit;

public class ManhuntTimer {
    private static int timer = 0;
    private static int timerTaskId = -1;
    public static int countdownLimitMinutes = 0;

    public static void startTimer() {
        if (timerTaskId != -1) return;

        timer = 0;
        timerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlus.getInstance(), () -> {
            timer++;

            if (timer % ManhuntPlus.getInstance().getConfig().getInt("broadcast-time-every") == 0 && ManhuntPlus.getInstance().getConfig().getBoolean("broadcast-time")){
                Bukkit.broadcastMessage("§eManhunt Timer: " + (timer / 60) + " minute(s)");
            }

            if (countdownLimitMinutes > 0 && timer >= countdownLimitMinutes * 60) {
                Bukkit.broadcastMessage("§cTime's up! Speedrunners failed to win in " + countdownLimitMinutes + " minutes.");
                stopTimer();
            }

        }, 0L, 20L);
    }

    public static void stopTimer() {
        if (timerTaskId != -1) {
            Bukkit.getScheduler().cancelTask(timerTaskId);
            timerTaskId = -1;
            Bukkit.broadcastMessage("§cManhunt stopped at " + (timer / 60) + " minute(s).");
        }
    }

    public static int getCountdownLimitMinutes(){
        return countdownLimitMinutes;
    }

    public static void setCountdownLimitMinutes(int mins){
        countdownLimitMinutes = mins;
    }
}

package net.tutla.manhuntPlus.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ManhuntContext {
    private static final List<UUID> speedrunners = new ArrayList<>();
    private static final List<UUID> playingSpeedrunners = new ArrayList<>();
    private static final List<UUID> hunters = new ArrayList<>();

    public static List<UUID> getPlayingSpeedrunners(){
        return playingSpeedrunners;
    }
    public static List<UUID> getSpeedrunners(){
        return speedrunners;
    }
    public static List<UUID> getHunters(){
        return hunters;
    }
    public List<Player> getPlayers(List<UUID> players){
        List<Player> e = new ArrayList<>();

        for (UUID p : players){
            e.add(Bukkit.getPlayer(p));
        }

        return e;
    }
    public static void addSpeedrunner(Player player){
        if (!speedrunners.contains((player.getUniqueId()))){
            speedrunners.add(player.getUniqueId());
        }
    }
    public static void removeSpeedrunner(Player player){
        if (speedrunners.contains((player.getUniqueId()))){
            speedrunners.remove(player.getUniqueId());
        }
    }
    public static boolean isSpeedrunner(Player player){
        return speedrunners.contains(player.getUniqueId());
    }

    public static void removeHunter(Player player){
        hunters.remove(player.getUniqueId());
    }
    public static void addHunter(Player player){
        if (!hunters.contains((player.getUniqueId()))){
            hunters.add(player.getUniqueId());
        }
    }
    public static boolean isHunter(Player player){
        return hunters.contains(player.getUniqueId());
    }

    public static void removePlayingSpeedrunner(Player player){
        if (playingSpeedrunners.contains((player.getUniqueId()))){
            playingSpeedrunners.remove(player.getUniqueId());
        }
    }
    public List<Player> getOpponents(Player player) {
        if (ManhuntContext.getHunters().contains(player.getUniqueId())) {
            return getPlayers(ManhuntContext.getSpeedrunners());
        } else if (ManhuntContext.getSpeedrunners().contains(player.getUniqueId())) {
            return getPlayers(ManhuntContext.getHunters());
        }
        return Collections.emptyList();
    }
}

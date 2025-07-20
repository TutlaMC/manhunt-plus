package net.tutla.manhuntPlus;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum Twist {
    DEFAULT,
    PIG_OP_LOOT,

}

public final class ManhuntPlus extends JavaPlugin {
    private static ManhuntPlus instance;
    private final List<Player> speedrunners = new ArrayList<>();
    private Twist twist = Twist.DEFAULT;

    private static LootPool basicLootPool;

    public static ManhuntPlus getInstance() {
        return instance;
    }

    public List<Player> getSpeedrunners(){
        return speedrunners;
    }

    public void addSpeedrunner(Player player){
        if (!speedrunners.contains((player))){
            speedrunners.add(player);
        }
    }
    public void removeSpeedrunner(Player player){
        if (speedrunners.contains((player))){
            speedrunners.remove(player);
        }
    }
    public Twist getTwist() {
        return twist;
    }
    public void setTwist(Twist twist) {
        this.twist = twist;
    }
    public LootPool getDefaultLoot() {
        return basicLootPool;
    }

    @Override
    public void onEnable() {
        instance = this;
        basicLootPool = LootPool.createDefault();

        getServer().getPluginManager().registerEvents(new EventListeners(), this);
        getLogger().info("Manhunt plugin loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye :(");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("compass")) {
            if (speedrunners.isEmpty()) {
                player.sendMessage("No speedrunner set.");
                return true;
            }

            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (!speedrunners.contains(target)) {
                        player.sendMessage("Player is not a speedrunner!");
                        return true;
                    }
                    ItemStack compass = new ItemStack(Material.COMPASS);
                    CompassMeta meta = (CompassMeta) compass.getItemMeta();
                    if (meta != null) {
                        Location loc = target.getLocation();
                        meta.setLodestone(loc);
                        meta.setLodestoneTracked(false);
                        compass.setItemMeta(meta);
                    }
                    player.getInventory().addItem(compass);
                    player.sendMessage("Tracking compass given for speedrunner "+target.getName());
                } else {
                    player.sendMessage("Player not found or not online");
                }

                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("speedrunner")){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    addSpeedrunner(target);
                    Bukkit.broadcastMessage(target.getName() + " is now a speedrunner!");
                } else {
                    player.sendMessage("Player not found or not online.");
                }
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("removespeedrunner")){
            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    removeSpeedrunner(target);
                    Bukkit.broadcastMessage(target.getName() + " is no longer a speedrunner!");
                } else {
                    player.sendMessage("Player not found or not online.");
                }
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("twist")) {
            if (args.length == 1) {
                try {
                    Twist selected = Twist.valueOf(args[0].toUpperCase());
                    setTwist(selected);
                    player.sendMessage("§aTwist set to: " + selected.name());
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cUnknown twist: " + args[0]);
                    player.sendMessage("§eAvailable twists: " + Arrays.toString(Twist.values()));
                }
                return true;
            } else {
                player.sendMessage("§eCurrent Twist is "+twist);
                player.sendMessage("§eUsage: /twist <twistName>");
            }
        }

        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("twist") && args.length == 1) {
            return Arrays.stream(Twist.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}

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

import java.util.*;

enum Twist {
    DEFAULT,
    PIG_OP_LOOT,
    MILK_HUNTER_OP_LOOT
}

public final class ManhuntPlus extends JavaPlugin {
    private static ManhuntPlus instance;
    private Boolean started = false;
    // runners/hunters
    private final List<Player> speedrunners = new ArrayList<>();
    private final List<Player> hunters = new ArrayList<>();
    // settings
    private Twist twist = Twist.DEFAULT;
    private int timer = 0;
    private int timerTaskId = -1;
    private int countdownLimitMinutes = 0;
    private Boolean broadcastRemainingTime = false;


    // loot defs
    private static LootPool basicLootPool;

    // access functions
    public static ManhuntPlus getInstance() {
        return instance;
    }
    public Boolean getStatus() {
        return started;
    }
    public void setStatus(Boolean stat) {
        started = stat;
    }

    public List<Player> getSpeedrunners(){
        return speedrunners;
    }
    public List<Player> getHunters(){
        return hunters;
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
    public void addHunter(Player player){
        if (!hunters.contains((player))){
            hunters.add(player);
        }
    }
    public void removeHunter(Player player){
        hunters.remove(player);
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

    public void startTimer() {
        if (timerTaskId != -1) return;

        timer = 0;
        timerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            timer++;

            if (timer % 60 == 0)
                Bukkit.broadcastMessage("§eManhunt Timer: " + (timer / 60) + " minute(s)");

            if (countdownLimitMinutes > 0 && timer >= countdownLimitMinutes * 60) {
                Bukkit.broadcastMessage("§cTime's up! Speedrunners failed to win in " + countdownLimitMinutes + " minutes.");
                stopTimer();
            }

        }, 0L, 20L);
    }

    public void stopTimer() {
        if (timerTaskId != -1) {
            Bukkit.getScheduler().cancelTask(timerTaskId);
            timerTaskId = -1;
            Bukkit.broadcastMessage("§cManhunt stopped at " + (timer / 60) + " minute(s).");
        }
    }


    // actual shi
    @Override
    public void onEnable() {
        instance = this;
        basicLootPool = LootPool.createDefault();
        TwistsHelper helper = new TwistsHelper();

        getServer().getPluginManager().registerEvents(new EventListeners(helper), this);
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
                player.sendMessage("§cNo speedrunner set.");
                return true;
            }

            if (args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (!speedrunners.contains(target)) {
                        player.sendMessage("§cPlayer is not a speedrunner!");
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
                    player.sendMessage("§cPlayer not found or not online");
                }

                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("speedrunner")){
            if (args.length == 2){
                if (args[0].equalsIgnoreCase("add")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        if (!speedrunners.contains(target)){
                            addSpeedrunner(target);
                            Bukkit.broadcastMessage("§a"+target.getName() + " is now a speedrunner!");
                        } else {
                            player.sendMessage("§cPlayer is already a speedrunner!");
                        }

                    } else {
                        player.sendMessage("§cPlayer not found or not online.");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        if (speedrunners.contains(target)){
                            removeSpeedrunner(target);
                            Bukkit.broadcastMessage("§a"+target.getName() + " is no longer a speedrunner!");
                        } else {
                            player.sendMessage("§cPlayer is not a speedrunner!");
                        }
                    } else {
                        player.sendMessage("§cPlayer not found or not online.");
                    }
                    return true;
                }
            }
            return false;
        } else if (cmd.getName().equalsIgnoreCase("hunter")){
            if (args.length == 2){
                if (args[0].equalsIgnoreCase("add")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        if (!hunters.contains(target)){
                            addHunter(target);
                            Bukkit.broadcastMessage("§a"+target.getName() + " is now a hunter!");
                        } else {
                            player.sendMessage("§cPlayer is already a hunter!");
                        }

                    } else {
                        player.sendMessage("§cPlayer not found or not online.");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null && target.isOnline()) {
                        if (hunters.contains(target)){
                            removeHunter(target);
                            Bukkit.broadcastMessage("§a"+target.getName() + " is no longer a hunter!");
                        } else {
                            player.sendMessage("§cPlayer is not a hunter!");
                        }
                    } else {
                        player.sendMessage("§cPlayer not found or not online.");
                    }
                    return true;
                }
            }
            return false;
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
        } else if (cmd.getName().equalsIgnoreCase("manhunt")) {
            if (args.length == 0) {
                player.sendMessage("§eUsage: /manhunt <start|stop|countdown>");
                return true;
            }

            switch (args[0].toLowerCase()) { // first time i actually used a switch case my whole life (if statements worked well so like it wasn't necessary but like worth trying)
                case "start" -> {
                    if (started) {
                        player.sendMessage("§cManhunt already running.");
                    } else {
                        Bukkit.broadcastMessage("§aManhunt started!");
                        setStatus(true);
                        startTimer();
                    }
                }
                case "stop" -> {
                    if (started) {
                        stopTimer();
                        setStatus(false);
                    } else {
                        player.sendMessage("§cManhunt is not running.");
                    }
                }
                case "countdown" -> {
                    if (args.length == 2) {
                        try {
                            int mins = Integer.parseInt(args[1]);
                            countdownLimitMinutes = mins;
                            if (mins > 0) {
                                player.sendMessage("§aCountdown set to " + mins + " minute(s).");
                            } else {
                                player.sendMessage("§eCountdown disabled.");
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cInvalid number.");
                        }
                    } else {
                        if (countdownLimitMinutes > 0)
                            player.sendMessage("§eCurrent countdown limit: " + countdownLimitMinutes + " minute(s).");
                        else
                            player.sendMessage("§eCountdown is disabled.");
                        player.sendMessage("§eUse /manhunt countdown <minutes>, set minutes to 0 to disable");
                    }
                }
                case "help" -> {
                    player.sendMessage("To use our plugin start adding speedrunners with: /speedrunner add, use /speedrunner remove to remove a speedrunner");
                    player.sendMessage("You can then add the hunters using /hunter add & remove them using /hunter remove");
                    player.sendMessage("To start the manhunt run §e/manhunt start");
                    player.sendMessage("Manhunt countdown (max limit a manhunt can last in minutes) can be set using §e/manhunt cooldown");
                    player.sendMessage("Read full documentation on Modrinth/Discord/Website");
                }
                default -> player.sendMessage("§cUnknown subcommand. Use: start, stop, countdown");
            }

            return true;
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
        } else if (cmd.getName().equalsIgnoreCase("speedrunner") || cmd.getName().equalsIgnoreCase("hunter")) {
            if (args.length == 1) {
                return Arrays.asList("add", "remove").stream()
                        .filter(o -> o.startsWith(args[0].toLowerCase()))
                        .toList();
            } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }
        } else if (cmd.getName().equalsIgnoreCase("manhunt")) {
            if (args.length == 1) {
                return Arrays.asList("help","start", "stop", "countdown").stream()
                        .filter(s -> s.startsWith(args[0].toLowerCase()))
                        .toList();
            } else if (args.length == 2 && args[0].equalsIgnoreCase("countdown")) {
                return List.of("0", "5", "10", "15", "30");
            }
        }
        return Collections.emptyList();
    }
}

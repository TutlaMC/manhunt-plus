package net.tutla.manhuntPlus;

import net.tutla.manhuntPlus.lootpool.LevellingFactory;
import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public final class ManhuntPlus extends JavaPlugin {
    // runtime shit
    private static ManhuntPlus instance;
    private Boolean started = false;
    public Boolean waitingForStart = false;

    public static final NamespacedKey COMPASS_ID_KEY = new NamespacedKey("manhuntplus", "feetpics");
    // runners/hunters
    private final List<UUID> speedrunners = new ArrayList<>();
    private final List<UUID> playingSpeedrunners = new ArrayList<>();
    private final List<UUID> hunters = new ArrayList<>();
    // settings
    private Twist twist = Twist.DEFAULT;
    private int timer = 0;
    private int timerTaskId = -1;
    private int countdownLimitMinutes = 0;
    // compass shit
    Map<UUID, Player> trackedCompasses = new HashMap<>();

    // twist shit
    // ik the code is shit but like im shit at naming
    public static Map<UUID, LootPoolLevelling> playerLootPoolLevels = new HashMap<>();

    public static LootPoolLevelling addPlayerLevellingLootPool(Player player) {
        LootPoolLevelling levelling = new LootPoolLevelling(LevellingFactory.createAllTiers(), 1.25);
        playerLootPoolLevels.put(player.getUniqueId(), levelling);
        return levelling;
    }

    public static void giveLootToLeveller(Player player) {
        LootPoolLevelling pool = playerLootPoolLevels.get(player.getUniqueId());
        if (pool == null) {
            pool = addPlayerLevellingLootPool(player);
        }
        ItemStack loot = pool.getLoot();
        player.getInventory().addItem(loot);
    }


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

    public Map<UUID, Player> getTrackedCompasses(){
        return trackedCompasses;
    }
    public List<UUID> getPlayingSpeedrunners(){
        return playingSpeedrunners;
    }
    public List<UUID> getSpeedrunners(){
        return speedrunners;
    }
    public List<UUID> getHunters(){
        return hunters;
    }

    public void addSpeedrunner(Player player){
        if (!speedrunners.contains((player.getUniqueId()))){
            speedrunners.add(player.getUniqueId());
        }
    }
    public void removeSpeedrunner(Player player){
        if (speedrunners.contains((player.getUniqueId()))){
            speedrunners.remove(player.getUniqueId());
        }
    }
    public void removePlayingSpeedrunner(Player player){
        if (playingSpeedrunners.contains((player.getUniqueId()))){
            playingSpeedrunners.remove(player.getUniqueId());
        }
    }
    public void addHunter(Player player){
        if (!hunters.contains((player.getUniqueId()))){
            hunters.add(player.getUniqueId());
        }
    }
    public void removeHunter(Player player){
        hunters.remove(player.getUniqueId());
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

            if (timer % getConfig().getInt("broadcast-time-every") == 0 && getConfig().getBoolean("broadcast-time")){
                Bukkit.broadcastMessage("§eManhunt Timer: " + (timer / 60) + " minute(s)");
            }

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

    public Boolean startManhunt(){
        waitingForStart = false;
        if (!started) {
            setStatus(true);
            startTimer();
            Bukkit.broadcastMessage("§aManhunt started!");
            playingSpeedrunners.addAll(speedrunners);
            return true;
        }
        return false;
    }

    public Boolean stopManhunt(){
        if (started) {
            setStatus(false);
            stopTimer();
            playingSpeedrunners.clear();
            Bukkit.broadcastMessage("§aManhunt stopped!");
            return true;
        }
        return false;
    }

    public List<Player> getOpponents(Player player) {
        if (hunters.contains(player.getUniqueId())) {
            return getPlayers(speedrunners);
        } else if (speedrunners.contains(player.getUniqueId())) {
            return getPlayers(hunters);
        }
        return Collections.emptyList();
    }

    public List<Player> getPlayers(List<UUID> players){
        List<Player> e = new ArrayList<>();

        for (UUID p : players){
            e.add(Bukkit.getPlayer(p));
        }

        return e;
    }

    // utils
    public void giveCompass(Player target, Player player){
        if (!speedrunners.contains(target.getUniqueId())) {
            player.sendMessage("§cPlayer is not a speedrunner!");
            return;
        }

        if (target != null && target.isOnline()) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            if (meta != null) {
                Location loc = target.getLocation();
                meta.setLodestone(loc);
                meta.setLodestoneTracked(false);

                UUID compassId = UUID.randomUUID();
                meta.getPersistentDataContainer().set(COMPASS_ID_KEY, PersistentDataType.STRING, compassId.toString());
                if (getConfig().getBoolean("name-tracking-compass")){
                    meta.setDisplayName("§bTracking §e" + target.getName());
                }
                compass.setItemMeta(meta);
                trackedCompasses.put(compassId, target);
            }

            player.getInventory().addItem(compass);
            player.sendMessage("Tracking compass given for speedrunner " + target.getName());
        } else {
            player.sendMessage("§cPlayer not found or not online");
        }
    }


    // actual shi
    @Override
    public void onEnable() {
        instance = this;
        basicLootPool = LootPool.createDefault();
        TwistsHelper helper = new TwistsHelper();

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EventListeners(helper), this);
        getLogger().info("Manhunt plugin loaded!");

        new BukkitRunnable() {
            public void run() {
                if (getConfig().getBoolean("auto-calibration")){
                    Iterator<Map.Entry<UUID, Player>> itr = trackedCompasses.entrySet().iterator();
                    while (itr.hasNext()) {
                        Map.Entry<UUID, Player> entry = itr.next();
                        UUID compassId = entry.getKey();
                        Player target = entry.getValue();

                        if (target == null || !target.isOnline()) {
                            itr.remove();
                            continue;
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            for (ItemStack item : p.getInventory().getContents()) {
                                if (item == null || item.getType() != Material.COMPASS) continue;

                                CompassMeta meta = (CompassMeta) item.getItemMeta();
                                if (meta == null) continue;

                                String id = meta.getPersistentDataContainer().get(COMPASS_ID_KEY, PersistentDataType.STRING);
                                if (id != null && id.equals(compassId.toString())) {
                                    meta.setLodestone(target.getLocation());
                                    meta.setLodestoneTracked(false);
                                    item.setItemMeta(meta);
                                }
                            }
                        }
                    }
                }

            }
        }.runTaskTimer(this, 0L, getConfig().getLong("auto-calibration-interval")*20);

    }

    @Override
    public void onDisable() {
        getLogger().info("Bye :(");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("compass")) {
            if (speedrunners.isEmpty()) {
                player.sendMessage("§cNo speedrunner set.");
                return true;
            }

            Player target;
            if (args.length >= 1){
                target = Bukkit.getPlayer(args[0]);
            } else {
                target = Bukkit.getPlayer(speedrunners.getFirst());
            }
            giveCompass(target, player);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("speedrunner")){
            if (args.length == 2){
                if (args[0].equalsIgnoreCase("add")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if (hunters.contains(target.getUniqueId())) {
                        player.sendMessage("§cPlayer is a hunter!");
                        return true;
                    }
                    if (target != null && target.isOnline()) {
                        if (!speedrunners.contains(target.getUniqueId())){
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
                        if (speedrunners.contains(target.getUniqueId())){
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
                    if (speedrunners.contains(target.getUniqueId())) {
                        player.sendMessage("§cPlayer is a speedrunner!");
                        return true;
                    }
                    if (target != null && target.isOnline()) {
                        if (!hunters.contains(target.getUniqueId())){
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
                        if (hunters.contains(target.getUniqueId())){
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
                player.sendMessage("§eUsage: /manhunt <start|stop|countdown|prepare|list|help>");
                return true;
            }

            switch (args[0].toLowerCase()) { // first time i actually used a switch case my whole life (if statements worked well so like it wasn't necessary but like worth trying)
                case "start" -> {
                    if (!startManhunt()){
                        player.sendMessage("§cManhunt already running.");
                    }
                }
                case "stop" -> {
                    if (!stopManhunt()){
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
                case "prepare" -> {
                    waitingForStart = true;
                    Bukkit.broadcastMessage("Waiting for speedrunner first hit");
                }
                case "list" -> {
                    player.sendMessage("§eSpeedrunners:");
                    for (UUID id : speedrunners){
                        player.sendMessage(Bukkit.getPlayer(id).getName());
                    }
                    player.sendMessage("§eHunters:");
                    for (UUID id : hunters){
                        player.sendMessage(Bukkit.getPlayer(id).getName());
                    }
                    player.sendMessage("§ePlaying Speedrunners:");
                    for (UUID id : playingSpeedrunners){
                        player.sendMessage(Bukkit.getPlayer(id).getName());
                    }
                }
                default -> player.sendMessage("§cUnknown subcommand. Use: start, stop, countdown");
            }

            return true;
        } else if (cmd.getName().equalsIgnoreCase("surround")){
            if (speedrunners.isEmpty()) {
                player.sendMessage("§cNo speedrunner set.");
                return true;
            }

            if (args.length >= 1){
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (!speedrunners.contains(target.getUniqueId())) {
                        player.sendMessage("§cPlayer is not a speedrunner!");
                        return true;
                    }

                    Location center = target.getLocation();
                    double radius = getConfig().getDouble("surround-radius");
                    int n = hunters.size();
                    // chatgpt slop, my ass is too stupid to calculate ts
                    for (int i = 0; i < n; i++) {
                        Player p = Bukkit.getPlayer(hunters.get(i));

                        double angle = 2 * Math.PI * i / n;
                        double xOffset = radius * Math.cos(angle);
                        double zOffset = radius * Math.sin(angle);

                        Location newLoc = center.clone().add(xOffset, 0, zOffset);
                        newLoc.setDirection(center.toVector().subtract(newLoc.toVector())); // face center
                        p.teleport(newLoc);
                    }
                    player.sendMessage("§aSurrounded  "+target.getName());
                } else {
                    player.sendMessage("§cPlayer not found or not online");
                }

                return true;
            }

        }

        return false;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String @NotNull [] args) {
        if (cmd.getName().equalsIgnoreCase("twist") && args.length == 1) {
            return Arrays.stream(Twist.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (cmd.getName().equalsIgnoreCase("speedrunner") || cmd.getName().equalsIgnoreCase("hunter")) {
            if (args.length == 1) {
                return Stream.of("add", "remove")
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
                return Stream.of("help","start", "stop", "countdown","prepare", "list")
                        .filter(s -> s.startsWith(args[0].toLowerCase()))
                        .toList();
            } else if (args.length == 2 && args[0].equalsIgnoreCase("countdown")) {
                return List.of("0", "5", "10", "15", "30");
            }
        }
        return Collections.emptyList();
    }

    enum Twist {
        DEFAULT,
        PIG_OP_LOOT,
        SUSSY
        // MILK_HUNTER_OP_LOOT // not for now cuz of modrinth restrictions
    }
}

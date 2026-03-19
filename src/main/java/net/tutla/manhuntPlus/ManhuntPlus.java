package net.tutla.manhuntPlus;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSystem;
import net.tutla.manhuntPlus.commandsystem.TutlaCommand;
import net.tutla.manhuntPlus.lootpool.LevellingFactory;
import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.manhunt.Manhunt;
import net.tutla.manhuntPlus.manhunt.ManhuntContext;
import net.tutla.manhuntPlus.manhunt.ManhuntTimer;
import net.tutla.manhuntPlus.manhunt.Twist;
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
    private final Manhunt manhunt = new Manhunt();
    private final CommandSystem commandSystem = new CommandSystem();


    public static final NamespacedKey COMPASS_ID_KEY = new NamespacedKey("manhuntplus", "feetpics");

    // compass shit
    static Map<UUID, Player> trackedCompasses = new HashMap<>();
    public Map<UUID, Player> getTrackedCompasses(){
        return trackedCompasses;
    }
    public static void updateCompass(ItemStack item, UUID compassId, Player target){
        if (item == null || item.getType() != Material.COMPASS) return;

        CompassMeta meta = (CompassMeta) item.getItemMeta();
        if (meta == null) return;

        String id = meta.getPersistentDataContainer().get(COMPASS_ID_KEY, PersistentDataType.STRING);
        if (id != null && id.equals(compassId.toString())) {
            if (!target.isSneaking()){
                meta.setLodestone(target.getLocation());
                meta.setLodestoneTracked(false);
                item.setItemMeta(meta);
            }
        }
    }


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





    public LootPool getDefaultLoot() {
        return basicLootPool;
    }

    // utils
    public void giveCompass(Player target, Player player){
        if (!ManhuntContext.getPlayingSpeedrunners().contains(target.getUniqueId())) {
            player.sendMessage("§cPlayer is not a playing speedrunner!");
            return;
        }

        if (target.isOnline()) {
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
            player.sendMessage("§bTracking compass given for speedrunner " + target.getName());
        } else {
            player.sendMessage("§cPlayer is not online");
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

                        if (target == null) {
                            itr.remove();
                            continue;
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            for (ItemStack item : p.getInventory().getContents()) {
                                if (!target.isOnline()){
                                    updateCompass(item, compassId, target);
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
        if (!(sender instanceof Player player)) return false;
        CommandContext cmdCtx = new CommandContext(player, cmd, label, args);
        return commandSystem.execute(cmdCtx);
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) return new ArrayList<>();
        return commandSystem.tabComplete(new CommandContext(player, cmd, label, args));
        /*
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
        }*/

    }
}

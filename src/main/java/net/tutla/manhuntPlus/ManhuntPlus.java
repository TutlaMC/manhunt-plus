package net.tutla.manhuntPlus;

import net.tutla.manhuntPlus.commandsystem.CommandContext;
import net.tutla.manhuntPlus.commandsystem.CommandSystem;
import net.tutla.manhuntPlus.lootpool.LevellingFactory;
import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.lootpool.LootPoolLevelling;
import net.tutla.manhuntPlus.manhunt.*;
import net.tutla.manhuntPlus.twist.TwistRegister;
import net.tutla.manhuntPlus.twist.TwistsHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ManhuntPlus extends JavaPlugin {
    // runtime shit
    private static ManhuntPlus instance;
    private final CommandSystem commandSystem = new CommandSystem();

    public static final NamespacedKey COMPASS_ID_KEY = new NamespacedKey("manhuntplus", "feetpics");

    // access functions
    public static ManhuntPlus getInstance() {
        return instance;
    }

    // twist shit
    // ik the code is shit but like im shit at naming
    public static Map<UUID, LootPoolLevelling> playerLootPoolLevels = new HashMap<>();

    private static LootPool basicLootPool;
    public LootPool getDefaultLoot() {
        return basicLootPool;
    }

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


    // actual shi
    @Override
    public void onEnable() {
        instance = this;
        basicLootPool = LootPool.createDefault();
        TwistsHelper helper = new TwistsHelper();

        saveDefaultConfig();

        commandSystem.initialise();
        TwistRegister.init();
        getServer().getPluginManager().registerEvents(new EventListeners(helper), this);
        getLogger().info("Manhunt plugin loaded!");

        new BukkitRunnable() {
            public void run() {
                if (getConfig().getBoolean("auto-calibration")){
                    ManhuntCompass.calibrateAllCompasses();
                }

            }
        }.runTaskTimer(this, 0L, getConfig().getLong("auto-calibration-interval")*20);
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
    }
}
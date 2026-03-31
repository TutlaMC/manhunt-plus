package net.tutla.manhuntPlus.manhunt;

import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.util.TextUtil;
import net.tutla.manhuntPlus.util.TutlaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ManhuntCompass {
    public static Map<UUID, UUID> trackedCompasses = new HashMap<>(); // compassId -> targetUUID
    private static Map<UUID, PortalInfo> playerPortalInfo = new HashMap<>();

    public static void giveCompass(Player target, Player player) {
        if (!ManhuntContext.getPlayingSpeedrunners().contains(target.getUniqueId())) {
            player.sendMessage("<red>Player is not a playing speedrunner!</red> Start the manhunt with /manhunt start to use it!");
            return;
        }

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        if (meta == null) return;

        UUID compassId = UUID.randomUUID();

        meta.setLodestone(target.getLocation());
        meta.setLodestoneTracked(false);
        meta.getPersistentDataContainer().set(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING, compassId.toString());

        if (ManhuntPlus.getInstance().getConfig().getBoolean("name-tracking-compass")) {
            meta.setDisplayName("§bTracking §e" + target.getName());
        }

        compass.setItemMeta(meta);
        trackedCompasses.put(compassId, target.getUniqueId());
        player.getInventory().addItem(compass);
        player.sendMessage("§bTracking compass given for speedrunner " + target.getName());
    }

    public static void calibrateAllCompasses() {
        for (Map.Entry<UUID, UUID> entry : trackedCompasses.entrySet()) {
            UUID compassId = entry.getKey();
            Player target = Bukkit.getPlayer(entry.getValue()); // always fresh

            if (target == null || !target.isOnline()) continue;

            for (Player hunter : Bukkit.getOnlinePlayers()) {
                for (ItemStack item : hunter.getInventory().getContents()) {
                    updateCompass(item, compassId, hunter, target);
                }
            }
        }
    }

    public static void updateCompass(ItemStack item, UUID compassId, Player hunter, Player target) {
        if (item == null || item.getType() != Material.COMPASS) return;
        CompassMeta meta = (CompassMeta) item.getItemMeta();
        if (meta == null) return;

        String id = meta.getPersistentDataContainer().get(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING);
        if (id == null || !id.equals(compassId.toString())) return;
        if (!TutlaUtil.isTargetValid(target)) return;
        if (target.isSneaking()) return;

        Location trackTo = getTrackingLocation(hunter, target);
        if (trackTo == null) return;

        meta.setLodestone(trackTo);
        meta.setLodestoneTracked(false);
        item.setItemMeta(meta);


        if (!hunter.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
            String targetDim = switch (target.getWorld().getEnvironment()) {
                case NETHER -> "the Nether";
                case THE_END -> "the End";
                default -> "the Overworld";
            };
            hunter.sendActionBar(TextUtil.parse(
                    "<gray>" + target.getName() + " is in <white>" + targetDim + "</white> – pointing to portal</gray>"
            ));
        }
    }

    // right click handler can now do:
    public static Player getTarget(UUID compassId) {
        UUID targetId = trackedCompasses.get(compassId);
        return targetId != null ? Bukkit.getPlayer(targetId) : null;
    }

    public static void removeTracking(Player target) {
        trackedCompasses.entrySet().removeIf(e -> e.getValue().equals(target.getUniqueId()));
    }

    public static void clearAllCompassData() {
        trackedCompasses.clear();
    }

    public static Location getTrackingLocation(Player hunter, Player target) {
        World.Environment hunterWorld = hunter.getWorld().getEnvironment();
        World.Environment targetWorld = target.getWorld().getEnvironment();

        if (hunter.getWorld().getEnvironment().equals(target.getWorld().getEnvironment())) {
            return target.getLocation(); // same dimension, track directly
        }
        PortalInfo info = getPlayerPortalInfo(target.getUniqueId());
        if (info == null) return target.getLocation();
        switch (hunterWorld){
            case NORMAL -> {
                if (targetWorld == World.Environment.THE_END){
                    return info.overworldToEndPortal;
                } else {
                    return info.overworldToNetherPortal;
                }
            }
            case NETHER -> {
                return info.netherToOverworldPortal;
            }
            default -> {
                return info.endToOverworldPortal;
            }
        }
    }

    public static PortalInfo getPlayerPortalInfo(UUID id){
        return playerPortalInfo.get(id);
    }

    public static void setPlayerPortalInfo(UUID player, PortalInfo info){
        playerPortalInfo.put(player, info);
    }
}

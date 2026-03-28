package net.tutla.manhuntPlus.manhunt;

import net.tutla.manhuntPlus.ManhuntPlus;
import net.tutla.manhuntPlus.util.TextUtil;
import net.tutla.manhuntPlus.util.TutlaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

        if (!hunter.getWorld().getName().equals(target.getWorld().getName())) {
            String targetDim = switch (target.getWorld().getEnvironment()) {
                case NETHER -> "the Nether";
                case THE_END -> "the End";
                default -> "the Overworld";
            };
            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeee");
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
        String hunterWorld = hunter.getWorld().getEnvironment().name();
        String targetWorld = target.getWorld().getEnvironment().name();

        if (hunter.getWorld().equals(target.getWorld())) {
            return target.getLocation(); // same dimension, track directly
        }

        return switch (targetWorld) {
            case "NETHER" -> // target is in nether, hunter is in overworld — point to nether portal
                    findNearestNetherPortal(hunter);
            case "THE_END" -> // target is in end — point to stronghold/end portal
                    findStronghold(hunter);
            default -> // target is in overworld, hunter is in nether — point to their nether portal
                    findNearestNetherPortal(hunter);
        };
    }

    private static Location findNearestNetherPortal(Player player) {
        // Search in a radius for nether portal blocks
        Location loc = player.getLocation();
        int radius = 128;

        for (int x = -radius; x <= radius; x += 4) {
            for (int y = 0; y <= 256; y += 4) {
                for (int z = -radius; z <= radius; z += 4) {
                    Location check = loc.clone().add(x, y - loc.getBlockY(), z);
                    if (check.getBlock().getType() == Material.NETHER_PORTAL) {
                        return check;
                    }
                }
            }
        }

        return null; // no portal found nearby
    }

    private static Location findStronghold(Player player) {
        // Use locate to find nearest stronghold
        Location loc = player.getLocation();
        var stronghold = player.getWorld().locateNearestStructure(
                loc,
                org.bukkit.generator.structure.Structure.STRONGHOLD,
                100,
                false
        );
        return stronghold != null ? stronghold.getLocation() : null;
    }
}

package net.tutla.manhuntPlus.manhunt;

import net.tutla.manhuntPlus.ManhuntPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ManhuntCompass {

    static Map<UUID, Player> trackedCompasses = new HashMap<>();
    public static Map<UUID, Player> getTrackedCompasses(){
        return trackedCompasses;
    }
    public static void updateCompass(ItemStack item, UUID compassId, Player target){
        if (item == null || item.getType() != Material.COMPASS) return;

        CompassMeta meta = (CompassMeta) item.getItemMeta();
        if (meta == null) return;

        String id = meta.getPersistentDataContainer().get(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING);
        if (id != null && id.equals(compassId.toString())) {
            if (!target.isSneaking()){
                meta.setLodestone(target.getLocation());
                meta.setLodestoneTracked(false);
                item.setItemMeta(meta);
            }
        }
    }
    public static void giveCompass(Player target, Player player){
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
                meta.getPersistentDataContainer().set(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING, compassId.toString());
                if (ManhuntPlus.getInstance().getConfig().getBoolean("name-tracking-compass")){
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

    public static void calibrateAllCompasses(){
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

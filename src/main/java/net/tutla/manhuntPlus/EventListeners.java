package net.tutla.manhuntPlus;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import net.tutla.manhuntPlus.LootPool;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EventListeners implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (ManhuntPlus.getInstance().getSpeedrunners().contains(player)){
            Bukkit.broadcastMessage("Speedrunner "+player.getName()+" has been eliminated");
            ManhuntPlus.getInstance().removeSpeedrunner(player);
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PIG) {
            if (ManhuntPlus.getInstance().getTwist() != Twist.PIG_OP_LOOT) return;

            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            if (!ManhuntPlus.getInstance().getSpeedrunners().contains(killer)) return;

            event.getDrops().clear();
            LootPool pool = ManhuntPlus.getInstance().getDefaultLoot();

            for (int i = 0; i < 2 + new Random().nextInt(3); i++) {
                ItemStack drop = pool.getRandomLoot();
                if (drop != null) event.getDrops().add(drop);
            }
        }
    }

}

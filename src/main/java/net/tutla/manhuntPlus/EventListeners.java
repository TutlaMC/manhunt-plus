package net.tutla.manhuntPlus;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EventListeners implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (ManhuntPlus.getInstance().getSpeedrunners().contains(player)){
            Bukkit.broadcastMessage("Speedrunner "+player.getName()+" has been eliminated");
            ManhuntPlus.getInstance().removeSpeedrunner(player);
            if (ManhuntPlus.getInstance().getSpeedrunners().isEmpty()){
                Bukkit.broadcastMessage("§aHunter(s) have won the Manhunt!");
                ManhuntPlus.getInstance().stopTimer();
                ManhuntPlus.getInstance().setStatus(false);
            }
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
        } else if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            if (ManhuntPlus.getInstance().getSpeedrunners().contains(killer)){
                if (ManhuntPlus.getInstance().getStatus() && !(ManhuntPlus.getInstance().getSpeedrunners().isEmpty())){
                    Bukkit.broadcastMessage("§aSpeedrunner(s) have won the Manhunt!");
                    ManhuntPlus.getInstance().stopTimer();
                    ManhuntPlus.getInstance().setStatus(false);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (ManhuntPlus.getInstance().getTwist() != Twist.MILK_HUNTER_OP_LOOT) return;

        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player target)) return;
        if (!ManhuntPlus.getInstance().getHunters().contains(target)) return;
        if (!ManhuntPlus.getInstance().getSpeedrunners().contains(player)) return;

        if (player.getInventory().getItemInMainHand().getType() == Material.BUCKET) {
            LootPool pool = ManhuntPlus.getInstance().getDefaultLoot();

            for (int i = 0; i < 2 + new Random().nextInt(3); i++) {
                ItemStack loot = pool.getRandomLoot();
                if (loot != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), loot);
                }
            }
            
            player.getInventory().setItemInMainHand(new ItemStack(Material.MILK_BUCKET)); // milking
            event.setCancelled(true);
        }
    }

}

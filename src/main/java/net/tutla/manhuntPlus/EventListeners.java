package net.tutla.manhuntPlus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;
import java.util.UUID;

public class EventListeners implements Listener {
    private static TwistsHelper helper;

    public EventListeners(TwistsHelper helper) {
        this.helper = helper;
    }

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


            ItemStack milk = new ItemStack(Material.MILK_BUCKET);
            ItemMeta meta = milk.getItemMeta();

            NamespacedKey key = new NamespacedKey(ManhuntPlus.getInstance(), "milked_from");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, target.getUniqueId().toString());

            milk.setItemMeta(meta);
            player.getInventory().addItem(milk);

            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem.getAmount() > 1) {
                mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                player.getInventory().setItemInMainHand(mainHandItem);
            } else {
                player.getInventory().setItemInMainHand(null);
            }

            target.sendMessage("§aYou have been milked!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.getType() == Material.MILK_BUCKET) {
            player.sendMessage("weeb");
            ItemMeta meta = item.getItemMeta();
            NamespacedKey key = new NamespacedKey(ManhuntPlus.getInstance(), "milked_from");

            if (meta != null && meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                String uuid = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if (uuid != null) {
                    Player target = Bukkit.getPlayer(UUID.fromString(uuid));
                    if (target != null) {
                        helper.tortureHunter(target);
                    }
                }
            }
        }
    }





}

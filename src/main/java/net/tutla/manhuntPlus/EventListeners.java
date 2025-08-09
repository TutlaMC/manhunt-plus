package net.tutla.manhuntPlus;

import net.tutla.manhuntPlus.lootpool.LootPool;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static net.tutla.manhuntPlus.TwistsHelper.getCardinalDirection;

public class EventListeners implements Listener {
    private static TwistsHelper helper;

    public EventListeners(TwistsHelper helper) {
        this.helper = helper;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (ManhuntPlus.getInstance().getPlayingSpeedrunners().contains(player.getUniqueId())){
            Bukkit.broadcastMessage("Speedrunner "+player.getName()+" has been eliminated");
            ManhuntPlus.getInstance().removePlayingSpeedrunner(player);
            if (ManhuntPlus.getInstance().getPlayingSpeedrunners().isEmpty()){
                Bukkit.broadcastMessage("§aHunter(s) have won the Manhunt!");
                ManhuntPlus.getInstance().stopManhunt();
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PIG) {
            if (ManhuntPlus.getInstance().getTwist() != ManhuntPlus.Twist.PIG_OP_LOOT) return;

            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            if (!ManhuntPlus.getInstance().getPlayingSpeedrunners().contains(killer.getUniqueId())) return;

            event.getDrops().clear();
            LootPool pool = ManhuntPlus.getInstance().getDefaultLoot();

            for (int i = 0; i < 2 + new Random().nextInt(3); i++) {
                ItemStack drop = pool.getRandomLoot();
                if (drop != null) event.getDrops().add(drop);
            }
        } else if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            if (ManhuntPlus.getInstance().getStatus() && !(ManhuntPlus.getInstance().getPlayingSpeedrunners().isEmpty())){
                Bukkit.broadcastMessage("§aSpeedrunner(s) have won the Manhunt!");
                ManhuntPlus.getInstance().stopManhunt();
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (ManhuntPlus.getInstance().getTwist() != ManhuntPlus.Twist.SUSSY) return;

        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player target)) return;
        if (!ManhuntPlus.getInstance().getHunters().contains(target)) return;
        if (!ManhuntPlus.getInstance().getSpeedrunners().contains(player)) return;

        if (player.getInventory().getItemInMainHand().getType() == Material.BUCKET) {


            ItemStack milk = new ItemStack(Material.MILK_BUCKET);
            ItemMeta meta = milk.getItemMeta();

            NamespacedKey key = new NamespacedKey(ManhuntPlus.getInstance(), "milked_from");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, target.getUniqueId().toString());
            meta.setDisplayName("§b" + target.getName()+ "§e's Milk");
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

    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COMPASS) return;

        if (!(item.getItemMeta() instanceof CompassMeta meta)) return;

        String id = meta.getPersistentDataContainer().get(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING);
        if (id == null) return;

        UUID compassId = UUID.fromString(id);
        Player target = ManhuntPlus.getInstance().getTrackedCompasses().get(compassId);
        if (target == null || !target.isOnline()) {
            event.getPlayer().sendMessage("§cTarget not available.");
            return;
        }

        Location loc = target.getLocation();
        meta.setLodestone(loc);
        meta.setLodestoneTracked(false);
        item.setItemMeta(meta);

        event.getPlayer().sendMessage("§aCompass calibrated to " + target.getName());
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.getType() == Material.MILK_BUCKET) {
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

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player hitter = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        if (ManhuntPlus.getInstance().getSpeedrunners().contains(hitter.getUniqueId()) && ManhuntPlus.getInstance().getHunters().contains(victim.getUniqueId())){
            if (ManhuntPlus.getInstance().waitingForStart){
                ManhuntPlus.getInstance().startManhunt();
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) return;
        if (ManhuntPlus.getInstance().getTwist() != ManhuntPlus.Twist.SUSSY) return;

        Player croucher = event.getPlayer();
        if (!ManhuntPlus.getInstance().getPlayingSpeedrunners().contains(croucher.getUniqueId())) return;



        for (Player target : ManhuntPlus.getInstance().getPlayers(ManhuntPlus.getInstance().getHunters())) {
            if (!target.getWorld().equals(croucher.getWorld())) continue;
            if (croucher.getLocation().distance(target.getLocation()) > 0.9) continue;
            target.playSound(target.getLocation(), Sound.ENTITY_GHAST_HURT, 1f, 1f);
            Vector targetFacing = target.getLocation().getDirection().normalize();
            Vector toCroucher = croucher.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
            if (getCardinalDirection(croucher) == getCardinalDirection(target)) {
                ManhuntPlus.giveLootToLeveller(croucher);
            }

        }

    }

}

package net.tutla.manhuntPlus;

import net.tutla.manhuntPlus.lootpool.LootPool;
import net.tutla.manhuntPlus.manhunt.*;
import net.tutla.manhuntPlus.twist.TwistsHelper;
import net.tutla.manhuntPlus.util.TextUtil;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.player.PlayerPortalEvent;

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
        if (ManhuntContext.getPlayingSpeedrunners().contains(player.getUniqueId())){
            ManhuntContext.removePlayingSpeedrunner(player);
            if (ManhuntContext.getPlayingSpeedrunners().isEmpty()){
                Bukkit.broadcastMessage("§aHunter(s) have won the Manhunt!");
                Manhunt.stopManhunt();
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PIG) {
            if (Manhunt.getTwist() != Twist.PIG_OP_LOOT) return;

            Player killer = event.getEntity().getKiller();
            if (killer == null) return;

            if (!ManhuntContext.getPlayingSpeedrunners().contains(killer.getUniqueId())) return;

            event.getDrops().clear();
            LootPool pool = ManhuntPlus.getInstance().getDefaultLoot();

            for (int i = 0; i < 2 + new Random().nextInt(3); i++) {
                ItemStack drop = pool.getRandomLoot();
                if (drop != null) event.getDrops().add(drop);
            }
        } else if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            if (Manhunt.getStatus() && !(ManhuntContext.getPlayingSpeedrunners().isEmpty())){
                Bukkit.broadcastMessage("§aSpeedrunner(s) have won the Manhunt!");
                Manhunt.stopManhunt();
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (Manhunt.getTwist() != Twist.MILK_HUNTER_OP_LOOT) return;

        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player target)) return;
        if (!ManhuntContext.getPlayingSpeedrunners().contains(player.getUniqueId())) return;

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

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COMPASS) return;
        if (!(item.getItemMeta() instanceof CompassMeta meta)) return;

        System.out.println("rc");
        String id = meta.getPersistentDataContainer().get(ManhuntPlus.COMPASS_ID_KEY, PersistentDataType.STRING);
        if (id == null) return; // not a manhunt compass

        UUID compassId = UUID.fromString(id);
        Player target = ManhuntCompass.getTarget(compassId);

        if (target == null) {
            event.getPlayer().sendMessage(TextUtil.parse("<red>Tracked player is offline or no longer being tracked.</red>"));
            return;
        }

        if (!target.isOnline()) {
            event.getPlayer().sendMessage(TextUtil.parse("<yellow>" + target.getName() + " is offline.</yellow>"));
            return;
        }

        ManhuntCompass.updateCompass(item, compassId, event.getPlayer(), target);
        event.getPlayer().sendMessage(TextUtil.parse("<green>Compass calibrated to <white>" + target.getName() + "</white>.</green>"));
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
        if (ManhuntContext.getSpeedrunners().contains(hitter.getUniqueId()) && ManhuntContext.getHunters().contains(victim.getUniqueId())){
            if (Manhunt.getWaitingForStart()){
                Manhunt.startManhunt();
            }
        }
    }

    /* let git tell history */

    @EventHandler
    public void onPortalEnter(PlayerPortalEvent event){
        if (event.getTo().getWorld() == null) return;

        UUID p = event.getPlayer().getUniqueId();
        PortalInfo info = ManhuntCompass.getPlayerPortalInfo(p);

        if (info == null){
            info = new PortalInfo();
        }

        switch (event.getTo().getWorld().getEnvironment()){ // based on destination, get prev dimensions place
            case NETHER -> {
                info.overworldToNetherPortal = event.getFrom();
            }
            case NORMAL -> {
                if (event.getFrom().getWorld().getEnvironment() == World.Environment.NETHER){
                    info.netherToOverworldPortal = event.getFrom();
                } else {
                    info.endToOverworldPortal = event.getFrom();
                }
            }
            case THE_END -> {
                info.overworldToEndPortal = event.getFrom();
            }

        }
        ManhuntCompass.setPlayerPortalInfo(p, info);
    }
}

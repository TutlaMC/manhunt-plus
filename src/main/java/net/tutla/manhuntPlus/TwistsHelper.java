package net.tutla.manhuntPlus;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class TwistsHelper {
    private static final Random random = new Random();

    public void tortureHunter(Player hunter) {
        int choice = random.nextInt(9);
        switch (choice) {
            case 0 -> hunter.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 2 * 60 * 20, 4)); // Mining fatigue V
            case 1 -> hunter.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 60 * 20, 4));     // Blindness V
            case 2 -> hunter.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60 * 20, 0));       // Levitation 1 min
            case 3 -> hunter.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 4 * 60 * 20, 7));     // Weakness VIII
            case 4 -> hunter.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 4 * 60 * 20, 7));       // Hunger VIII
            case 5 -> createSinkhole(hunter);
            case 6 -> launchToSky(hunter);
            case 7 -> clearHalfInventory(hunter);
            case 8 -> hunter.setHealth(10);
        }
        hunter.getWorld().playSound(hunter.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
    }

    private void createSinkhole(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return;
        for (int y = loc.getBlockY(); y > loc.getBlockY() - world.getMinHeight() && y >= world.getMinHeight(); y--) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Location blockLoc = loc.clone().add(x, y - loc.getBlockY(), z);
                    world.getBlockAt(blockLoc).setType(Material.AIR);
                }
            }
        }
    }

    private void launchToSky(Player player) {
        player.setVelocity(new Vector(0, 10, 0)); //
    }

    private void clearHalfInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        List<Integer> nonEmptySlots = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].getType() != Material.AIR) {
                nonEmptySlots.add(i);
            }
        }

        Collections.shuffle(nonEmptySlots);
        int itemsToRemove = nonEmptySlots.size() / 2;
        for (int i = 0; i < itemsToRemove; i++) {
            int slot = nonEmptySlots.get(i);
            player.getInventory().setItem(slot, null);
        }

        player.sendMessage("§cHalf of your inventory has been cleared!");
    }
}

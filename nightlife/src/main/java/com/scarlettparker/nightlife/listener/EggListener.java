package com.scarlettparker.nightlife.listener;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.ItemMeta;

import static com.scarlettparker.nightlife.Plugin.*;

public class EggListener implements Listener {
  private static final String SPAWNER_TAG = "spawner_spawned";
  private final Plugin plugin;

  public EggListener(Plugin plugin) {
    this.plugin = plugin;
  }
  
  /**
   * Give entities a tag when spawned by a spawner so that it won't drop an egg if
   * killed by a player (applies only to custom crafted spawners).
   */
  @EventHandler
  public void onSpawnerSpawn(SpawnerSpawnEvent event) {
    if (SPAWNER_DROP_EGGS) {
      return;
    }
    
    Entity entity = event.getEntity();
    CreatureSpawner spawner = event.getSpawner();
    
    if (!(entity instanceof LivingEntity)) {
      return;
    }

    entity.getPersistentDataContainer().set(
      new NamespacedKey(plugin, SPAWNER_TAG), PersistentDataType.BYTE, (byte) 1
    );
  }

  /**
   * Drop spawn egg on entity death. Chance is set in config but default is 1%
   */
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    Entity entity = event.getEntity();
    EntityType entityType = entity.getType();
    
    // Check if entity was spawned by spawner
    if (entity.getPersistentDataContainer().has(
      new NamespacedKey(plugin, SPAWNER_TAG), PersistentDataType.BYTE
    )) {
      return;
    }
    
    if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
      if (damageEvent.getDamager() instanceof Player) {
        if (!(entity instanceof Mob) || entity.getCustomName() != null) {
          return;
        }
        if (Math.random() < MOB_EGG_CHANCE / 100.0) {
          Material spawnEggMaterial = getSpawnEggMaterial(entityType);
          if (spawnEggMaterial != null) {
            ItemStack egg = new ItemStack(spawnEggMaterial, 1);
            entity.getWorld().dropItemNaturally(entity.getLocation(), egg);
          }
        }
      }
    }
  }

  private Material getSpawnEggMaterial(EntityType entityType) {
    String eggName = entityType.name() + "_SPAWN_EGG";
    return Material.matchMaterial(eggName);
  }

  /**
   * Update the spawned entity type of a spawner when a player right clicks it with a spawn egg.
   * This only works for crafted spawners (though idk a player would otherwise get an empty spawner lol).
   */
  @EventHandler
  public void onSpawnerInteract(PlayerInteractEvent event) {
    Block clickedBlock = event.getClickedBlock();
    ItemStack item = event.getItem();

    if (clickedBlock == null || item == null) {
        return;
    }

    if (clickedBlock.getType() != Material.SPAWNER || !item.getType().toString().endsWith("_SPAWN_EGG")) {
        return;
    }

    CreatureSpawner spawner = (CreatureSpawner) clickedBlock.getState();

    // Get entity type from spawn egg
    EntityType entityType = EntityType.valueOf(
      item.getType().toString().replace("_SPAWN_EGG", "")
    );

    // Update spawner
    spawner.setSpawnedType(entityType);
    spawner.update();
    item.setAmount(item.getAmount() - 1);
    event.setCancelled(true);
  }
}
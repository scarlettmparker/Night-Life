package com.scarlettparker.nightlife.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.block.BlockState;

import static com.scarlettparker.nightlife.Plugin.*;

public class ExplosionListener implements Listener {

  /**
   * Make ender crystal explosions weaker.
   */
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
      Entity damager = event.getDamager();

      if (damager instanceof EnderCrystal) {
        event.setDamage(event.getDamage() * 0.08);
      } else if (damager instanceof Creeper) {
        // Increase damage from creepers
        event.setDamage(event.getDamage() * 1.1);
      }
    }
  }

  /**
   * Handle respawn anchor explosion event. This changes the default respawn anchor explosion to make it a bit weaker.
   */
  @EventHandler
  public void onRespawnAnchorInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    Block block = event.getClickedBlock();
    if (block == null || !(block.getBlockData() instanceof RespawnAnchor)) return;

    RespawnAnchor anchor = (RespawnAnchor) block.getBlockData();
    String worldName = block.getWorld().getName().toLowerCase();

    if (worldName.contains("nether")) {
      return;
    }

    if (anchor.getCharges() > 0) {
      handleExplosion(event, block, ANCHOR_EXPLOSION_RADIUS, ANCHOR_EXPLOSION_DAMPENING);
    }
  }


  /**
   * Handle bed explosion event. This changes the default bed explosion to make it a bit weaker.
   */
  @EventHandler
  public void onBedInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    Block block = event.getClickedBlock();
    if (block == null || !(block.getState() instanceof Bed)) return;

    String worldName = block.getWorld().getName().toLowerCase();

    if (worldName.contains("nether") || worldName.contains("the_end")) {
      handleExplosion(event, block, BED_EXPLOSION_RADIUS, BED_EXPLOSION_DAMPENING);
    }
  }

  /**
   * Create a custom explosion for beds and respawn anchors. It's based on the default
   * explosion (e.g. Creeper) but with a multiplier to make it weaker.
   * 
   * @param event The event that triggered the explosion
   * @param block The block that triggered the explosion
   * @param power The power of the explosion
   * @param multiplier The multiplier to weaken the explosion
   */
  private void handleExplosion(PlayerInteractEvent event, Block block, float power, float multiplier) {
    event.setCancelled(true);
    block.setType(org.bukkit.Material.AIR);
    block.getWorld().createExplosion(block.getLocation(), power, false, true, event.getPlayer());

    // Only affect players
    if (event.getPlayer() instanceof Player) {
      event.getPlayer().damage(event.getPlayer().getHealth() / multiplier);
    } else {
      event.getPlayer().damage(event.getPlayer().getHealth());
    }
    event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(-EXPLOSION_SPEED).setY(1));
  }
}
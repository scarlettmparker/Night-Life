package com.scarlettparker.nightlife.listener;

import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class FireworkListener implements Listener {

  /**
   * Prevent players from crafting firework stars.
   */
  @EventHandler
  public void onCraftItem(CraftItemEvent event) {
    if (event.getRecipe().getResult().getType() == Material.FIREWORK_STAR) {
      event.setCancelled(true);
    }
  }

  /**
   * Remove firework stars from player inventory when they are selected.
   */
  @EventHandler
  public void onPlayerItemHeld(PlayerItemHeldEvent event) {
    ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
    if (item != null && item.getType() == Material.FIREWORK_STAR) {
      event.getPlayer().getInventory().remove(item);
    }
  }

  /**
   * Remove firework stars from player inventory when they are clicked in the inventory.
   */
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    ItemStack item = event.getCurrentItem();
    if (item != null && item.getType() == Material.FIREWORK_STAR) {
      event.getWhoClicked().getInventory().remove(item);
    }
  }

  /**
   * Remove firework stars from player inventory when they are swapped to the offhand.
   */
  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
    ItemStack offHandItem = event.getOffHandItem();
    if (offHandItem != null && offHandItem.getType() == Material.FIREWORK_STAR) {
      event.setCancelled(true);
      event.getPlayer().getInventory().remove(offHandItem);
    }
  }

  /**
   * Remove firework stars from player inventory when they are used.
   */
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.getType() == Material.FIREWORK_STAR) {
      event.setCancelled(true);
      event.getPlayer().getInventory().remove(item);
    }
  }

  /**
   * Prevent fireworks from damaging entities.
   */
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Firework) {
      event.setCancelled(true);
    }
  }
}
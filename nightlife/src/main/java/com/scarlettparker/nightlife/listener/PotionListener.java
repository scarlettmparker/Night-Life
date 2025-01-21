package com.scarlettparker.nightlife.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

public class PotionListener implements Listener {
  /**
   * Check if an item is a level > 1 potion, splash potion, lingering potion, or tipped arrow.
   */
  private boolean isUpgradedPotion(ItemStack item) {
    if (item != null && (item.getType() == Material.POTION ||
        item.getType() == Material.TIPPED_ARROW ||
        item.getType() == Material.SPLASH_POTION ||
        item.getType() == Material.LINGERING_POTION)) {
      PotionMeta meta = (PotionMeta) item.getItemMeta();
      return meta != null && meta.getBasePotionData().isUpgraded();
    }
    return false;
  }

  /**
   * Downgrade a potion, splash potion, lingering potion, or tipped arrow to level 1.
   */
  private void downgradePotion(ItemStack item) {
    if (item != null && (item.getType() == Material.POTION ||
        item.getType() == Material.TIPPED_ARROW ||
        item.getType() == Material.SPLASH_POTION ||
        item.getType() == Material.LINGERING_POTION)) {
      PotionMeta meta = (PotionMeta) item.getItemMeta();
      if (meta != null) {
        PotionData potionData = new PotionData(meta.getBasePotionData().getType(), false, false);
        meta.setBasePotionData(potionData);
        item.setItemMeta(meta);
      }
    }
  }

  /**
   * Prevent > level 1 potions from being brewed.
   */
  @EventHandler
  public void onBrew(BrewEvent event) {
    for (ItemStack item : event.getContents()) {
      if (isUpgradedPotion(item)) {
        if (item.getType() == Material.POTION) {
          event.setCancelled(true);
          return;
        } else if (item.getType() == Material.TIPPED_ARROW) {
          downgradePotion(item);
        }
      }
    }
  }

  /**
   * Prevent players from holding level > 1 potions or tipped arrows.
   * Also, downgrade any potions or tipped arrows to level 1.
   */
  @EventHandler
  public void onPlayerItemHeld(PlayerItemHeldEvent event) {
    Player player = event.getPlayer();
    ItemStack item = player.getInventory().getItem(event.getNewSlot());
    if (isUpgradedPotion(item)) {
      downgradePotion(item);
    }
  }

  /**
   * Downgrade any level > 1 potions or tipped arrows when picked up.
   */
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    ItemStack item = event.getItem().getItemStack();
    if (isUpgradedPotion(item)) {
      downgradePotion(item);
    }
  }

  /**
   * Downgrade any level > 1 potions or tipped arrows when added to the inventory.
   */
  @EventHandler
  public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
    ItemStack item = event.getCurrentItem();
    if (isUpgradedPotion(item)) {
      downgradePotion(item);
    }
  }

  /**
   * Prevent > level 1 potions or tipped arrows from damaging entities.
   */
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof ThrownPotion) {
      ThrownPotion potion = (ThrownPotion) event.getDamager();
      ItemStack item = potion.getItem();
      if (isUpgradedPotion(item)) {
        event.setCancelled(true);
      }
    } else if (event.getDamager() instanceof Arrow) {
      Arrow arrow = (Arrow) event.getDamager();
      if (arrow.getBasePotionData() != null && arrow.getBasePotionData().isUpgraded()) {
        event.setCancelled(true);
      }
    }
  }

  /**
   * Prevent > level 1 potions from being consumed.
   */
  @EventHandler
  public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
    ItemStack item = event.getItem();
    if (isUpgradedPotion(item) && item.getType() == Material.POTION) {
      event.setCancelled(true);
      event.getPlayer().getInventory().remove(item);
    }
  }

  /**
   * Prevent > level 1 splash or lingering potions from being thrown.
   */
  @EventHandler
  public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
    if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR || 
        event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
      ItemStack item = event.getItem();
      if (isUpgradedPotion(item) && 
          (item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION)) {
        event.setCancelled(true);
        event.getPlayer().getInventory().remove(item);
      }
    }
  }

  /**
   * Prevent > level 1 splash or lingering potions from being dispensed.
   */
  @EventHandler
  public void onBlockDispense(BlockDispenseEvent event) {
    if (event.getBlock().getState() instanceof Dispenser) {
      ItemStack item = event.getItem();
      if (isUpgradedPotion(item) && 
          (item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION)) {
        event.setCancelled(true);
        Dispenser dispenser = (Dispenser) event.getBlock().getState();
        dispenser.getInventory().remove(item);
      }
    }
  }
}
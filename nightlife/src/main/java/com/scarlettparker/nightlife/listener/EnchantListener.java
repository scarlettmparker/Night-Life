package com.scarlettparker.nightlife.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;

public class EnchantListener implements Listener {

  /**
   * Prevent > level 1 enchantments from appearing in the enchanting table.
   */
  @EventHandler
  public void onPrepareEnchant(PrepareItemEnchantEvent event) {
    if (event.getItem().getType() == Material.BOOK) {
      event.setCancelled(true);
      return;
    }

    EnchantmentOffer[] offers = event.getOffers();
    if (offers != null) {
      for (EnchantmentOffer offer : offers) {
        if (offer != null)
          offer.setEnchantmentLevel(1);
      }
    }
  }

  /**
   * Prevent > level 1 enchantments from being added to items.
   */
  @EventHandler
  public void onEnchant(EnchantItemEvent event) {
    if (event.getItem().getType() == Material.BOOK) {
      event.setCancelled(true);
      return;
    }

    Map<Enchantment, Integer> enchants = event.getEnchantsToAdd();
    Map<Enchantment, Integer> newEnchants = new HashMap<>();
    
    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
      newEnchants.put(entry.getKey(), 1);
    
    enchants.clear();
    enchants.putAll(newEnchants);
    event.setExpLevelCost(1);
  }

  /**
   * Prevent > level 1 enchantments from being added to items using anvils.
   */
  @EventHandler
  public void onAnvilPrepare(PrepareAnvilEvent event) {
    ItemStack result = event.getResult();
    if (result == null) return;

    if (result.getType() == Material.ENCHANTED_BOOK) {
      result.setType(Material.BOOK);
      event.setResult(result);
      return;
    }

    if (result.getEnchantments().size() > 0)
      nerfEnchants(result);
  }

  /**
   * Remove enchantments of level > 1 from items when picked up.
   */
  @EventHandler
  public void onItemPickup(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player)
      removeEnchants(event.getItem().getItemStack());
  }

  /**
   * Remove enchantments of level > 1 from items when switching hotbar items.
   */
  @EventHandler
  public void onHotbarSwitch(PlayerItemHeldEvent event) {
    Player player = event.getPlayer();
    ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
    
    if (newItem != null && newItem.getType() != Material.AIR)
      removeEnchants(newItem);
  }

  /**
   * Remove enchantments of level > 1 when equiping items (e.g. armour, weapons).
   */
  @EventHandler
  public void onEquipmentChange(EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player)) return;
    Player player = (Player) event.getEntity();

    ItemStack mainHand = player.getInventory().getItemInMainHand();
    if (mainHand != null && mainHand.getType() != Material.AIR)
      removeEnchants(mainHand);
    
    ItemStack offHand = player.getInventory().getItemInOffHand();
    if (offHand != null && offHand.getType() != Material.AIR)
      removeEnchants(offHand);
  }

  /**
   * Remove enchantments of level > 1 from items when right-clicking anything.
   */
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    ItemStack item = event.getItem();

    if (item != null)
      removeEnchants(item);
  }

  /**
   * Remove enchantments of level > 1 from items when clicking on it in an inventory.
   */
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    ItemStack current = event.getCurrentItem();
    ItemStack cursor = event.getCursor();
    
    if (current != null)
      removeEnchants(current);
    if (cursor != null)
      removeEnchants(cursor);
  }

  /**
   * Helper function to remove enchantments of level > 1 from items.
   * @param item Item to remove enchantments from.
   */
  private void removeEnchants(ItemStack item) {
    if (item == null) return;

    if (item.getType() == Material.ENCHANTED_BOOK) {
      removeBookEnchants(item);
      return;
    }
    if (item.getEnchantments().size() > 0)
      nerfEnchants(item);
  }

  /**
   * Helper function to remove enchantments of level > 1 from enchanted books.
   * @param book Book to remove enchantments from.
   */
  private void removeBookEnchants(ItemStack book) {
    if (book.getType() == Material.ENCHANTED_BOOK) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
      if (meta != null) {
        for (Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
          meta.removeStoredEnchant(entry.getKey());
        }
        book.setItemMeta(meta);
      }
      book.setType(Material.BOOK);
    }
  }

  /**
   * Helper function to nerf enchantments of level > 1 to level 1.
   * @param item Item to nerf enchantments.
   */
  private void nerfEnchants(ItemStack item) {
    Map<Enchantment, Integer> enchants = item.getEnchantments();
    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
      if (entry.getValue() > 1) {
        item.removeEnchantment(entry.getKey());
        item.addEnchantment(entry.getKey(), 1);
      }
    }
  }
}
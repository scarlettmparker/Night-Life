package com.scarlettparker.nightlife.custom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class CustomRecipe {
  private final Plugin plugin;

  public CustomRecipe(Plugin plugin) {
    this.plugin = plugin;
    saddleRecipe();
    nametagRecipe();
    tntRecipe();
    spawnerRecipe();
    elytraRecipe();
  }

  /**
   * Create a shaped recipe. This refers to a recipe that must be created a certain way.
   * 
   * @param material The material to create the recipe for.
   * @param namespacedKey The namespaced key for the recipe.
   * @return The shaped recipe.
   */
  private ShapedRecipe createShapedRecipe(Material material, String namespacedKey) {
    ItemStack item = new ItemStack(material);
    NamespacedKey key = new NamespacedKey(plugin, namespacedKey);
    return new ShapedRecipe(key, item);
  }

  /**
   * Create a shapeless recipe. This refers to a recipe that can be crafted in any order.
   * E.g. flint and steel can be created in whatever order of iron and flint
   * 
   * @param material The material to create the recipe for.
   * @param namespacedKey The namespaced key for the recipe.
   * @return The shapeless recipe.
   */
  private ShapelessRecipe createShapelessRecipe(Material material, String namespacedKey) {
    ItemStack item = new ItemStack(material);
    NamespacedKey key = new NamespacedKey(plugin, namespacedKey);
    return new ShapelessRecipe(key, item);
  }

  private void saddleRecipe() {
    ShapedRecipe recipe = createShapedRecipe(Material.SADDLE, "saddle");
    recipe.shape(
            "LLL",
            "LTL",
            "   ");
    recipe.setIngredient('L', Material.LEATHER);
    recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
    Bukkit.addRecipe(recipe);
  }

  private void nametagRecipe() {
    ShapelessRecipe recipe = createShapelessRecipe(Material.NAME_TAG, "nametag");
    recipe.addIngredient(1, Material.IRON_BARS);
    recipe.addIngredient(1, Material.PAPER);
    recipe.addIngredient(1, Material.STRING);
    Bukkit.addRecipe(recipe);
  }

  private void tntRecipe() {
    ShapelessRecipe recipe = createShapelessRecipe(Material.TNT, "tnt");
    recipe.addIngredient(1, Material.GUNPOWDER);
    recipe.addIngredient(1, Material.PAPER);
    recipe.addIngredient(1, Material.STRING);
    Bukkit.addRecipe(recipe);
  }

  private void spawnerRecipe() {
    ShapedRecipe recipe = createShapedRecipe(Material.SPAWNER, "spawner");
    recipe.shape(
            "III",
            "ISI",
            "III");
    recipe.setIngredient('I', Material.IRON_BARS);
    recipe.setIngredient('S', Material.DIAMOND);
    Bukkit.addRecipe(recipe);
  }

  private void elytraRecipe() {
    ShapedRecipe recipe = createShapedRecipe(Material.ELYTRA, "elytra");
    recipe.shape(
        "BLB",
        "LCL",
        "D D"
    );
    recipe.setIngredient('B', Material.BLAZE_ROD);
    recipe.setIngredient('L', Material.END_STONE);
    recipe.setIngredient('C', Material.NETHERITE_CHESTPLATE);
    recipe.setIngredient('D', Material.DIAMOND);
    Bukkit.addRecipe(recipe);
  }
}
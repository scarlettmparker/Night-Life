package com.scarlettparker.nightlife;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

// commands
import com.scarlettparker.nightlife.commands.Help;
import com.scarlettparker.nightlife.commands.PluginList;
import com.scarlettparker.nightlife.commands.Tell;
import com.scarlettparker.nightlife.life.commands.Boogey;
import com.scarlettparker.nightlife.life.commands.Cure;
import com.scarlettparker.nightlife.life.commands.Punish;
import com.scarlettparker.nightlife.life.commands.Lives;
import com.scarlettparker.nightlife.life.commands.SetLife;
import com.scarlettparker.nightlife.life.commands.StartLife;

// custom
import com.scarlettparker.nightlife.custom.CustomRecipe;

// listener
import com.scarlettparker.nightlife.listener.CommandListener;
import com.scarlettparker.nightlife.listener.ProtectionListener;
import com.scarlettparker.nightlife.listener.EnchantListener;
import com.scarlettparker.nightlife.listener.PotionListener;
import com.scarlettparker.nightlife.listener.ExplosionListener;
import com.scarlettparker.nightlife.listener.FireworkListener;

import com.scarlettparker.nightlife.listener.EggListener;
import com.scarlettparker.nightlife.life.listener.NightListener;

/*
 * nightlife java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER = Logger.getLogger("nightlife");

  /**
   * Life config values
   */
  public static int STARTING_LIVES;
  public static int MAX_LIVES;
  public static boolean BOOGEY_TRANSFER_NIGHT;
  public static boolean KILL_LIFE_INCREMENT;
  public static int KILL_LIFE_UPPER_THRESHOLD;
  public static int KILL_LIFE_LOWER_THRESHOLD;

  /**
   * Explosion config values
   */
  public static float EXPLOSION_SPEED;
  public static float BED_EXPLOSION_RADIUS;
  public static float BED_EXPLOSION_DAMPENING;
  public static float ANCHOR_EXPLOSION_RADIUS;
  public static float ANCHOR_EXPLOSION_DAMPENING;

  /**
   * Egg config values
   */
  public static float MOB_EGG_CHANCE;
  public static boolean SPAWNER_DROP_EGGS;

  private NightListener nightListener;
  private EggListener eggListener;

  @Override
  public void onEnable()
  {
    LOGGER.info("Night Life enabled!");
    saveDefaultConfig();

    try {
      /**
       * Life config values
       */
      STARTING_LIVES = getConfig().getInt("life.starting_lives");
      MAX_LIVES = getConfig().getInt("life.max_lives");
      BOOGEY_TRANSFER_NIGHT = getConfig().getBoolean("life.boogey_transfer_night");
      KILL_LIFE_INCREMENT = getConfig().getBoolean("life.kill_life_increment");
      KILL_LIFE_UPPER_THRESHOLD = getConfig().getInt("life.kill_life_upper_threshold");
      KILL_LIFE_LOWER_THRESHOLD = getConfig().getInt("life.kill_life_lower_threshold");

      /**
       * Explosion config values
       */
      EXPLOSION_SPEED = (float) getConfig().getDouble("explosion.explosion_speed");
      BED_EXPLOSION_RADIUS = (float) getConfig().getDouble("explosion.bed_explosion_radius");
      BED_EXPLOSION_DAMPENING = (float) getConfig().getDouble("explosion.bed_explosion_dampening");
      ANCHOR_EXPLOSION_RADIUS = (float) getConfig().getDouble("explosion.anchor_explosion_radius");
      ANCHOR_EXPLOSION_DAMPENING = (float) getConfig().getDouble("explosion.anchor_explosion_dampening");

      /**
       * Egg config values
       */
      MOB_EGG_CHANCE = (float) getConfig().getDouble("egg.mob_egg_chance");
      SPAWNER_DROP_EGGS = getConfig().getBoolean("egg.spawner_drop_eggs");
    } catch (Exception e) {
      LOGGER.severe("Error reading config file. Disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    if (STARTING_LIVES < 1 || 
        MAX_LIVES < 1 ||
        KILL_LIFE_UPPER_THRESHOLD < 0 ||
        KILL_LIFE_LOWER_THRESHOLD < 0 ||
        EXPLOSION_SPEED < 0 ||
        BED_EXPLOSION_RADIUS < 0 ||
        BED_EXPLOSION_DAMPENING < 0 ||
        ANCHOR_EXPLOSION_RADIUS < 0 ||
        ANCHOR_EXPLOSION_DAMPENING < 0 ||
        MOB_EGG_CHANCE < 0 ||
        MOB_EGG_CHANCE > 100
    ) {
      LOGGER.severe("Invalid config values. Disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    // commands
    getCommand("help").setExecutor(new Help());
    getCommand("plugins").setExecutor(new PluginList());
    getCommand("tell").setExecutor(new Tell());
    getCommand("boogey").setExecutor(new Boogey());
    getCommand("cure").setExecutor(new Cure());
    getCommand("punish").setExecutor(new Punish());
    getCommand("lives").setExecutor(new Lives());
    getCommand("startlife").setExecutor(new StartLife());
    getCommand("setlife").setExecutor(new SetLife());

    // listeners
    getServer().getPluginManager().registerEvents(new CommandListener(), this);
    getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
    getServer().getPluginManager().registerEvents(new EnchantListener(), this);
    getServer().getPluginManager().registerEvents(new PotionListener(), this);
    getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
    getServer().getPluginManager().registerEvents(new FireworkListener(), this);

    nightListener = new NightListener(this);
    getServer().getPluginManager().registerEvents(nightListener, this);
    eggListener = new EggListener(this);
    getServer().getPluginManager().registerEvents(eggListener, this);

    // add recipes
    new CustomRecipe(this);
  }

  @Override
  public void onDisable()
  {
    LOGGER.info("Night Life disabled!");
    if (nightListener != null)
      nightListener.cleanup();
  }

  private void createRecipes() {

  }
}

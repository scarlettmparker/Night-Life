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
import com.scarlettparker.nightlife.life.commands.SetLife;
import com.scarlettparker.nightlife.life.commands.StartLife;

// listener
import com.scarlettparker.nightlife.listener.CommandListener;
import com.scarlettparker.nightlife.listener.ProtectionListener;
import com.scarlettparker.nightlife.listener.EnchantListener;
import com.scarlettparker.nightlife.life.listener.NightListener;

/*
 * nightlife java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER = Logger.getLogger("nightlife");

  public static int STARTING_LIVES;
  public static int MAX_LIVES;
  public static boolean KILL_LIFE_INCREMENT;

  private NightListener nightListener;

  @Override
  public void onEnable()
  {
    LOGGER.info("Night Life enabled!");
    saveDefaultConfig();

    try {
      STARTING_LIVES = getConfig().getInt("starting_lives");
      MAX_LIVES = getConfig().getInt("max_lives");
      KILL_LIFE_INCREMENT = getConfig().getBoolean("kill_life_increment");
    } catch (Exception e) {
      LOGGER.severe("Error reading config file. Disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    // commands
    getCommand("help").setExecutor(new Help());
    getCommand("plugins").setExecutor(new PluginList());
    getCommand("tell").setExecutor(new Tell());
    getCommand("boogey").setExecutor(new Boogey());
    getCommand("cure").setExecutor(new Cure());
    getCommand("startlife").setExecutor(new StartLife());
    getCommand("setlife").setExecutor(new SetLife());

    // listeners
    getServer().getPluginManager().registerEvents(new CommandListener(), this);
    getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
    getServer().getPluginManager().registerEvents(new EnchantListener(), this);

    nightListener = new NightListener(this);
    getServer().getPluginManager().registerEvents(nightListener, this);
  }

  @Override
  public void onDisable()
  {
    LOGGER.info("Night Life disabled!");
    if (nightListener != null)
      nightListener.cleanup();
  }
}

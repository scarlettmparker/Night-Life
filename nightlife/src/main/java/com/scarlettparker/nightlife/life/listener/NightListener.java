package com.scarlettparker.nightlife.life.listener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import org.bukkit.entity.Firework;
import org.bukkit.Location;
import org.bukkit.FireworkEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.Color;

import java.util.Objects;
import java.util.UUID;
import java.time.Instant;

import com.scarlettparker.nightlife.life.object.Death;
import com.scarlettparker.nightlife.life.object.TPlayer;
import com.scarlettparker.nightlife.life.utils.InstantFirework;
import com.scarlettparker.nightlife.life.utils.NightUtils;

import static com.scarlettparker.nightlife.life.commands.StartLife.*;
import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;
import static com.scarlettparker.nightlife.life.utils.WorldUtils.*;
import static com.scarlettparker.nightlife.Plugin.*;

public class NightListener implements Listener {
  private static final long NIGHT_START = 12800;
  private static final long NIGHT_END = 23599;
  private static final long DAY_START = 0;
  private static final long DAY_END = 12500;

  private static final long CHECK_FREQUENCY = 20L * 10;
  
  private final Plugin plugin;
  private final NightUtils nightUtils;
  private BukkitTask timeCheckTask;

  public NightListener(Plugin plugin) {
    this.plugin = plugin;
    this.nightUtils = new NightUtils();
    startTimeChecker();
  }

  /**
   * Start the time checker task.
   * This task will check the time of the overworld every 10 seconds.
   * If it is night time, it will handle the night time events.
   * If it is day time, it will handle the day time events.
   */
  private void startTimeChecker() {
    timeCheckTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      for (World world : Bukkit.getWorlds()) {
        if (world.getEnvironment() != Environment.NORMAL) {
          continue;
        }

        long time = world.getTime() % 24000;
        boolean isNightTime = time >= NIGHT_START && time <= NIGHT_END;
        boolean isDayTime = time >= DAY_START && time <= DAY_END;

        // transition into night
        if (isNightTime && !nightUtils.getNightTime()) {
          Bukkit.broadcastMessage("§9The sun is setting...");
          nightUtils.hidePlayerLives();

          nightUtils.setNightTime(true);
          nightUtils.setDayTime(false);
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule announceAdvancements false");
        }

        // transition into day
        if (isDayTime && !nightUtils.getDayTime()) {
          Bukkit.broadcastMessage("§eThe sun is rising...");
          nightUtils.showPlayerLives();

          nightUtils.setNightTime(false);
          nightUtils.setDayTime(true);
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule announceAdvancements true");
        }
      }
    }, 0L, CHECK_FREQUENCY);
  }

  /**
   * Cleanup the time check task.
   */
  public void cleanup() {
    if (timeCheckTask != null) {
      timeCheckTask.cancel();
      timeCheckTask = null;
    }
  }

  /**
   * Handle player join event. This will either create a new player or update the player's lives display.
   * It will also hide the player join message during night time.
   */
  @EventHandler
  public void playerJoinEvent(PlayerJoinEvent event) {
    UUID playerUUID = event.getPlayer().getUniqueId();

    if (playerFile.exists() && !playerExists(playerUUID)) {
      createPlayer(event.getPlayer());
      if (nightUtils.getNightTime()) {
        hidePlayerLives(event.getPlayer());
      }
    } else {
      TPlayer tempPlayer = new TPlayer(playerUUID);
      int lives = tempPlayer.getLives();

      if (!nightUtils.getNightTime()) {
        setPlayerName(event.getPlayer(), lives);
      }
      else {
        hidePlayerLives(event.getPlayer());
      }
    }

    if (nightUtils.getNightTime())
      event.setJoinMessage(null);
  }

  /**
   * Handle player quit event. This will hide the player leave message during night time.
   */
  @EventHandler
  public void playerQuitEvent(PlayerQuitEvent event) {
    if (nightUtils.getNightTime())
      event.setQuitMessage(null);
  }

  /**
   * Handle player death event. This will handle the player's death, decrement lives, and handle final death.
   */
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player playerEntity = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
    UUID playerUUID = playerEntity.getUniqueId();

    if (playerFile.exists() && playerExists(playerUUID)) {
      long unixTime = Instant.now().getEpochSecond();
      Death death = new Death(unixTime, event.getDeathMessage());

      TPlayer tempPlayer = new TPlayer(playerUUID);
      Death[] deaths = tempPlayer.getDeaths();
      Death[] newDeaths = new Death[deaths.length + 1];

      System.arraycopy(deaths, 0, newDeaths, 0, deaths.length);
      newDeaths[deaths.length] = death;
      tempPlayer.setDeaths(newDeaths);

      int lives = tempPlayer.getLives();
      if (lives > 1) {
        lives--;
      } else {
        lives = 0;
        handleFinalDeath(playerEntity.getName());
      }

      tempPlayer.setLives(lives);
      if (!nightUtils.getNightTime()) {
        setPlayerName(playerEntity, lives);
      } else {
        hidePlayerLives(playerEntity);
      }
    }

    if (!nightUtils.getNightTime()) {
      Location pLoc = playerEntity.getLocation();
      FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(false).trail(true)
        .with(FireworkEffect.Type.BALL).withColor(Color.WHITE).withFade(Color.GRAY).build();
      new InstantFirework(fireworkEffect, pLoc, "deathfirework");
    }

    // boogey man checking
    if (!(event.getEntity() instanceof Player)) {
      return;
    }

    Player victim = (Player) event.getEntity();
    Player killer = victim.getKiller();

    UUID victimUUID = victim.getUniqueId();
    UUID killerUUID = killer != null ? killer.getUniqueId() : null;
        
    // Self kills are not counted
    if (killer != null && killer != victim) {
      TPlayer tempPlayer = new TPlayer(killerUUID);
      TPlayer tempVictim = new TPlayer(victimUUID);

      if (tempPlayer.getBoogeyMan()) {
        tempPlayer.setBoogeyMan(false);
        killer.sendTitle("§aYou have been cured!", "", 10, 70, 20);
        killer.playSound(killer.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
        if (nightUtils.getNightTime() && BOOGEY_TRANSFER_NIGHT) {
          tempVictim.setBoogeyMan(true);
          victim.sendTitle("§cYou are now the Boogeyman!", "", 10, 70, 20);
          victim.playSound(victim.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        }
      }

      // Increment killer's lives if victim has more than 4 lives (Default)
      // and killer has less or equal to 2 lives (Default).
      // Boogey kills are not counted.
      if (KILL_LIFE_INCREMENT && tempVictim.getLives() >= KILL_LIFE_UPPER_THRESHOLD &&
          !tempPlayer.getBoogeyMan() && tempPlayer.getLives() <= KILL_LIFE_LOWER_THRESHOLD) {
        int lives = tempPlayer.getLives() + 1;
        if (lives > MAX_LIVES) {
          lives = MAX_LIVES;
        }
        tempPlayer.setLives(lives);
        Player player = Bukkit.getPlayer(killer.getName());
        if (!nightUtils.getNightTime()) {
          setPlayerName(player, lives);
        } else {
          hidePlayerLives(playerEntity);
        }
      }
    }

    if (nightUtils.getNightTime()) {
      event.setDeathMessage(null);
    }
  }

  /**
   * Handle player bed enter event. This will prevent players from sleeping during night time.
   */
  @EventHandler
  public void onSleepEvent(PlayerBedEnterEvent event) {
    if (nightUtils.getNightTime()) {
      event.setCancelled(true);
      event.getPlayer().sendMessage("§cYou cannot sleep at night.");
    }
  }

  /**
   * Handle player damage event during a totem usage. This prevents the usage of totems.
   */
  @EventHandler
  public void playerDamageEvent(EntityResurrectEvent event) {
    if (event.getEntity() instanceof Player)
      event.setCancelled(true);
  }

  /**
   * Handle entity damage event. This will prevent the damage caused by death fireworks.
   */
  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Firework) {
      Firework firework = (Firework) event.getDamager();
      String metaData = firework.getPersistentDataContainer().get(new NamespacedKey("nightlife", "fireworkmeta"), PersistentDataType.STRING);
      if (metaData != null && metaData.equals("deathfirework"))
        event.setCancelled(true);
    }
  }

  /**
   * Handle entity death event. This will prevent the death message from being displayed during night time.
   */
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (nightUtils.getNightTime() && event.getEntity() instanceof org.bukkit.entity.Player) {
      ((PlayerDeathEvent) event).setDeathMessage(null);
    }
  }

  /**
   * Handle player chat event. This will prevent players from chatting during night time.
   */
  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    if (nightUtils.getNightTime()) {
      event.setCancelled(true);
      Player player = event.getPlayer();
      player.sendMessage("§cYou cannot talk at night.");
    }
  }
}
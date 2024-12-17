package com.scarlettparker.nightlife.life.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.Bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WorldUtils {
  static Plugin plugin = Bukkit.getPluginManager().getPlugin("nightlife");

  /**
   * Helper function to get all players on the server, and shuffle their
   * order - used for selecting who will be the boogeyman.
   */
  public static List<Player> getAllPlayers() {
    List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
    Collections.shuffle(players);
    return players;
  }

  /**
   * Set player's name to display lives.
   * This uses the nametag plugin to display the name above the player's head as well.
   * @param p Player to change name.
   * @param lives Number of lives the player has.
   */
  public static void setPlayerName(Player p, int lives) {
    String newName, command;
    ChatColor lifeColor;
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    if (lives >= 1) {
      lifeColor = ChatColor.DARK_GREEN;
      if (lives == 1) {
        lifeColor = ChatColor.RED;
      } else if (lives == 2) {
        lifeColor = ChatColor.YELLOW;
      } else if (lives == 3) {
        lifeColor = ChatColor.GREEN;
      }
    } else {
      lifeColor = ChatColor.GRAY;
    }

    setNameColor(p, lifeColor);
    p.setDisplayName(lifeColor + p.getName() + ChatColor.RESET);
  }

  /**
   * Hides the player's lives from being displayed.
   * This is used during night time.
   * @param p Player to change name.
   */
  public static void hidePlayerLives(Player p) {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    ChatColor lifeColor = ChatColor.GRAY;
    setNameColor(p, lifeColor);
    p.setDisplayName(lifeColor + p.getName() + ChatColor.RESET);
  }

  /**
   * Handle final death of player.
   * This will clear the player's inventory, drop all items, play lightning effect, and set player to spectator mode.
   * @param name Player name to handle final death for.
   */
  public static void handleFinalDeath(String name) {
    Player tempPlayer = Bukkit.getPlayer(name);
    Location location = tempPlayer.getLocation();
    World world = location.getWorld();
    Inventory inventory = tempPlayer.getInventory();

    for (ItemStack is : inventory) {
      if (is != null && is.getType() != Material.AIR) {
        world.dropItemNaturally(location, is);
      }
    }

    // clear inventory and play lightning effect for everyone
    tempPlayer.getInventory().clear();
    world.strikeLightningEffect(location).setSilent(true);
    for (Player p : Bukkit.getOnlinePlayers()) {
      Location pLoc = p.getLocation();
      p.playSound(pLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
    }

    tempPlayer.setGameMode(GameMode.SPECTATOR);
    tempPlayer.sendTitle("§cYou have run out of lives", "", 10, 70, 20);
  }
  
  /**
   * Helper function to set a player's name color. It does this by
   * creating a new team for the player and setting the color of the team.
   * @param p Player to change name.
   * @param color Color to set the player's name to.
   */
  private static void setNameColor(Player p, ChatColor color) {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard = manager.getMainScoreboard();
    if (scoreboard == null) {
        scoreboard = manager.getNewScoreboard();
    }

    Team team = scoreboard.getTeam("team_" + p.getName());
    if (team == null) {
        team = scoreboard.registerNewTeam("team_" + p.getName());
    }
    
    team.setColor(color);
    team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    team.addEntry(p.getName());
    p.setScoreboard(scoreboard);
  }
}
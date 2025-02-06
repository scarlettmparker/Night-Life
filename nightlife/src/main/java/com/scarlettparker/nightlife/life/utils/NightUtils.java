package com.scarlettparker.nightlife.life.utils;

import java.util.List;
import org.bukkit.entity.Player;
import com.scarlettparker.nightlife.life.object.TPlayer;
import com.scarlettparker.nightlife.life.utils.WorldUtils;
import java.util.UUID;

public class NightUtils {
  public static boolean isNightTime = false;
  public static boolean isDayTime = false;
  
  public static void hidePlayerLives() {
    List<Player> players = WorldUtils.getAllPlayers();
    for (Player p : players) {
      WorldUtils.hidePlayerLives(p);
    }
  }

  public static void showPlayerLives() {
    List<Player> players = WorldUtils.getAllPlayers();
    for (Player p : players) {
      UUID playerUUID = p.getUniqueId();
      TPlayer tempPlayer = new TPlayer(playerUUID);
      int lives = tempPlayer.getLives();

      if (lives > 0) {
        WorldUtils.setPlayerName(p, lives);
      }
    }
  }

  public static boolean getNightTime() {
    return isNightTime;
  }

  public static boolean getDayTime() {
    return isDayTime;
  }

  public static void setNightTime(boolean nightTime) {
    isNightTime = nightTime;
  }

  public static void setDayTime(boolean dayTime) {
    isDayTime = dayTime;
  }
}

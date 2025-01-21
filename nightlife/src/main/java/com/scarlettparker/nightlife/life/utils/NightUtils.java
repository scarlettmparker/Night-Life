package com.scarlettparker.nightlife.life.utils;

import java.util.List;
import org.bukkit.entity.Player;
import com.scarlettparker.nightlife.life.object.TPlayer;
import com.scarlettparker.nightlife.life.utils.WorldUtils;
import java.util.UUID;

public class NightUtils {
  private boolean isNightTime = false;
  private boolean isDayTime = false;
  
  public void hidePlayerLives() {
    List<Player> players = WorldUtils.getAllPlayers();
    for (Player p : players) {
      WorldUtils.hidePlayerLives(p);
    }
  }

  public void showPlayerLives() {
    List<Player> players = WorldUtils.getAllPlayers();
    for (Player p : players) {
      UUID playerUUID = p.getUniqueId();
      TPlayer tempPlayer = new TPlayer(playerUUID);
      int lives = tempPlayer.getLives();

      if (lives > 0)
        WorldUtils.setPlayerName(p, lives);
    }
  }

  public boolean getNightTime() {
    return isNightTime;
  }

  public boolean getDayTime() {
    return isDayTime;
  }

  public void setNightTime(boolean isNightTime) {
    this.isNightTime = isNightTime;
  }

  public void setDayTime(boolean isDayTime) {
    this.isDayTime = isDayTime;
  }

}
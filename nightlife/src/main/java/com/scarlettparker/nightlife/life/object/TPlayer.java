package com.scarlettparker.nightlife.life.object;

import org.bukkit.Bukkit;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.text.SimpleDateFormat;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;
import com.scarlettparker.nightlife.life.utils.WorldUtils;
import com.scarlettparker.nightlife.life.utils.NightUtils;

public class TPlayer {
  private UUID uuid;

  public TPlayer(UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public int getLives() {
    Object lives = getJSONObjectAttribute(playerFile, uuid.toString(), "lives");
    if (lives instanceof Number) {
      return ((Number) lives).intValue();
    }
    return 0;
  }

  public void setLives(int lives) {
    setJSONObjectAttribute(playerFile, uuid.toString(), "lives", lives);
    Player player = Bukkit.getPlayer(uuid);
    if (!Objects.equals(uuid.toString(), "CONSOLE")) {
      if (NightUtils.getNightTime()) {
        WorldUtils.hidePlayerLives(player);
      } else {
        WorldUtils.setPlayerName(player, lives);
      }
    }
  }

  public Death[] getDeaths() {
    Object deaths = getJSONObjectAttribute(playerFile, uuid.toString(), "deaths");
    if (deaths instanceof String) {
      try {
        Gson gson = new GsonBuilder().create();
        String[] deathStrings = gson.fromJson((String)deaths, String[].class);
        
        return Arrays.stream(deathStrings)
          .map(str -> {
            String[] parts = str.split(" - ", 2);
            long timestamp = Long.parseLong(parts[0]);
            String cause = parts[1];
            return new Death(timestamp, cause);
          })
          .toArray(Death[]::new);
      } catch (Exception e) {
        e.printStackTrace();
        return new Death[0];
      }
    }
    return new Death[0];
  }
  
  private String formatDeath(Death death) {
    return death.getTime() + " - " + death.getCause();
  }
  
  public void setDeaths(Death[] deaths) {
    try {
      String[] formattedDeaths = Arrays.stream(deaths)
        .map(this::formatDeath)
        .toArray(String[]::new);
      
      Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
      String deathsJson = gson.toJson(formattedDeaths);
      setJSONObjectAttribute(playerFile, uuid.toString(), "deaths", deathsJson);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addDeath(Death death) {
    Death[] currentDeaths = getDeaths();
    Death[] newDeaths = Arrays.copyOf(currentDeaths, currentDeaths.length + 1);
    newDeaths[currentDeaths.length] = death;
    setDeaths(newDeaths);
  }

  public boolean getBoogeyMan() {
    Object boogeyMan = getJSONObjectAttribute(playerFile, uuid.toString(), "boogeyman");
    return boogeyMan instanceof Boolean && (boolean) boogeyMan;
  }

  public void setBoogeyMan(boolean boogeyMan) {
    setJSONObjectAttribute(playerFile, uuid.toString(), "boogeyman", boogeyMan);
  }
}
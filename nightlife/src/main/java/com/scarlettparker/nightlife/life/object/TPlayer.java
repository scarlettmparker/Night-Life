package com.scarlettparker.nightlife.life.object;

import org.bukkit.Bukkit;
import java.util.Arrays;
import java.util.Objects;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;
import static com.scarlettparker.nightlife.life.utils.WorldUtils.*;

public class TPlayer {
  private String name;

  public TPlayer(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLives() {
    Object lives = getJSONObjectAttribute(playerFile, name, "lives");
    if (lives instanceof Number) {
        return ((Number) lives).intValue();
    }
    return 0;
  }

  public void setLives(int lives) {
    setJSONObjectAttribute(playerFile, name, "lives", lives);
    if (!Objects.equals(name, "CONSOLE")) {
      setPlayerName(Bukkit.getPlayer(name), lives);
    }
  }

  public Death[] getDeaths() {
    Object deaths = getJSONObjectAttribute(playerFile, name, "deaths");
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
      setJSONObjectAttribute(playerFile, name, "deaths", deathsJson);
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
    Object boogeyMan = getJSONObjectAttribute(playerFile, name, "boogeyman");
    return boogeyMan instanceof Boolean && (boolean) boogeyMan;
  }

  public void setBoogeyMan(boolean boogeyMan) {
    setJSONObjectAttribute(playerFile, name, "boogeyman", boogeyMan);
  }
}
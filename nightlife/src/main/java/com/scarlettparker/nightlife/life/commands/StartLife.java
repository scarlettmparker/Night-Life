package com.scarlettparker.nightlife.life.commands;

import org.bukkit.plugin.java.JavaPlugin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.scarlettparker.nightlife.life.object.TPlayer;
import com.scarlettparker.nightlife.life.object.Death;
import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;
import static com.scarlettparker.nightlife.life.utils.WorldUtils.*;
import static com.scarlettparker.nightlife.Plugin.*;

public class StartLife implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player && !sender.isOp()) {
      sender.sendMessage("§cYou do not have permission to use this command.");
      return true;
    }

    if (JSONFileExists(playerFile) && (args.length != 1 || !Objects.equals(args[0], "confirm"))) {
      sender.sendMessage("§cPlayer file already exists. Please type /startlife confirm to confirm that you want to reset.");
      return true;
    }

    sender.sendMessage("Starting new game of Night Life...");
    createJSONFile(playerFile);

    setGameRules();
    sender.sendMessage("§aGame rules successfully initialized.");

    for (Player p : getAllPlayers()) {
      createPlayer(p);
      p.setHealth(20);
      p.setFoodLevel(20);
    }

    sender.sendMessage("§aLives successfully initialized.");
    return true;
  }

  /**
   * Helper functionn to create a new player in the player file.
   * Sets the default number of lives to the number found in the config.
   * @param p Player to create.
   */
  public static void createPlayer(Player p) {
    JsonObject playersObject = readJSONFile(playerFile);
    if (playersObject == null) {
      playersObject = new JsonObject();
    }

    UUID playerUUID = p.getUniqueId();
    TPlayer tempPlayer = new TPlayer(playerUUID);
    Gson gson = new Gson();
    JsonElement playerElement = gson.toJsonTree(tempPlayer);
    playersObject.add(playerUUID.toString(), playerElement);
    
    writeJSONToFile(playerFile, playersObject);
    tempPlayer.setLives(STARTING_LIVES);
    tempPlayer.setDeaths(new Death[]{});
    tempPlayer.setBoogeyMan(false);

    setPlayerName(p, STARTING_LIVES);
  }

  /**
   * Set the game rules for the overworld, nether, and end dimensions.
   */
  private void setGameRules() {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    setGameRulesForDimension(console, "minecraft:overworld");
    setGameRulesForDimension(console, "minecraft:the_nether");
    setGameRulesForDimension(console, "minecraft:the_end");
  }

  /**
   * Set the specific game rules per dimension.
   * Game rules are:
   * - logAdminCommands false. This prevents the server from logging commands run by admins.
   * - sendCommandFeedback false. This prevents the server from sending command feedback to the player.
   * - keepInventory true. This prevents the player from losing their inventory upon death.
   * - doInsomnia false. This prevents phantoms from spawning.
   * - difficulty hard. This sets the difficulty to hard.
   * @param console Console command sender to run the commands.
   * @param dimension Dimension to set the game rules for.
   */
  private void setGameRulesForDimension(ConsoleCommandSender console, String dimension) {
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run gamerule logAdminCommands false");
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run gamerule sendCommandFeedback false");
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run gamerule keepInventory true");
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run gamerule doInsomnia false");
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run gamerule minecartMaxSpeed 1000");
    Bukkit.dispatchCommand(console, "execute in " + dimension + " run difficulty hard");
  }
}
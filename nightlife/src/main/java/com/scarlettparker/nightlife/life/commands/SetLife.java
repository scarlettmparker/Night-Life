package com.scarlettparker.nightlife.life.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.Objects;

import com.scarlettparker.nightlife.life.object.TPlayer;
import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;

public class SetLife implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    int lives;
    Player player = Bukkit.getPlayer(args[0]);

    if (sender instanceof Player && !sender.isOp()) {
      sender.sendMessage("§cYou do not have permission to use this command.");
      return true;
    }
    
    if (args.length != 2) {
      sender.sendMessage("§cUsage: /setlife <player> <lives>");
      return true;
    }

    if (!JSONFileExists(playerFile)) {
      sender.sendMessage("§cPlayer file does not exist. Please type /startlife to create a new game.");
      return true;
    }

    if (!playerExists(args[0]) || player == null) {
      sender.sendMessage("§cPlayer not found.");
      return true;
    }

    try {
      lives = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      sender.sendMessage("§cInvalid number of lives.");
      return true;
    }

    if (lives < 1) {
      sender.sendMessage("§cNumber of lives must be greater than 0.");
      return true;
    }

    TPlayer tempPlayer = new TPlayer(player.getName());
    if (tempPlayer.getLives() == lives) {
      sender.sendMessage("§cPlayer already has " + lives + " lives.");
      return true;
    }

    if (tempPlayer.getLives() == 0) {
      player.setGameMode(GameMode.SURVIVAL);
      player.teleport(player.getWorld().getSpawnLocation());
    }

    tempPlayer.setLives(lives);
    player.sendMessage("§aYour lives have been set to " + lives + ".");

    return true;
  }
}
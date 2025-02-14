package com.scarlettparker.nightlife.life.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

import com.scarlettparker.nightlife.life.object.TPlayer;
import static com.scarlettparker.nightlife.life.utils.ConfigUtils.*;

public class Lives implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cYou must be a player to use this command.");
      return true;
    }

    if (args.length > 0) {
      sender.sendMessage("§cUsage: /lives");
      return true;
    }

    Player player = (Player) sender;
    UUID playerUUID = player.getUniqueId();

    if (!playerExists(playerUUID) || player == null) {
      sender.sendMessage("§cData not found.");
      return true;
    }

    TPlayer tempPlayer = new TPlayer(playerUUID);
    String prefix = "";
    int lives = tempPlayer.getLives();

    if (lives <= 0) {
      sender.sendMessage("§7You are dead.");
      return true;
    } else if (lives == 1) {
      prefix = "§c";
    } else if (lives == 2) {
      prefix = "§e";
    } else if (lives == 3) {
      prefix = "§a";
    } else {
      prefix = "§2";
    }

    sender.sendMessage(prefix + "You have " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
    return true;
  }
}
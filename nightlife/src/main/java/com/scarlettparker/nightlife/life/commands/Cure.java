package com.scarlettparker.nightlife.life.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import com.scarlettparker.nightlife.life.object.TPlayer;

public class Cure implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player && !sender.isOp()) {
      sender.sendMessage("§cYou do not have permission to use this command.");
      return true;
    }

    if (args.length != 1) {
      sender.sendMessage("§cUsage: /cure <player>");
      return true;
    }

    // cure all players
    if (args[0].equals("all")) {
      for (Player p : Bukkit.getOnlinePlayers()) {
        TPlayer tempPlayer = new TPlayer(p.getName());
        if (tempPlayer.getBoogeyMan()) {
          tempPlayer.setBoogeyMan(false);
          p.sendTitle("§aYou have been cured!", "", 10, 70, 20);
        }
      }
      return true;
    }

    TPlayer tempPlayer = new TPlayer(args[0]);
    if (!tempPlayer.getBoogeyMan()) {
      sender.sendMessage("§c" + args[0] + " is not the Boogeyman.");
      return true;
    }

    tempPlayer.setBoogeyMan(false);
    Player player = Bukkit.getPlayer(args[0]);
    if (player != null) {
      player.sendTitle("§aYou have been cured!", "", 10, 70, 20);
    }
    return true;
  }
}
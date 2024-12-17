package com.scarlettparker.nightlife.life.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import com.scarlettparker.nightlife.life.object.TPlayer;
import static com.scarlettparker.nightlife.life.utils.WorldUtils.*;

public class Boogey implements CommandExecutor {
  static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("nightlife");

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player && !sender.isOp()) {
      sender.sendMessage("§cYou do not have permission to use this command.");
      return true;
    }

    if (args.length != 1) {
      sender.sendMessage("§cUsage: /boogey <number>");
      return true;
    }

    int playerCountTemp;
    try {
      playerCountTemp = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      sender.sendMessage("§cNumber of boogeymen must be an integer.");
      return true;
    }

    List<Player> players = getAllPlayers();

    for (Player p : players) {
      TPlayer tempPlayer = new TPlayer(p.getName());
      if (tempPlayer.getBoogeyMan() || tempPlayer.getLives() <= 1) {
        playerCountTemp--;
        players.remove(p);
      }
    }

    final int playerCount = playerCountTemp;

    if (playerCount > players.size()) {
      sender.sendMessage("§cNumber of boogeymen cannot exceed the number of available players " + players.size() + ".");
      return true;
    }
    
    Bukkit.broadcastMessage("§cThe Boogeyman is about to be chosen.");
    BukkitRunnable task = new BukkitRunnable() {
      int count = 3;
    
      @Override
      public void run() {
        if (count > 0) {
          String color = count == 3 ? "§a" : count == 2 ? "§e" : "§c";
          for (Player p : players) {
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            p.sendTitle(color + count, "", 10, 40, 10);
          }
          count--;
        } else {
          this.cancel();
    
          for (Player p : players) {
            p.sendTitle("§eYou are...", "", 20, 60, 20);
          }
    
          Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < playerCount; i++) {
              Player p = players.get(i);
              TPlayer tPlayer = new TPlayer(p.getName());
              tPlayer.setBoogeyMan(true);
              p.sendTitle("§cThe Boogeyman.", "", 20, 80, 10);
            }
    
            for (int i = playerCount; i < players.size(); i++) {
              players.get(i).sendTitle("§aNot The Boogeyman.", "", 20, 80, 10);
            }
          }, 80L); // 4 seconds
        }
      }
    };
    
    task.runTaskTimer(plugin, 40L, 40L); // delay start by 2 seconds
    
    return true;
  }
}
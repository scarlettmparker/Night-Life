package com.scarlettparker.nightlife.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.scarlettparker.nightlife.life.utils.NightUtils;

public class Tell implements CommandExecutor, TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 2) {
      sender.sendMessage("§cUsage: /tell <player> <message>");
      return false;
    }

    Player target = Bukkit.getPlayer(args[0]);
    if (target == null) {
      sender.sendMessage("§cPlayer not found!");
      return false;
    }

    // build message and send to server
    if (!NightUtils.getNightTime()) {
      String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
      Bukkit.broadcastMessage("§c" + sender.getName() + " tried to tell " + target.getName() + "§f: " + message);
    } else {
      sender.sendMessage("§cYou cannot talk at night.");
    }

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    
    if (args.length == 1) {
      String partialName = args[0].toLowerCase();
      completions = Bukkit.getOnlinePlayers().stream()
          .map(Player::getName)
          .filter(name -> name.toLowerCase().startsWith(partialName))
          .collect(Collectors.toList());
    }
    
    return completions;
  }
}
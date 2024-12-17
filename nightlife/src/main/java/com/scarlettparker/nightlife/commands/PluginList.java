package com.scarlettparker.nightlife.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class PluginList implements CommandExecutor, TabExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    sender.sendMessage("§6Server Plugins\n§7 - §aNight Life");
    return true;
  }

  public java.util.List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    return java.util.Collections.emptyList();
  }
}
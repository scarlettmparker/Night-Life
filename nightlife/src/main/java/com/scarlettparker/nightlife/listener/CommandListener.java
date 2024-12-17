package com.scarlettparker.nightlife.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    
  @EventHandler
  public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
    String message = event.getMessage().toLowerCase();
    
    if (message.equals("/plugins") || message.equals("/pl")) {
      event.setCancelled(true);
      event.getPlayer().performCommand("nightlife:plugins");
    }

    if (message.equals("/tell") || message.equals("/msg")
      || message.equals("/w") || message.equals("/tellraw")) {
      event.setCancelled(true);
      event.getPlayer().sendMessage("§cYou cannot use this command.");
    }
  }
}
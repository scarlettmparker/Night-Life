package com.scarlettparker.nightlife.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProtectionListener implements Listener {
  @EventHandler
  public void playerJoinEvent(PlayerJoinEvent event) {
    event.getPlayer().setNoDamageTicks(0);
  }  
}
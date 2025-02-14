package com.scarlettparker.nightlife.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class ProtectionListener implements Listener {
  @EventHandler
  public void playerJoinEvent(PlayerJoinEvent event) {
    event.getPlayer().setNoDamageTicks(0);
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    World world = player.getWorld();
    
    // check if the player is in the Nether
    if (world.getEnvironment() == World.Environment.NETHER) {
      WorldBorder border = world.getWorldBorder();
      Location playerLocation = player.getLocation();
      double borderSize = border.getSize() / 2;
      double borderX = border.getCenter().getX();
      double borderZ = border.getCenter().getZ();
      
      // calculate how far the player is from the border
      double xDist = Math.abs(playerLocation.getX() - borderX);
      double zDist = Math.abs(playerLocation.getZ() - borderZ);
      
      if (xDist > borderSize + 10 || zDist > borderSize + 10) {
        // teleport player to overworld spawn
        World overworld = Bukkit.getWorld("world");
        if (overworld != null) {
          Location spawnLocation = overworld.getSpawnLocation();
          player.teleport(spawnLocation);
          player.sendMessage("§aYou were outside the border and have been teleported to spawn.");
        }
      }
    }
  }
}
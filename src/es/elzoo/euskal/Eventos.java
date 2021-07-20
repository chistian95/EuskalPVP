package es.elzoo.euskal;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Eventos implements Listener {	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.joinMessage(null);
		
		Utils.limpiar(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.quitMessage(null);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Location pos = event.getEntity().getLocation();
		Optional<Arena> arena = Arena.getArena(pos);
		if(!arena.isPresent()) {
			if(event.getFoodLevel() < event.getEntity().getFoodLevel()) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		event.setCancelled(true);
		
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}		
		Player victima = (Player) event.getEntity();
		
		Optional<Arena> arena = Arena.getArena(victima);
		if(!arena.isPresent()) {
			event.setCancelled(true);
		}
 	}
}

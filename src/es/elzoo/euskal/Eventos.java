package es.elzoo.euskal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
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
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.deathMessage(null);
	}
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
		
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		
		Player victima = (Player) event.getEntity();
		Player atacante = null;
		
		if(event.getDamager().getType() == EntityType.PLAYER) {
			atacante = (Player) event.getDamager();
		} else if(event.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) event.getDamager();
			if(proj.getShooter() instanceof Player) {
				atacante = (Player) proj.getShooter();
			}
		}
		if(atacante == null) {
			return;
		}
 	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}
}

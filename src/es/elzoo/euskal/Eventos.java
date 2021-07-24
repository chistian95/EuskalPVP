package es.elzoo.euskal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventos implements Listener {	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {		
		Utils.limpiar(event.getPlayer());
	}
}

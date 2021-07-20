package es.elzoo.euskal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;

public class Utils {	
	public static Component txt(String msg) {
		msg = mensajeColor(msg);
		return Component.text(msg);
	}
	
	public static String mensajeColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static void broadcast(String msg) {
		Bukkit.getServer().broadcast(Utils.txt(msg));
		Bukkit.getLogger().info(mensajeColor(msg));
	}
	
	public static void tpSpawn(Player player) {
		if(player == null || !player.isOnline()) {
			return;
		}
		
		player.teleportAsync(Bukkit.getWorlds().get(0).getSpawnLocation());
		player.getInventory().clear();
	}
	
	public static void limpiar(Player player) {
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		
		tpSpawn(player);
	}
}

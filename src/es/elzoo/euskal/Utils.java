package es.elzoo.euskal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
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
	
	public static Location stringToLoc(String locRaw) {
		String[] split = locRaw.split(",");
		
		World mundo = Bukkit.getWorld(split[0]);
		
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);
		
		float pitch = Float.parseFloat(split[4]);
		float yaw = Float.parseFloat(split[5]);
		
		return new Location(mundo, x, y, z, yaw, pitch);
	}
	
	public static String LocToString(Location loc) {
		return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getPitch()+","+loc.getYaw();
	}
	
	public static Location locFromConfig(ConfigurationSection conf) {
		World mundo = Bukkit.getWorld(conf.getString("mundo"));
		double x = conf.getDouble("x");
		double y = conf.getDouble("y");
		double z = conf.getDouble("z");
		float pitch = (float) conf.getDouble("pitch");
		float yaw = (float) conf.getDouble("yaw"); 
		
		return new Location(mundo, x, y, z, yaw, pitch);
	}
}

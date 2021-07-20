package es.elzoo.euskal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class Arena {
	private static Map<String, Arena> arenas = new HashMap<String, Arena>();
	
	private Location pos1, pos2;
	private String nombre;
	
	public Arena(String nombre, Location pos1, Location pos2) {
		this.nombre = nombre;
		this.pos1 = pos1;
		this.pos2 = pos2;
		
		arenas.put(nombre, this);
	}
	
	public void luchar(Player p1, Player p2) {
		preparar(p1, pos1);
		preparar(p2, pos2);
		
		Utils.broadcast("&6�Ha comenzado el combate de&r "+p1.getName()+" &6vs&r "+p2.getName()+"&6!");
	}
	
	public void parar() {
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ProtectedRegion reg = container.get(new BukkitWorld(pos1.getWorld())).getRegion(nombre);
		
		pos1.getWorld().getPlayers().forEach(pl -> {
			if(pl.getGameMode() != GameMode.SURVIVAL) {
				return;
			}
			
			Location pos = pl.getLocation();
			if(!reg.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ())) {
				return;
			}
			
			Utils.limpiar(pl);
		});
	}
	
	private static void preparar(Player player, Location pos) {
		//TODO Dar kit, curar y hacer TP
	}
	
	public static Optional<Arena> getArena(String nombre) {
		return arenas.values().stream().filter(entry -> entry.getNombre().equalsIgnoreCase(nombre)).findFirst();
	}
	
	public static Optional<Arena> getArena(Player player) {
		return getArena(player.getLocation());
	}
	
	public static Optional<Arena> getArena(Location loc) {
		Optional<Arena> arena = Optional.ofNullable(null);
		
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regiones = container.get(new BukkitWorld(loc.getWorld()));
		
		Optional<String> arenaId = regiones.getApplicableRegions(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
			.getRegions()
			.stream()
			.filter((rg) -> arenas.keySet().stream().anyMatch(ar -> ar.equalsIgnoreCase(rg.getId())))
			.map(rg -> rg.getId())
			.findFirst();
		
		if(arenaId.isPresent()) {
			arena = getArena(arenaId.get());
		}
		
		return arena;
	}
	
	public static Optional<Arena> getPrimeraArena() {
		return arenas.values().stream().findFirst();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Location getPos1() {
		return pos1;
	}
	
	public Location getPos2() {
		return pos2;
	}
}

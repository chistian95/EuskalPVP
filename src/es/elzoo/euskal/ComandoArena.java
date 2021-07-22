package es.elzoo.euskal;

import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class ComandoArena implements CommandExecutor {	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if(!sender.hasPermission("superadmin")) {
			return true;
		}
		
		if(args.length < 1) {
			sender.sendMessage(Utils.txt("&7/arena luchar <p1> <p2> [<arena>]"));
			sender.sendMessage(Utils.txt("&7/arena parar [<arena>]"));
			sender.sendMessage(Utils.txt("&7/arena reload"));
			sender.sendMessage(Utils.txt("&7/arena crear <arena>"));
			sender.sendMessage(Utils.txt("&7/arena editar <arena> (pos1|pos2)"));
			sender.sendMessage(Utils.txt("&7/arena borrar <arena>"));
			sender.sendMessage(Utils.txt("&7/arena lista"));
		} else if(args[0].equalsIgnoreCase("parar")) {
			Optional<Arena> arena = Arena.getPrimeraArena();
			if(args.length >= 2) {
				arena = Arena.getArena(args[1]);
			}
			
			if(!arena.isPresent()) {
				sender.sendMessage(Utils.txt("&cArena no encontrada."));
				return true;
			}
			
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			ProtectedRegion reg = container.get(new BukkitWorld(arena.get().getPos1().getWorld())).getRegion(arena.get().getNombre());
			if(reg == null) {
				sender.sendMessage(Utils.txt("&cDebes crear la región primero."));
				return true;
			}
			
			arena.get().parar();
		} else if(args[0].equalsIgnoreCase("reload")) {
			EuskalPVP euskal = (EuskalPVP) Bukkit.getPluginManager().getPlugin("EuskalPVP");
			euskal.reloadConfig();
		} else if(args[0].equalsIgnoreCase("crear")) {
			if(args.length < 2) {
				sender.sendMessage(Utils.txt("&c/arena crear <arena>"));
				return true;
			}
			
			Optional<Arena> arena = Arena.getArena(args[1]);
			if(arena.isPresent()) {
				sender.sendMessage("&cYa hay una arena con ese nombre");
				return true;
			}
			
			
			
			Location loc = Bukkit.getWorlds().get(0).getSpawnLocation();
			Arena nueva = new Arena(args[1], loc, loc);
			nueva.guardarConfig();
			
			sender.sendMessage(Utils.txt("&aArena creada. Coloca los spawns."));
		} else if(args[0].equalsIgnoreCase("editar")) {
			if(args.length < 3) {
				sender.sendMessage(Utils.txt("&c/arena editar <arena> (pos1|pos2)"));
				return true;
			}
			
			if(!(sender instanceof Player)) {
				sender.sendMessage(Utils.txt("&cEste comando solo puede ser ejecutado por un jugador."));
				return true;
			}			
			Player player = (Player) sender;
			
			Optional<Arena> arena = Arena.getArena(args[1]);
			if(!arena.isPresent()) {
				sender.sendMessage(Utils.txt("&cArena no encontrada."));
			}
			
			Location pos = player.getLocation();
			if(args[2].equalsIgnoreCase("pos1")) {
				arena.get().setPos1(pos);
			} else if(args[2].equalsIgnoreCase("pos2")) {
				arena.get().setPos2(pos);
			} else {
				sender.sendMessage(Utils.txt("&c/arena editar <arena> (pos1|pos2)"));
				return true;
			}
			
			arena.get().guardarConfig();
			sender.sendMessage(Utils.txt("&7Arena editada."));
		} else if(args[0].equalsIgnoreCase("borrar")) {
			if(args.length < 2) {
				sender.sendMessage(Utils.txt("&c/arena borrar <arena>"));
				return true;
			}
			
			Optional<Arena> arena = Arena.getArena(args[1]);
			if(!arena.isPresent()) {
				sender.sendMessage(Utils.txt("&cArena no encontrada."));
				return true;
			}
			
			arena.get().borrar();
			sender.sendMessage(Utils.txt("&7Arena borrada."));
		} else if(args[0].equalsIgnoreCase("lista")) {
			String txt = "&7Arenas: &r"+Arena.arenas.keySet().stream().collect(Collectors.joining(", "));
			sender.sendMessage(Utils.txt(txt));
		} else if(args[0].equalsIgnoreCase("luchar")) {
			if(args.length < 3) {
				sender.sendMessage(Utils.txt("&c/arena luchar <p1> <p2> [<arena>]"));
				return true;
			}
			
			Player p1 = Bukkit.getPlayer(args[1]);
			Player p2 = Bukkit.getPlayer(args[2]);
			
			Optional<Arena> arena = Arena.getPrimeraArena();
			if(args.length >= 4) {
				arena = Arena.getArena(args[3]);
			}
			
			if(!arena.isPresent()) {
				sender.sendMessage(Utils.txt("&cArena no encontrada."));
				return true;
			}
			
			arena.get().luchar(p1, p2);
		}
		
		return true;
	}
}

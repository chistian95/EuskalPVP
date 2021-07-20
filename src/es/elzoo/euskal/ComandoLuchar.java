package es.elzoo.euskal;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoLuchar implements CommandExecutor {	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if(!sender.hasPermission("superadmin")) {
			return true;
		}
		
		if(args.length < 1) {
			sender.sendMessage(Utils.txt("&7/luchar <p1> <p2> [<arena>]"));
			sender.sendMessage(Utils.txt("&7/luchar parar [<arena>]"));
		} else if(args[0].equalsIgnoreCase("parar")) {
			Optional<Arena> arena = Arena.getPrimeraArena();
			if(args.length >= 2) {
				arena = Arena.getArena(args[1]);
			}
			
			if(!arena.isPresent()) {
				sender.sendMessage(Utils.txt("&cArena no encontrada."));
				return true;
			}
			
			arena.get().parar();
		} else {
			Player p1 = Bukkit.getPlayer(args[0]);
			Player p2 = Bukkit.getPlayer(args[1]);
			
			Optional<Arena> arena = Arena.getPrimeraArena();
			if(args.length >= 3) {
				arena = Arena.getArena(args[2]);
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

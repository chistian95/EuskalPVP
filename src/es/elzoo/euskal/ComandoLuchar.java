package es.elzoo.euskal;

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
		
		if(args.length < 2) {
			sender.sendMessage("/luchar <p1> <p2> [<arena>]");
			sender.sendMessage("/luchar parar [<arena>]");
		} else if(args[0].equalsIgnoreCase("parar")) {
			
		} else {
			Player p1 = Bukkit.getPlayer(args[0]);
			Player p2 = Bukkit.getPlayer(args[1]);
			
			boolean isClasi = true;
			if(args.length >= 3) {
				isClasi = false;
			}
			
			
		}
		
		return true;
	}
}

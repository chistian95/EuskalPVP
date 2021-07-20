package es.elzoo.euskal;

import org.bukkit.plugin.java.JavaPlugin;

public class EuskalPVP extends JavaPlugin {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Eventos(), this);
		
		getCommand("luchar").setExecutor(new ComandoLuchar());	
	}
}

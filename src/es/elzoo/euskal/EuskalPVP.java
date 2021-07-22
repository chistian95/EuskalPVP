package es.elzoo.euskal;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class EuskalPVP extends JavaPlugin {
	@Override
	public void onEnable() {
		getConfig().addDefault("arenas", new ArrayList<Object>());
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getServer().getPluginManager().registerEvents(new Eventos(), this);
		
		getCommand("arena").setExecutor(new ComandoArena());
		
		cargarArenas();
	}
	
	@Override
	public void reloadConfig() {
		super.reloadConfig();
		
		cargarArenas();
	}
	
	private void cargarArenas() {
		ConfigurationSection arenasRaw = getConfig().getConfigurationSection("arenas");
		arenasRaw.getValues(false).forEach((arenaid, arenaRaw) -> {
			ConfigurationSection arenaConf = (ConfigurationSection) arenaRaw;
			
			ConfigurationSection pos1Conf = arenaConf.getConfigurationSection("pos1");
			Location pos1 = Utils.locFromConfig(pos1Conf);			
			
			ConfigurationSection pos2Conf = arenaConf.getConfigurationSection("pos2");
			Location pos2 = Utils.locFromConfig(pos2Conf);
			
			
			Arena arena = new Arena(arenaid, pos1, pos2);
			Arena.arenas.put(arenaid, arena);
		});
	}
}

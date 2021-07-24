package es.elzoo.euskal;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EuskalPVP extends JavaPlugin {
	private static PotionEffect fuerza = new PotionEffect(PotionEffectType.SPEED, 160, 1, true, false, false);
	private static PotionEffect velocidad = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 0, true, false, false);
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getServer().getPluginManager().registerEvents(new Eventos(), this);
		
		getCommand("arena").setExecutor(new ComandoArena());
		
		cargarArenas();
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
			getServer().getOnlinePlayers().forEach(pl -> {
				pl.addPotionEffect(fuerza);
				pl.addPotionEffect(velocidad);
			});
		}, 10, 10);
	}
	
	@Override
	public void reloadConfig() {
		super.reloadConfig();
		
		cargarArenas();
	}
	
	private void cargarArenas() {
		ConfigurationSection arenasRaw = getConfig().getConfigurationSection("arenas");
		if(arenasRaw != null) {
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
}
